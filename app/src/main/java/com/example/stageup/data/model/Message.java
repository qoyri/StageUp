package com.example.stageup.data.model;

public class Message {
    private int envoyeurId;
    private String contenu;
    private String dateEnvoi;
    private boolean isSelf;

    public int getEnvoyeurId() {
        return envoyeurId;
    }

    public void setEnvoyeurId(int envoyeurId) {
        this.envoyeurId = envoyeurId;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }
}