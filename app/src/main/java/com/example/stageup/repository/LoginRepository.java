package com.example.stageup.repository;

import com.example.stageup.data.model.LoginRequest;
import com.example.stageup.data.model.LoginResponse;
import com.example.stageup.data.network.AuthService;
import com.example.stageup.data.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Response;

public class LoginRepository {

    public static String login(String username, String password) throws Exception {
        // Prépare le service Retrofit
        AuthService authService = RetrofitClient.getInstance().create(AuthService.class);

        // Crée l'objet de la requête
        LoginRequest loginRequest = new LoginRequest(username, password);

        // Appelle l'API de connexion
        Call<LoginResponse> call = authService.login(loginRequest);
        Response<LoginResponse> response = call.execute();

        // Vérifie la réponse
        if (response.isSuccessful() && response.body() != null) {
            // Connexion réussie (retourne le token pour cet exemple)
            return response.body().getToken();
        } else {
            // Erreur de connexion
            throw new Exception(response.errorBody() != null ? response.errorBody().string() : "Unknown error");
        }
    }
}