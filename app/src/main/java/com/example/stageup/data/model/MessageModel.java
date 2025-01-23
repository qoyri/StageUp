package com.example.stageup.data.model;

public class MessageModel {
    private final int id;
    private final String title;
    private final String lastMessage;

    public MessageModel(int id, String title, String lastMessage) {
        this.id = id;
        this.title = title;
        this.lastMessage = lastMessage;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLastMessage() {
        return lastMessage;
    }
}