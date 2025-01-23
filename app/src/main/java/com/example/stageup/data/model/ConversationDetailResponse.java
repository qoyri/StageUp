package com.example.stageup.data.model;

import java.util.List;

public class ConversationDetailResponse {
    private int conversationId;
    private MessageRoot messages;

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public MessageRoot getMessages() {
        return messages;
    }

    public void setMessages(MessageRoot messages) {
        this.messages = messages;
    }

    public static class MessageRoot {
        // Utiliser directement la classe Message du bon package
        private List<com.example.stageup.data.model.Message> $values;

        public List<com.example.stageup.data.model.Message> getValues() {
            return $values;
        }

        public void setValues(List<com.example.stageup.data.model.Message> values) {
            this.$values = values;
        }
    }
}