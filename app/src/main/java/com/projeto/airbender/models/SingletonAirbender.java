package com.projeto.airbender.models;

import android.content.Context;
import android.widget.Toast;

import java.util.Base64;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.projeto.airbender.listeners.LoginListener;
import com.projeto.airbender.utils.JsonParser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SingletonAirbender {
    private static SingletonAirbender instance = null;
    private static RequestQueue requestQueue = null;

    private static String SERVER = "192.168.45.129"; // test server
    private static String LOCALHOST = "10.0.2.2"; // localhost
    private static final String URL = "http://" + LOCALHOST + "/plsi/airbender/backend/web/api/";
    private static String TOKEN = null;

    private LoginListener loginListener;
    // private DetalhesListener detalhesListener;

    public static synchronized SingletonAirbender getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonAirbender(context);
            requestQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }

    private SingletonAirbender(Context context) {
        //gerarDadosDinamicos();
        //livros = new ArrayList<>();
        //livroBD = new LivroBDHelper(context);
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void loginAPI(final Context context, String username, String password) {
        if (!JsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
        } else {
            StringRequest req = new StringRequest(Request.Method.GET, URL + "login", new Response.Listener<String>() {
                // Sucesso
                @Override
                public void onResponse(String response) {
                    loginListener.onAttemptLogin(JsonParser.parserJsonLogin(response).get("token"), Integer.parseInt(JsonParser.parserJsonLogin(response).get("id")), JsonParser.parserJsonLogin(response).get("role"));
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

                    if (!(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)) {
                        return null;
                    }
                    byte[] base64 = Base64.getEncoder().encode((username + ':' + password).getBytes());
                    params.put("Authorization", "Basic " + new String(base64));
                    params.put("User-Agent", "Mozilla/5.0");

                    return params;
                }
            };
            requestQueue.add(req);
        }

    }

    //public void setLivrosListener(LivrosListener listener) {
    //   this.listener = listener;
    //}

    //public void setDetalhesListener(DetalhesListener detalhesListener) {
    //   this.detalhesListener = detalhesListener;
    //}

    //private void gerarDadosDinamicos(){
    //   livros = new ArrayList<>();
    //}

    /*public Livro getLivro(int id) {
        for (Livro livro : livros) {
            if (livro.getId() == id) {
                return livro;
            }
        }
        return null;
    }*/


    /*public void adicionarLivroAPI(final Livro livro, final Context context){
        if(!LivroJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
        }
        else {
            StringRequest req = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    adicionarLivroBD(LivroJsonParser.parserJsonLivro(response));
                    if (detalhesListener != null)
                        detalhesListener.onRefreshDetalhes(MenuMainActivity.ADD);
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
    }

    public void getAllLivrosAPI(final Context context){
        if(!LivroJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_LONG).show();
            if (listener != null) {
                listener.onRefreshListaLivros(getLivrosBD());
            }
        }
        else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL,null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    livros = LivroJsonParser.parserJsonLivros(response);
                    atualizarLivrosBD(livros);
                    if (listener != null) {
                        listener.onRefreshListaLivros(livros);
                    }
                }},
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage()+ "", Toast.LENGTH_LONG).show();
                        }
                    }
            );
            requestQueue.add(req);
        }
    }

    public void removerLivroAPI(final Livro livro, final Context context){
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
