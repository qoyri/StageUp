package com.example.stageup.data.network;

import com.example.stageup.data.model.LoginRequest;
import com.example.stageup.data.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/api/v1/Auth/login")
    @Headers("Content-Type: application/json")
    Call<LoginResponse> login(@Body LoginRequest request);
}