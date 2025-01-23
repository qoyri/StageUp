package com.example.stageup.data.model;

import com.google.gson.annotations.SerializedName;

public class Etudiant {

    @SerializedName("id")
    private int id;

    @SerializedName("nom")
    private String nom;

    @SerializedName("prenom")
    private String prenom;

    @SerializedName("contact")
    private String contact;

    @SerializedName("promo")
    private String promo;

    @SerializedName("reseauxSociaux")
    private String reseauxSociaux;

    @SerializedName("imageData")
    private String imageData;

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getContact() {
        return contact;
    }

    public String getPromo() {
        return promo;
    }

    public String getReseauxSociaux() {
        return reseauxSociaux;
    }

    public String getImageData() {
        return imageData;
    }
}