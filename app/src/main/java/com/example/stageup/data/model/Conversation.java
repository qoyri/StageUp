package com.example.stageup.data.model;

public class Conversation {
    private int conversationId;
    private Interlocuteur interlocuteur;

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public Interlocuteur getInterlocuteur() {
        return interlocuteur;
    }

    public void setInterlocuteur(Interlocuteur interlocuteur) {
        this.interlocuteur = interlocuteur;
    }
}