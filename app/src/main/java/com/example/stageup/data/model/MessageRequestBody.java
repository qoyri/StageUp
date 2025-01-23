package com.example.stageup.data.model;

public class MessageRequestBody {
    private String contenu;

    public MessageRequestBody(String contenu) {
        this.contenu = contenu;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
}