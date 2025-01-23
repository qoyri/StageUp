package com.example.stageup.data.network;

import com.example.stageup.data.model.*;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessagingService {

    // Get list of conversations
    @GET("api/v1/Messaging/conversations")
    Call<RootResponse> getConversations(@Header("Authorization") String token);

    // Get details of a specific conversation including messages
    @GET("api/v1/Messaging/conversations/{conversationId}")
    Call<ConversationDetailResponse> getConversationDetails(
            @Header("Authorization") String token,
            @Path("conversationId") int conversationId
    );

    // Send a message to a conversation
    @POST("/api/v1/Messaging/conversations/{conversationId}/messages")
    Call<Void> sendMessage(
            @Header("Authorization") String authorizationHeader, // Authentification
            @Path("conversationId") int conversationId,          // Insérez l'ID de la conversation
            @Body MessageRequestBody messageRequestBody          // Corps de la requête avec le message
    );

    @POST("/api/v1/Messaging/conversations")
    Call<ConversationResponse> createConversation(
            @Header("Authorization") String authHeader,
            @Body ConversationRequestBody requestBody
    );
}