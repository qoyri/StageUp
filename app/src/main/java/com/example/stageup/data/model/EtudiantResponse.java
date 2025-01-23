package com.example.stageup.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EtudiantResponse {

    @SerializedName("totalItems")
    private int totalItems;

    @SerializedName("page")
    private int page;

    @SerializedName("pageSize")
    private int pageSize;

    @SerializedName("totalPages")
    private int totalPages;

    @SerializedName("data")
    private EtudiantData data; // Contient les données des étudiants

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

    public List<Etudiant> getEtudiants() {
        if (data != null && data.getValues() != null) {
            return data.getValues();
        }
        return new ArrayList<>(); // Retourne une liste vide s'il n'y a aucune donnée
    }

    static class EtudiantData {
        @SerializedName("$values") // Correspond à "$values" dans le JSON
        private List<Etudiant> values;

        public List<Etudiant> getValues() {
            return values != null ? values : new ArrayList<>(); // Gère le cas où values est null
        }
    }
}