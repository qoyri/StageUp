package com.example.stageup.data.network;


import java.util.List;

import com.example.stageup.data.model.Stage;
import com.example.stageup.data.model.StageResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface StageService {

    /**
     * Récupérer les stages par ID d'entreprise.
     *
     * @param authorization Le jeton d'autorisation (Bearer token).
     * @param entrepriseId  L'ID de l'entreprise pour laquelle récupérer les stages.
     * @return Une liste des stages liés à l'entreprise.
     */
    @GET("/api/v1/Stages")
    Call<StageResponse<Stage>> getStagesByEntreprise(
            @Header("Authorization") String authorization,
            @Query("entreprise_id") int entrepriseId
    );
}