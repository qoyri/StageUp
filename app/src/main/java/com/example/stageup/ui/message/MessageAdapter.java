package com.example.stageup.ui.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stageup.R;
import com.example.stageup.data.model.Conversation;
import com.example.stageup.data.model.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final List<Conversation> conversations;
    private final OnConversationClickListener listener;

    public MessageAdapter(List<Conversation> conversations, OnConversationClickListener listener) {
        this.conversations = conversations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bind(conversations.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    // ViewHolder pour une conversation
    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName; // Changer ici pour un ID existant

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName); // Modifier ici pour correspondre à l'ID XML existant
        }

        public void bind(Conversation conversation, OnConversationClickListener listener) {
            textViewName.setText(conversation.getInterlocuteur().getName()); // Exemple d'utilisation
            itemView.setOnClickListener(v -> listener.onConversationClick(conversation));
        }
    }

    public interface OnConversationClickListener {
        void onConversationClick(Conversation conversation);
    }
}