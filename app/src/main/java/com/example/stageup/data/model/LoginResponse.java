package com.example.stageup.data.model;

public class LoginResponse {
    private String token;
    private String username;
    private String role;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}