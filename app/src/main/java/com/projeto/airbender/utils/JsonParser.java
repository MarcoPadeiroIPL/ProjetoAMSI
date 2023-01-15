package com.projeto.airbender.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;

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
        try {
            JSONObject login = new JSONObject(response);
            map.put("token", login.getString("token"));
            map.put("role", login.getString("role"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static boolean isConnectionInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network ni = cm.getActiveNetwork();
        return ni != null && cm.getNetworkInfo(ni).isConnected();
    }
}
