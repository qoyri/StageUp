package com.example.stageup.utils;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONObject;

public class PreferenceManager {

    private static final String PREFERENCES_NAME = "com.example.stageup.PREFERENCES";
    private static final String KEY_AUTH_TOKEN = "auth_token";

    private final SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    // Sauvegarder le token
    public void saveAuthToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.apply();
    }

    // Récupérer le token
    public String getAuthToken() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    // Supprimer le token
    public void clearAuthToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_AUTH_TOKEN);
        editor.apply();
    }
    public int getUserIdFromToken() {
        String token = getAuthToken();
        if (token == null || token.isEmpty()) {
            return -1;
        }

        try {
            String[] parts = token.split("\\."); // JWT est composé de trois parties : header.payload.signature
            String payload = new String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE));
            JSONObject jsonObject = new JSONObject(payload);
            return jsonObject.getInt("user_id"); // Assurez-vous que "user_id" correspond à la clé utilisée dans le JWT
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}