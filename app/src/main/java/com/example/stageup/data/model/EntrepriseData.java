package com.example.stageup.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EntrepriseData {
    @SerializedName("$values") // Correspond à "$values" dans l'objet JSON
    private List<Entreprise> values;

    public List<Entreprise> getValues() {
        return values != null ? values : new ArrayList<>(); // Prévenir les nulls
    }
}
