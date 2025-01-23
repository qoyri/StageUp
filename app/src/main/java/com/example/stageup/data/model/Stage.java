package com.example.stageup.data.model;

import com.google.gson.annotations.SerializedName;

public class Stage {
    @SerializedName("id")
    private int id;
    @SerializedName("titre")
    private String titre;
    @SerializedName("description")
    private String description;
    @SerializedName("entrepriseId")
    private int entrepriseId;
    @SerializedName("entrepriseNom")
    private String entrepriseNom;
    @SerializedName("lieu")
    private String lieu;
    @SerializedName("duree")
    private String duree;
    @SerializedName("typeContrat")
    private String typeContrat;
    @SerializedName("dateDebut")
    private String dateDebut;
    @SerializedName("dateFin")
    private String dateFin;
    @SerializedName("statut")
    private String statut;

    // Getters et setters
    public int getId() { return id; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public int getEntrepriseId() { return entrepriseId; }
    public String getEntrepriseNom() { return entrepriseNom; }
    public String getLieu() { return lieu; }
    public String getDuree() { return duree; }
    public String getTypeContrat() { return typeContrat; }
    public String getDateDebut() { return dateDebut; }
    public String getDateFin() { return dateFin; }
    public String getStatut() { return statut; }
}