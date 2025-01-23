// EntrepriseService.java
package com.example.stageup.data.network;

import java.util.List;

import com.example.stageup.data.model.Entreprise;
import com.example.stageup.data.model.EntrepriseResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface EntrepriseService {

    @GET("api/v1/Entreprise")
    Call<EntrepriseResponse> getEntreprises(
            @Header("Authorization") String token,
            @retrofit2.http.Query("page") int page,
            @retrofit2.http.Query("pageSize") int pageSize
    );

    @GET("api/v1/Entreprise/{id}")
    Call<Entreprise> getEntrepriseById(
            @Header("Authorization") String token,
            @Path("id") int id
    );
}