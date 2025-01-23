package com.example.stageup.data.model;

public class MessageSendResponse {
    private String message;
    private SentMessage data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SentMessage getData() {
        return data;
    }

    public void setData(SentMessage data) {
        this.data = data;
    }

    public static class SentMessage {
        private int conversationId;
        private int envoyeurId;
        private String contenu;
        private String dateEnvoi;

        public int getConversationId() {
            return conversationId;
        }

        public void setConversationId(int conversationId) {
            this.conversationId = conversationId;
        }

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
    }
}