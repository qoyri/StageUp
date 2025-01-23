package com.example.stageup.ui.message;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stageup.R;
import com.example.stageup.data.model.ConversationDetailResponse;
import com.example.stageup.data.model.Message;
import com.example.stageup.data.model.MessageRequestBody;
import com.example.stageup.data.network.MessagingService;
import com.example.stageup.data.network.RetrofitClient;
import com.example.stageup.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagingActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private MessageDisplayAdapter adapter; // Utilisez le nouvel adaptateur
    private List<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MessageDisplayAdapter(messages);
        recyclerViewMessages.setAdapter(adapter);

        // Champ de texte et bouton d'envoi
        EditText editTextMessage = findViewById(R.id.editTextMessage); // Correction de l'ID
        ImageButton sendButton = findViewById(R.id.sendButton); // Utilisez ImageButton ici

        // Récupérez l'ID de la conversation depuis l'intent
        int conversationId = getIntent().getIntExtra("conversationId", -1);

        // Gérer le clic sur le bouton d'envoi
        sendButton.setOnClickListener(v -> {
            String messageContent = editTextMessage.getText().toString().trim();
            if (!messageContent.isEmpty()) {
                sendMessage(conversationId, messageContent);
                editTextMessage.setText(""); // Réinitialisez le champ après l'envoi
            } else {
                Toast.makeText(this, "Le message ne peut pas être vide", Toast.LENGTH_SHORT).show();
            }
        });

        // Charger les messages existants
        loadMessages();
    }

    private void loadMessages() {
        int conversationId = getIntent().getIntExtra("conversationId", -1);
        MessagingService service = RetrofitClient.getInstance().create(MessagingService.class);
        PreferenceManager preferenceManager = new PreferenceManager(this);
        String token = preferenceManager.getAuthToken();

        service.getConversationDetails("Bearer " + token, conversationId).enqueue(new Callback<ConversationDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<ConversationDetailResponse> call, @NonNull Response<ConversationDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    messages.clear();
                    messages.addAll(response.body().getMessages().getValues()); // Ajout des messages depuis MessageRoot
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ConversationDetailResponse> call, @NonNull Throwable t) {
                Toast.makeText(MessagingActivity.this, "Error loading messages: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(int conversationId, String messageContent) {
        MessagingService service = RetrofitClient.getInstance().create(MessagingService.class);

        PreferenceManager preferenceManager = new PreferenceManager(this);
        String token = preferenceManager.getAuthToken();

        // Créez le corps de la requête
        MessageRequestBody requestBody = new MessageRequestBody(messageContent);

        // Envoyer la requête via Retrofit
        service.sendMessage("Bearer " + token, conversationId, requestBody)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            // Mise à jour de la liste des messages
                            Toast.makeText(MessagingActivity.this, "Message envoyé avec succès", Toast.LENGTH_SHORT).show();
                            refreshMessages(conversationId); // Vous pouvez rafraîchir les messages
                        } else {
                            Toast.makeText(MessagingActivity.this, "Erreur lors de l'envoi : " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Toast.makeText(MessagingActivity.this, "Échec de l'envoi : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void refreshMessages(int conversationId) {
        loadMessages(); // Réutilisez la méthode si elle recharge les messages depuis l'API
    }
}