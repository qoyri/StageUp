package com.example.stageup.data.network;

import com.example.stageup.data.model.EtudiantResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface EtudiantService {

    @GET("api/v1/Etudiants")
    Call<EtudiantResponse> getEtudiants(
            @Header("Authorization") String token,
            @Query("page") int page,
            @Query("pageSize") int pageSize
    );
}