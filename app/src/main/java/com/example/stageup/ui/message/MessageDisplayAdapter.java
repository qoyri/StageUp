package com.example.stageup.ui.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stageup.R;
import com.example.stageup.data.model.Message;

import java.util.List;

public class MessageDisplayAdapter extends RecyclerView.Adapter<MessageDisplayAdapter.MessageViewHolder> {

    private final List<Message> messages;

    public MessageDisplayAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.item_message_self; // Si nécessaire, alternez entre R.layout.item_message_other ou similaire
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView); // Assurez-vous que cet ID existe
        }

        // Liez les données du message à la vue
        public void bind(Message message) {
            messageTextView.setText(message.getContenu()); // Affichez le contenu du message
        }
    }
}