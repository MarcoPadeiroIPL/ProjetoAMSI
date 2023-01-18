package com.projeto.airbender.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.Base64;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.projeto.airbender.listeners.BalanceReqListener;
import com.projeto.airbender.listeners.LoginListener;
import com.projeto.airbender.utils.ContentValuesHelper;
import com.projeto.airbender.utils.DBHelper;
import com.projeto.airbender.utils.JsonParser;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SingletonAirbender {
    private static SingletonAirbender instance = null;
    private static RequestQueue requestQueue = null;
    private DBHelper dbHelper;

    private ArrayList<BalanceReq> balanceReqs;

    private static final String PATH = "/sis/airbender/backend/web/api/";

    private LoginListener loginListener;
    private BalanceReqListener balanceReqListener;

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
        gerarDadosDinamicos();
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

    public void loginAPI(final Context context, String username, String password) {
        if (!JsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(context, "Server error", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                if (error.networkResponse.statusCode == 403) {
                                    Toast.makeText(context, "Wrong credentials", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception ex) {
                                Toast.makeText(context, "Server not found", Toast.LENGTH_LONG).show();
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

    private void gerarDadosDinamicos() {
        balanceReqs = new ArrayList<BalanceReq>();
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

    public void addBalanceReqAPI(final int amount, final Context context) {
        if (!JsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
        } else {
            StringRequest req = new StringRequest(Request.Method.POST, makeURL(getServer(context), "balance-req", getToken(context)), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dbHelper.insertDB("balanceReq", contentValuesHelper.getBalanceReq(JsonParser.parseBalanceReq(response)));
                    if (balanceReqListener != null)
                        balanceReqListener.onRefreshBalanceReqList(dbHelper.getAllBalanceReqDB());
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("amount", amount + "");
                    return params;
                }
            };
            requestQueue.add(req);
        }
    }

    public void getAllBalanceReqs(final Context context) {
        if (!JsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
            if (balanceReqListener != null) {
                balanceReqListener.onRefreshBalanceReqList(dbHelper.getAllBalanceReqDB());
            }
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, makeURL(getServer(context), "balance-req", getToken(context)), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    balanceReqs = JsonParser.parseBalanceReqs(response);
                    if(dbHelper.getAllBalanceReqDB().size() == 0) {
                        for (BalanceReq balanceReq : balanceReqs) {
                            dbHelper.insertDB("balanceReq", contentValuesHelper.getBalanceReq(balanceReq));
                        }
                    }
                    if (balanceReqListener != null)
                        balanceReqListener.onRefreshBalanceReqList(dbHelper.getAllBalanceReqDB());
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage() + "", Toast.LENGTH_LONG).show();
                        }
                    }
            );
            requestQueue.add(req);
        }
    }

    /*public void removerLivroAPI(final Livro livro, final Context context){
        if(!LivroJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
            );
            requestQueue.add(req);
        }
    }

    public void editarLivroAPI(final Livro livro, final Context context){
        if(!LivroJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
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
