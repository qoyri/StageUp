package com.example.stageup.data.network;


import com.example.stageup.data.model.PingResponse;
import com.example.stageup.data.model.TokenResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface HealthService {
    // Ping le serveur pour vérifier l'état
    @GET("api/v1/Health/ping")
    Call<PingResponse> pingServer();

    // Vérifie la validité du token
    @GET("api/v1/Health/check-token")
    Call<TokenResponse> checkToken(@Header("Authorization") String token);
}
