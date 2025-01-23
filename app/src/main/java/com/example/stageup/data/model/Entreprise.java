// EntrepriseService.java
package com.example.stageup.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Entreprise {
    @SerializedName("id")
    private int id;

    @SerializedName("nom")
    private String nom;

    @SerializedName("adresse")
    private String adresse;

    @SerializedName("contact")
    private String contact;

    @SerializedName("description")
    private String description;

    @SerializedName("userId")
    private int userId;

    @SerializedName("imageData")
    private String imageData;

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getContact() {
        return contact;
    }

    public String getDescription() {
        return description;
    }
    public int getUserId(){
        return userId;
    }

    public String getImageData() {
        return imageData;
    }
}