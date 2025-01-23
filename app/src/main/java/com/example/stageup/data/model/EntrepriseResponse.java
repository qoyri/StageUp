package com.example.stageup.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EntrepriseResponse {
    @SerializedName("totalItems")
    private int totalItems;

    @SerializedName("page")
    private int page;

    @SerializedName("pageSize")
    private int pageSize;

    @SerializedName("totalPages")
    private int totalPages;

    @SerializedName("data")
    private EntrepriseData data; // Contient les données réelles (objets intermédiaires)

    public int getTotalItems() {
        return totalItems;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<Entreprise> getEntreprises() {
        if (data != null && data.getValues() != null) {
            return data.getValues(); // Retourne la liste des entreprises dans "$values"
        }
        return new ArrayList<>(); // Retourne une liste vide si aucune donnée
    }
}
