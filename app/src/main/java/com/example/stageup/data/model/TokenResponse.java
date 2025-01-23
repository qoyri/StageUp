package com.example.stageup.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TokenResponse {
    private String message;
    private Claims claims;

    // Getters et Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Claims getClaims() {
        return claims;
    }

    public void setClaims(Claims claims) {
        this.claims = claims;
    }

    public static class Claims {
        @SerializedName("$values")
        private List<ClaimValue> values;

        // Getter
        public List<ClaimValue> getValues() {
            return values;
        }

        // Setter
        public void setValues(List<ClaimValue> values) {
            this.values = values;
        }
    }

    public static class ClaimValue {
        private String type;
        private String value;

        // Getters et Setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}