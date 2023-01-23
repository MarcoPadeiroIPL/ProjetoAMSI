package com.projeto.airbender.models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import java.security.Timestamp;
import java.util.Arrays;
import java.util.Base64;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.projeto.airbender.activities.LoginActivity;
import com.projeto.airbender.listeners.BalanceReqListener;
import com.projeto.airbender.listeners.LoginListener;
import com.projeto.airbender.listeners.TicketListener;
import com.projeto.airbender.utils.ContentValuesHelper;
import com.projeto.airbender.utils.DBHelper;
import com.projeto.airbender.utils.JsonParser;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SingletonAirbender {
    private static SingletonAirbender instance = null;
    private static RequestQueue requestQueue = null;
    private DBHelper dbHelper;

    private ArrayList<BalanceReq> balanceReqs;
    private ArrayList<TicketInfo> tickets;

    private static final String PATH = "/sis/airbender/backend/web/api/";

    private LoginListener loginListener;
    private BalanceReqListener balanceReqListener;
    private TicketListener ticketUpcomingListener;
    private TicketListener ticketPendingListener;
    private TicketListener ticketPastListener;

    private ContentValuesHelper contentValuesHelper;

    public static synchronized SingletonAirbender getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonAirbender(context);
            requestQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }

    private SingletonAirbender(Context context) {
        dbHelper = new DBHelper(context);
        contentValuesHelper = new ContentValuesHelper();
    }

    public String makeURL(String server, String params, String token) {
        if (token == null)
            return "http://" + server + PATH + params;
        else
            return "http://" + server + PATH + params + "?access-token=" + token;
    }

    public String getServer(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("settings", 0);
        return preferences.getString("SERVER", "");
    }

    public String getToken(Context context) {
        SharedPreferences user = context.getSharedPreferences("user_data", 0);
        return user.getString("TOKEN", "");
    }

    public int getUserID(Context context) {
        SharedPreferences user = context.getSharedPreferences("user_data", 0);
        return user.getInt("ID", 0);
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void setBalanceReqListener(BalanceReqListener balanceReqListener) {
        this.balanceReqListener = balanceReqListener;
    }

    public void setTicketPendingListener(TicketListener ticketListener) {
        this.ticketPendingListener = ticketListener;
    }

    public void setTicketUpcomingListener(TicketListener ticketListener) {
        this.ticketUpcomingListener = ticketListener;
    }

    public void setTicketPastListener(TicketListener ticketListener) {
        this.ticketPastListener = ticketListener;
    }

    public void updateUserData(final Context context) {
        if (!JsonParser.isConnectionInternet(context)) {
            return;
        } else {
            StringRequest req = new StringRequest(Request.Method.GET, makeURL(getServer(context), "user", getToken(context)), new Response.Listener<String>() {
                // Sucesso
                @Override
                public void onResponse(String response) {
                    loginListener.updateUserData(JsonParser.parseJsonUpdateUserData(response));
                }
            },
                    // erro
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                if (error.networkResponse.statusCode == 500) {
                                    Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (error.networkResponse.statusCode == 403) {
                                    Toast.makeText(context, "Wrong credentials", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception ex) {
                                Toast.makeText(context, "Server not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );
            requestQueue.add(req);
        }

    }

    public void loginAPI(final Context context, String username, String password) {
        if (!JsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        } else {
            StringRequest req = new StringRequest(Request.Method.GET, makeURL(getServer(context), "login", null), new Response.Listener<String>() {
                // Sucesso
                @Override
                public void onResponse(String response) {
                    loginListener.onLogin(JsonParser.parserJsonLogin(response));
                }
            },
                    // erro
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                if (error.networkResponse.statusCode == 500) {
                                    Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (error.networkResponse.statusCode == 403) {
                                    Toast.makeText(context, "Wrong credentials", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception ex) {
                                Toast.makeText(context, "Server not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            ) {
                // Parametros a serem enviados
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();

                    if (!(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O))
                        return null;

                    byte[] base64 = Base64.getEncoder().encode((username + ':' + password).getBytes());
                    params.put("Authorization", "Basic " + new String(base64));

                    return params;
                }
            };
            requestQueue.add(req);
        }

    }

    public BalanceReq getBalanceReq(int id) {
        for (BalanceReq balanceReq : balanceReqs) {
            if (balanceReq.getId() == id) {
                return balanceReq;
            }
        }
        return null;
    }

    /*public void removerLivroBD(int id) {
        Livro livroaux = getLivro(id);
        if (livroaux != null)
            livroBD.removerLivroBD(id);
    }

    public void editarLivroBD(Livro livro) {
        Livro livroaux = getLivro(livro.getId());
        if (livroaux != null)
            livroBD.editarLivroDB(livro);
    }*/


    private Flight findFlight(ArrayList<Flight> flights, int flight_id) {
        for (Flight flight : flights)
            if (flight.getId() == flight_id)
                return flight;
        return null;
    }

    private Airport findAirport(ArrayList<Airport> airports, int airport_id) {
        for (Airport airport : airports) {
            if (airport.getId() == airport_id) {
                return airport;
            }
        }
        return null;
    }

    public boolean airportExists(Airport airport) {
        ArrayList<Airport> airports = dbHelper.getAllAirports();
        for (Airport a : airports) {
            if (a.getId() == airport.getId())
                return true;
        }
        return false;
    }

    public void replaceAirportToUpcoming(Airport airport) {
        ArrayList<Airport> airports = dbHelper.getAllAirports();
        for (Airport a : airports) {
            if (a.getId() == airport.getId() && !Objects.equals(a.getType(), "upcoming")) {
                dbHelper.updateDB("airports", contentValuesHelper.getAirport(airport, "upcoming"));
            }
        }
    }

    public void replaceFlightToUpcoming(Flight flight) {
        ArrayList<Flight> flights = dbHelper.getAllFlights();
        for (Flight f : flights) {
            if (f.getId() == flight.getId() && !Objects.equals(f.getType(), "upcoming")) {
                dbHelper.updateDB("flights", contentValuesHelper.getFlight(flight, "upcoming"));
            }
        }
    }

    public void replaceTicketToUpcoming(Ticket ticket) {
        ArrayList<Ticket> tickets = dbHelper.getAllTickets();
        for (Ticket t : tickets) {
            if (t.getId() == ticket.getId() && !Objects.equals(t.getType(), "upcoming")) {
                dbHelper.updateDB("flights", contentValuesHelper.getTicket(ticket, "upcoming"));
            }
        }
    }

    public boolean ticketExists(Ticket ticket) {
        ArrayList<Ticket> tickets = dbHelper.getAllTickets();
        for (Ticket t : tickets) {
            if (t.getId() == ticket.getId())
                return true;
        }
        return false;
    }
    public boolean flightExists(Flight flight) {
        ArrayList<Flight> flights = dbHelper.getAllFlights();
        for (Flight f : flights) {
            if (f.getId() == flight.getId())
                return true;
        }
        return false;
    }

    public void getTicketsFromDB(int position) {
        final int UPCOMING = 0, PENDING = 1, PAST = 2;
        String type = position == UPCOMING ? "upcoming" : position == PENDING ? "pending" : "past";
        ArrayList<TicketInfo> ticketInfo = new ArrayList<TicketInfo>();
        ArrayList<Ticket> tickets = dbHelper.getTickets("type", type);
        ArrayList<Flight> flights = dbHelper.getFlights("type", type);
        ArrayList<Airport> airports = dbHelper.getAirports("type", type);
        for (Ticket ticket : tickets) {
            Flight flight = findFlight(flights, ticket.getFlight_id());
            Airport airportArrival = findAirport(airports, flight.getAirportArrival());
            Airport airportDeparture = findAirport(airports, flight.getAirportDeparture());

            ticketInfo.add(new TicketInfo(ticket, airportDeparture, airportArrival, flight));
        }
        if (ticketUpcomingListener != null)
            ticketUpcomingListener.onRefreshTicketList(ticketInfo);
    }

    public void requestTicketsAPI(final Context context, int position) {
        if (!JsonParser.isConnectionInternet(context)) {
            Snackbar.make(((Activity) context).findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_SHORT).show();
            return;
        }
        final int UPCOMING = 0, PENDING = 1, PAST = 2;
        String path = position == UPCOMING ? "upcoming" : position == PENDING ? "pending" : "past";
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, makeURL(getServer(context), "tickets/" + path, getToken(context)), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                tickets = JsonParser.parseTickets(response);
                //dbHelper.deleteDB("tickets", "type", path);
                dbHelper.deleteDB("airports", "type", path);
                dbHelper.deleteDB("flights", "type", path);
                dbHelper.deleteDB("tickets", "type", path);

                for (TicketInfo ticket : tickets) {
                    if (!airportExists(ticket.getAirportArrival()))
                        dbHelper.insertDB("airports", contentValuesHelper.getAirport(ticket.getAirportArrival(), path));
                    else
                        replaceAirportToUpcoming(ticket.getAirportArrival());
                    if (!airportExists(ticket.getAirportDeparture()))
                        dbHelper.insertDB("airports", contentValuesHelper.getAirport(ticket.getAirportDeparture(), path));
                    else
                        replaceAirportToUpcoming(ticket.getAirportArrival());
                    if (!flightExists(ticket.getFlight()))
                        dbHelper.insertDB("flights", contentValuesHelper.getFlight(ticket.getFlight(), path));
                    else
                        replaceFlightToUpcoming(ticket.getFlight());
                    if(!ticketExists(ticket.getTicket()))
                        dbHelper.insertDB("tickets", contentValuesHelper.getTicket(ticket.getTicket(), path));
                    else
                        replaceTicketToUpcoming(ticket.getTicket());
                }
                if (position == UPCOMING) {
                    if (ticketUpcomingListener != null)
                        ticketUpcomingListener.onRefreshTicketList(tickets);
                } else if (position == PENDING) {
                    if (ticketPendingListener != null)
                        ticketPendingListener.onRefreshTicketList(tickets);
                } else {
                    if (ticketPastListener != null)
                        ticketPastListener.onRefreshTicketList(tickets);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        requestQueue.add(req);
    }

    public void getAllBalanceReqsDB(final Context context) {
        if (balanceReqListener != null)
            balanceReqListener.onRefreshBalanceReqList(dbHelper.getBalanceReq());
    }

    public void addBalanceReq(final int amount, final Context context) {
        if (!JsonParser.isConnectionInternet(context)) {
            Snackbar.make(((Activity) context).findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_SHORT).show();
        } else {
            try {
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("amount", amount);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, makeURL(getServer(context), "balance-req", getToken(context)), jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                requestBalanceReqsAPI(context);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                    }
                }

                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }
                };
                requestQueue.add(jsonObjectRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<BalanceReq> requestBalanceReqsAPI(final Context context) {
        System.out.println("requestBalanceReqsAPI");
        if (!JsonParser.isConnectionInternet(context)) {
            Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Could not update to most recent information", Snackbar.LENGTH_SHORT).show();
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, makeURL(getServer(context), "balance-req", getToken(context)), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    balanceReqs = JsonParser.parseBalanceReqs(response);
                    dbHelper.deleteAllDB("balanceReq");
                    for (BalanceReq balanceReq : balanceReqs) {
                        dbHelper.insertDB("balanceReq", contentValuesHelper.getBalanceReq(balanceReq));
                    }
                    if (balanceReqListener != null)
                        balanceReqListener.onRefreshBalanceReqList(dbHelper.getBalanceReq());
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage() + "", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            requestQueue.add(req);
        }
        return balanceReqs;
    }

    public boolean deleteBalanceReq(Context context, BalanceReq balanceReq) {
        final boolean[] success = {false};
        if (!JsonParser.isConnectionInternet(context)) {
            Snackbar.make(((Activity) context).findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_SHORT).show();
        } else {
            StringRequest req = new StringRequest(Request.Method.DELETE, makeURL(getServer(context), "balance-req/" + balanceReq.getId(), getToken(context)), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dbHelper.deleteID("balanceReq", balanceReq.getId());
                    if (balanceReqListener != null)
                        balanceReqListener.onRefreshBalanceReqList(dbHelper.getBalanceReq());
                    Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Deleted successfully!", Snackbar.LENGTH_SHORT).show();
                    success[0] = true;
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse.statusCode == 403) {
                                Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Cannot delete decided balance requests", Snackbar.LENGTH_SHORT).show();
                            }
                            success[0] = false;
                        }
                    }
            );
            requestQueue.add(req);
        }
        return success[0];
    }

    /*public void removerLivroAPI(final Livro livro, final Context context){
        if(!LivroJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
        else {
            StringRequest req = new StringRequest(Request.Method.DELETE, URL + "/" + livro.getId(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    removerLivroBD(livro.getId());
                    if (detalhesListener != null)
                        detalhesListener.onRefreshDetalhes(MenuMainActivity.DELETE);
                }},
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            requestQueue.add(req);
        }
    }

    public void editarLivroAPI(final Livro livro, final Context context){
        if(!LivroJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
        else {
            StringRequest req = new StringRequest(Request.Method.PUT, URL + "/" + livro.getId(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    editarLivroBD(livro);
                    if (detalhesListener != null)
                        detalhesListener.onRefreshDetalhes(MenuMainActivity.EDIT);
                }},
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("token", TOKEN);
                    params.put("titulo", livro.getTitulo());
                    params.put("autor", livro.getAutor());
                    params.put("serie", livro.getSerie());
                    params.put("ano", livro.getAno());
                    params.put("capa", livro.getCapa());

                    return params;
                }
            };
            requestQueue.add(req);
        }
    } */
}
