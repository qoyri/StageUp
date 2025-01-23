package com.example.stageup.ui.detail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.stageup.R;
import com.example.stageup.data.model.*;
import com.example.stageup.data.network.EntrepriseService;
import com.example.stageup.data.network.MessagingService;
import com.example.stageup.data.network.RetrofitClient;
import com.example.stageup.data.network.StageService;
import com.example.stageup.ui.message.MessagingActivity;
import com.example.stageup.utils.PreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class EntrepriseDetailActivity extends AppCompatActivity {

    private int entrepriseId; // Stocker l'ID de l'entreprise ici (plus défini par le réseau)
    private Button btnContact;
    private ImageView imageView;
    private TextView stageTextView;
    private RecyclerView stageRecyclerView;
    private TextView nomTextView, adresseTextView, contactTextView, descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entreprise_detail);

        imageView = findViewById(R.id.imageView);
        nomTextView = findViewById(R.id.nomTextView);
        adresseTextView = findViewById(R.id.adresseTextView);
        contactTextView = findViewById(R.id.contactTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        btnContact = findViewById(R.id.btnContact);

        // Récupération de l'ID entreprise depuis Intent pour appeler le réseau
        int intentEntrepriseId = getIntent().getIntExtra("entreprise_id", -1);

        if (intentEntrepriseId == -1) {
            Toast.makeText(this, "Erreur : Entreprise introuvable.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Désactiver temporairement le bouton tant que les détails ne sont pas chargés
        btnContact.setOnClickListener(v -> {
            if (entrepriseId > 0) {
                Log.d("ButtonClick", "Clic sur 'Contact', ID utilisé : " + entrepriseId);
                createConversation(entrepriseId);
            } else {
                Toast.makeText(this, "Erreur : ID utilisateur introuvable.", Toast.LENGTH_SHORT).show();
            }
        });

        // Charger les détails depuis l'API en récupérant l'ID d'entreprise
        loadEntrepriseDetails(intentEntrepriseId);
    }

    private void createConversation(int participant2Id) {
        MessagingService messagingService = RetrofitClient.getInstance().create(MessagingService.class);
        String token = new PreferenceManager(this).getAuthToken();

        // Crée l'objet de la requête avec seulement participant2Id
        ConversationRequestBody requestBody = new ConversationRequestBody(participant2Id);
        Log.d("CreateConversation", "Token: " + token);
        Log.d("CreateConversation", "Participant2Id: " + participant2Id); // Ajout d'un log pour vérifier

        messagingService.createConversation("Bearer " + token, requestBody).enqueue(new Callback<ConversationResponse>() {
            @Override
            public void onResponse(Call<ConversationResponse> call, Response<ConversationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int conversationId = response.body().getConversationId();
                    Log.d("CreateConversation", "Conversation créée avec ID : " + conversationId);

                    Intent intent = new Intent(EntrepriseDetailActivity.this, MessagingActivity.class);
                    intent.putExtra("conversationId", conversationId);
                    startActivity(intent);
                } else {
                    Log.e("CreateConversation", "Erreur lors de la création : " + response.code()); // Afficher le code HTTP
                    Toast.makeText(EntrepriseDetailActivity.this, "Erreur lors de la création.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ConversationResponse> call, Throwable t) {
                Log.e("CreateConversation", "Erreur réseau : " + t.getMessage()); // Trace complète de l'erreur
                Toast.makeText(EntrepriseDetailActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadEntrepriseDetails(int id) {
        String token = new PreferenceManager(this).getAuthToken();
        EntrepriseService entrepriseService = RetrofitClient.getInstance().create(EntrepriseService.class);

        entrepriseService.getEntrepriseById("Bearer " + token, id).enqueue(new Callback<Entreprise>() {
            @Override
            public void onResponse(Call<Entreprise> call, Response<Entreprise> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Afficher les détails
                    Entreprise entreprise = response.body();
                    displayEntrepriseDetails(entreprise);
                } else {
                    Toast.makeText(EntrepriseDetailActivity.this, "Erreur lors du chargement des détails", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Entreprise> call, Throwable t) {
                Toast.makeText(EntrepriseDetailActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayEntrepriseDetails(Entreprise entreprise) {
        nomTextView.setText(entreprise.getNom());
        adresseTextView.setText(entreprise.getAdresse());
        contactTextView.setText(entreprise.getContact());
        descriptionTextView.setText(entreprise.getDescription());

        // Récupérer et stocker l'ID depuis l'API
        entrepriseId = entreprise.getUserId(); // Utiliser entreprise.getUserId() ou entreprise.getId()
        Log.d("EntrepriseDetail", "Entreprise User ID = " + entrepriseId);

        if (entrepriseId > 0) {
            Toast.makeText(this, "Entreprise User ID récupéré : " + entrepriseId, Toast.LENGTH_LONG).show();
            btnContact.setEnabled(true); // Activer le bouton maintenant que l'ID est valide
        } else {
            Toast.makeText(this, "Erreur : ID non récupéré pour cette entreprise.", Toast.LENGTH_LONG).show();
            btnContact.setEnabled(false); // Désactiver en cas d'erreur
        }

        // Charger les stages associés
        loadStagesByEntreprise(entreprise.getId());

        // Décoder l'image Base64 et l'afficher dans l'ImageView
        String imageData = entreprise.getImageData();
        if (imageData != null && !imageData.isEmpty()) {
            Bitmap decodedImage = decodeBase64Image(imageData);
            if (decodedImage != null) {
                imageView.setImageBitmap(decodedImage);
            } else {
                imageView.setImageDrawable(new ColorDrawable(Color.RED)); // Image d'erreur
            }
        } else {
            imageView.setImageDrawable(new ColorDrawable(Color.LTGRAY)); // Placeholder
        }
    }

    private Bitmap decodeBase64Image(String base64String) {
        try {
            // Décoder la chaîne Base64 en tableau d'octets
            byte[] decodedBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT);
            // Convertir les octets décodés en Bitmap
            return android.graphics.BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            e.printStackTrace(); // Gérer les erreurs si la chaîne n'est pas en Base64 valide
            return null;
        }
    }

    private void loadStagesByEntreprise(int entrepriseId) {
        String token = new PreferenceManager(this).getAuthToken();
        StageService stageService = RetrofitClient.getInstance().create(StageService.class);

        stageService.getStagesByEntreprise("Bearer " + token, entrepriseId)
                .enqueue(new Callback<StageResponse<Stage>>() {
                    @Override
                    public void onResponse(Call<StageResponse<Stage>> call, Response<StageResponse<Stage>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Récupérez les données sous $values
                            List<Stage> stages = response.body().get$values();
                            if (stages != null && !stages.isEmpty()) {
                                displayStages(stages);
                            } else {
                                Toast.makeText(EntrepriseDetailActivity.this, "Aucun stage trouvé.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(EntrepriseDetailActivity.this, "Erreur : " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<StageResponse<Stage>> call, Throwable t) {
                        Toast.makeText(EntrepriseDetailActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayStages(List<Stage> stages) {
        stageTextView = findViewById(R.id.stageTextView);
        stageRecyclerView = findViewById(R.id.stageRecyclerView);

        if (stageTextView != null) {
            stageTextView.setVisibility(View.VISIBLE);
        } else {
            Log.e("DEBUG", "Stage TextView is null");
        }

        if (stageRecyclerView != null) {
            stageRecyclerView.setVisibility(View.VISIBLE);

            StageAdapter stageAdapter = new StageAdapter(stages);
            stageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            stageRecyclerView.setAdapter(stageAdapter);
        } else {
            Log.e("DEBUG", "Stage RecyclerView is null");
        }
    }
}