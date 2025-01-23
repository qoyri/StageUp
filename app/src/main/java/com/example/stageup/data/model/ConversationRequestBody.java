package com.example.stageup.data.model;

public class ConversationRequestBody {
    private int participant2Id; // Ne garde que ce champ

    public ConversationRequestBody(int participant2Id) {
        this.participant2Id = participant2Id;
    }

    public int getParticipant2Id() {
        return participant2Id;
    }

    public void setParticipant2Id(int participant2Id) {
        this.participant2Id = participant2Id;
    }
}