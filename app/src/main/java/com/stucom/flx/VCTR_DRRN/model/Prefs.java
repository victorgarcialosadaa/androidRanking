package com.stucom.flx.VCTR_DRRN.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class Prefs {

    /*SharedPreference of Player*/
    private Player player;
    private String token;
    private Context context;
   private static Prefs instance;

    public Prefs(Context context) {
        this.context = context;
        //cargar los datos
        loadFromPrefs(context);
    }

    // Obtenir (i crear) la instància. Solo se instancia una vez
    public static Prefs getInstance(Context context) {
        if (instance == null) {
            instance = new Prefs(context.getApplicationContext());
        }
        return instance;
    }


    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public  String getToken() {return token;}

    public void setToken(String token) {this.token = token;}

    public void loadFromPrefs(Context context) {
        Gson gson = new Gson();
        SharedPreferences prefs = this.context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        /*The next line we change the String player to an Object/Class Player*/
        this.player = gson.fromJson(prefs.getString("currentPlayer","null"), Player.class);
        this.token = prefs.getString("currentToken","");

    }

    public void saveToPrefs(Context context) {
        Gson gson = new Gson();
        SharedPreferences prefs = this.context.getSharedPreferences("MyPref", context.MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("currentPlayer", gson.toJson(this.player));
        prefsEditor.putString("currentToken", token);
        prefsEditor.apply();
    }
}
