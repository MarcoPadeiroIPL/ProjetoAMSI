package com.projeto.airbender.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;

import com.projeto.airbender.models.Airport;
import com.projeto.airbender.models.BalanceReq;
import com.projeto.airbender.models.Flight;
import com.projeto.airbender.models.Ticket;
import com.projeto.airbender.models.TicketInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JsonParser {

    /*public static ArrayList<Livro> parserJsonLivros(JSONArray response) {
        ArrayList<Livro> livros = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject livro = response.optJSONObject(i);
                int id = livro.optInt("id");
                String titulo = livro.optString("titulo");
                String autor = livro.optString("autor");
                String ano = String.valueOf(livro.optInt("ano"));
                String serie = livro.optString("serie");
                String capa = livro.optString("capa");
                livros.add(new Livro(id, ano, capa, titulo, serie, autor));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return livros;
    }*/

    /*public static Livro parserJsonLivro(String response) {
        Livro livroAux = null;
        try {
            JSONObject livro = new JSONObject(response);
            int id = livro.optInt("id");
            String titulo = livro.optString("titulo");
            String autor = livro.optString("autor");
            String ano = String.valueOf(livro.optInt("ano"));
            String serie = livro.optString("serie");
            String capa = livro.optString("capa");
            livroAux = new Livro(id, ano, capa, titulo, serie, autor);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return livroAux;
    }*/

    public static Map<String, String> parserJsonLogin(String response) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", null);
        map.put("role", null);
        map.put("fName", null);
        map.put("surname", null);
        map.put("nif", null);
        map.put("phone", null);
        map.put("balance", null);
        try {
            JSONObject login = new JSONObject(response);
            map.put("token", login.getString("token"));
            map.put("role", login.getString("role"));
            map.put("fName", login.getString("fName"));
            map.put("surname", login.getString("surname"));
            map.put("nif", login.getString("nif"));
            map.put("phone", login.getString("phone"));
            map.put("balance", String.valueOf(login.getDouble("balance")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static BalanceReq parseBalanceReq(String response) {
        BalanceReq balanceReq = null;
        try {
            JSONObject json = new JSONObject(response);
            int id = json.optInt("id");
            double amount = json.optDouble("amount");
            String requestDate = json.optString("requestDate");
            String decisionDate = json.optString("decisionDate");
            String status = json.optString("status");
            int client_id = json.optInt("client_id");
            balanceReq = new BalanceReq(id, amount, requestDate, decisionDate, status, client_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return balanceReq;
    }

    public static TicketInfo parseTicket(String response) {
        Ticket ticket = null;
        Flight flight = null;
        Airport airportArrival = null;
        Airport airportDeparture = null;
        TicketInfo ticketInfo = null;
        try {
            JSONObject json = new JSONObject(response);
            ticket = new Ticket(json.optInt("id"), json.optString("fName"), json.optString("surname"),
                    json.optString("gender"), json.optInt("age"), json.optInt("checkedIn"), json.optInt("client_id"),
                    json.optInt("flight_id"), json.optString("seatLinha"), json.optInt("seatCol"), json.optInt("luggage_1"),
                    json.optInt("luggage_2"), json.optInt("receipt_id"), json.optInt("tariff_id"), json.optString("tariffType"));

            JSONObject jsonFlight = json.getJSONObject("flight");
            flight = new Flight(jsonFlight.optInt("id"), jsonFlight.optString("departureDate"), json.optString("duration"),
                    jsonFlight.optInt("airplane_id"), jsonFlight.optInt("airportDeparture_id"), jsonFlight.optInt("airportArrival_id"), jsonFlight.optString("status"));

            JSONObject jsonAirportArrival = jsonFlight.getJSONObject("airportArrival");
            airportArrival = new Airport(jsonAirportArrival.optInt("id"), jsonAirportArrival.optString("country"), jsonAirportArrival.optString("code"),
                    jsonAirportArrival.optString("city"), jsonAirportArrival.optInt("search"), jsonAirportArrival.optString("status"));

            JSONObject jsonAirportDeparture = jsonFlight.getJSONObject("airportDeparture");
            airportDeparture = new Airport(jsonAirportDeparture.optInt("id"), jsonAirportDeparture.optString("country"), jsonAirportDeparture.optString("code"),
                    jsonAirportDeparture.optString("city"), jsonAirportDeparture.optInt("search"), jsonAirportDeparture.optString("status"));

            ticketInfo = new TicketInfo(ticket, airportDeparture, airportArrival, flight);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ticketInfo;
    }

    public static ArrayList<TicketInfo> parseTickets(JSONArray response) {
        Ticket ticket = null;
        Flight flight = null;
        Airport airportArrival = null;
        Airport airportDeparture = null;
        ArrayList<TicketInfo> ticketsInfo = new ArrayList<TicketInfo>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject json = response.optJSONObject(i);
                ticket = new Ticket(json.optInt("id"), json.optString("fName"), json.optString("surname"),
                        json.optString("gender"), json.optInt("age"), json.optInt("checkedIn"), json.optInt("client_id"),
                        json.optInt("flight_id"), json.optString("seatLinha"), json.optInt("seatCol"), json.optInt("luggage_1"),
                        json.optInt("luggage_2"), json.optInt("receipt_id"), json.optInt("tariff_id"), json.optString("tariffType"));

                JSONObject jsonFlight = json.getJSONObject("flight");
                flight = new Flight(jsonFlight.optInt("id"), jsonFlight.optString("departureDate"), json.optString("duration"),
                        jsonFlight.optInt("airplane_id"), jsonFlight.optInt("airportDeparture_id"), jsonFlight.optInt("airportArrival_id"), jsonFlight.optString("status"));

                JSONObject jsonAirportArrival = jsonFlight.getJSONObject("airportArrival");
                airportArrival = new Airport(jsonAirportArrival.optInt("id"), jsonAirportArrival.optString("country"), jsonAirportArrival.optString("code"),
                        jsonAirportArrival.optString("city"), jsonAirportArrival.optInt("search"), jsonAirportArrival.optString("status"));

                JSONObject jsonAirportDeparture = jsonFlight.getJSONObject("airportDeparture");
                airportDeparture = new Airport(jsonAirportDeparture.optInt("id"), jsonAirportDeparture.optString("country"), jsonAirportDeparture.optString("code"),
                        jsonAirportDeparture.optString("city"), jsonAirportDeparture.optInt("search"), jsonAirportDeparture.optString("status"));

                ticketsInfo.add(new TicketInfo(ticket, airportDeparture, airportArrival, flight));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ticketsInfo;
    }

    public static ArrayList<BalanceReq> parseBalanceReqs(JSONArray response) {
        ArrayList<BalanceReq> balanceReqs = new ArrayList<BalanceReq>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject balanceReq = response.optJSONObject(i);
                int id = balanceReq.optInt("id");
                double amount = balanceReq.optDouble("amount");
                String requestDate = balanceReq.optString("requestDate");
                String decisionDate = balanceReq.optString("decisionDate");
                String status = balanceReq.optString("status");
                int client_id = balanceReq.optInt("client_id");
                balanceReqs.add(new BalanceReq(id, amount, requestDate, decisionDate, status, client_id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return balanceReqs;
    }

    public static boolean isConnectionInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network ni = cm.getActiveNetwork();
        return ni != null && cm.getNetworkInfo(ni).isConnected();
    }
}
