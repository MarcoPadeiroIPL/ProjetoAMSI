package com.projeto.airbender.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    public static String parserJsonLogin(String response) {
        String token = null;
        try {
            JSONObject login = new JSONObject(response);
            if(login.getInt("status") == 200)
                token = login.getString("token");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return token;
    }

    public static boolean isConnectionInternet(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network ni = cm.getActiveNetwork();
        return ni != null && cm.getNetworkInfo(ni).isConnected();
    }
}
