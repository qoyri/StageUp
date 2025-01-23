package com.example.stageup.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stageup.R;
import com.example.stageup.data.model.*;
import com.example.stageup.data.network.*;
import com.example.stageup.ui.detail.EntrepriseDetailActivity;
import com.example.stageup.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textView; // Affiche un message de rôle ou d'erreur

    private RecyclerView.Adapter<?> adapter; // Peut être EntrepriseAdapter ou EtudiantAdapter
    private List<?> items = new ArrayList<>(); // Liste générique (étudiants ou entreprises)

    private boolean isLoading = false;
    private int currentPage = 1;
    private int totalPages = 1;

    private String userRole; // Définit si l'utilisateur est "Entreprise" ou "Etudiant"

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        textView = root.findViewById(R.id.text_home);
        recyclerView = root.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Charger le rôle utilisateur et ensuite charger les données correspondantes
        loadUserRoleAndStartLoading();

        // Configurer un écouteur pour le défilement infini
        setupScrollListener();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        Toast.makeText(requireContext(), "Resume", Toast.LENGTH_SHORT).show();

        if (userRole == null || userRole.isEmpty()) {
            loadUserRoleAndStartLoading();
        } else {
            if ("Entreprise".equals(userRole)) {
                loadEtudiants();
            } else if ("Etudiant".equals(userRole)) {
                loadEntreprises();
            }
        }
    }

    private void loadUserRoleAndStartLoading() {
        String token = new PreferenceManager(requireContext()).getAuthToken();
        if (token == null || token.isEmpty()) {
            showToast("Token introuvable !");
            return;
        }

        HealthService healthService = RetrofitClient.getInstance().create(HealthService.class);
        Call<TokenResponse> call = healthService.checkToken("Bearer " + token);

        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Recherche du rôle de l'utilisateur
                    for (TokenResponse.ClaimValue claim : response.body().getClaims().getValues()) {
                        if ("role".equals(claim.getType())) {
                            userRole = claim.getValue();
                            break;
                        }
                    }

                    // Déterminer les données à charger en fonction du rôle
                    if ("Entreprise".equals(userRole)) {
                        textView.setText("Étudiants disponibles pour votre entreprise");
                        loadEtudiants(); // Charger les étudiants pour le rôle Entreprise
                    } else if ("Etudiant".equals(userRole)) {
                        textView.setText("Entreprises disponibles");
                        loadEntreprises(); // Charger les entreprises pour le rôle Étudiant
                    } else {
                        textView.setText("Données indisponibles pour ce rôle");
                    }
                } else {
                    showToast("Erreur lors de la récupération du rôle !");
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                showToast("Erreur : " + t.getMessage());
            }
        });
    }

    private void loadEntreprises() {
        if (isLoading) return;

        isLoading = true;

        EntrepriseService entrepriseService = RetrofitClient.getInstance().create(EntrepriseService.class);
        String token = new PreferenceManager(requireContext()).getAuthToken();

        Call<EntrepriseResponse> call = entrepriseService.getEntreprises("Bearer " + token, currentPage, 10);

        call.enqueue(new Callback<EntrepriseResponse>() {
            @Override
            public void onResponse(@NonNull Call<EntrepriseResponse> call, @NonNull Response<EntrepriseResponse> response) {
                isLoading = false;

                if (response.isSuccessful() && response.body() != null) {
                    List<Entreprise> newEntreprises = response.body().getEntreprises();

                    if (!newEntreprises.isEmpty()) {
                        if (adapter == null) {
                            items = newEntreprises;

                            // Ajouter un écouteur de clic ici
                            adapter = new EntrepriseAdapter((List<Entreprise>) items, entreprise -> {
                                // Redirige l'utilisateur vers l'activité de détails
                                Intent intent = new Intent(requireContext(), EntrepriseDetailActivity.class);
                                intent.putExtra("entreprise_id", entreprise.getId());
                                startActivity(intent);
                            });

                            recyclerView.setAdapter(adapter);
                        } else {
                            int startPosition = items.size();
                            ((List<Entreprise>) items).addAll(newEntreprises);
                            adapter.notifyItemRangeInserted(startPosition, newEntreprises.size());
                        }

                        totalPages = response.body().getTotalPages();
                    } else {
                        textView.setText("Aucune entreprise trouvée");
                    }
                } else {
                    showToast("Erreur lors du chargement des entreprises.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<EntrepriseResponse> call, @NonNull Throwable t) {
                isLoading = false;
                showToast("Erreur : " + t.getMessage());
            }
        });
    }

    private void loadEtudiants() {
        if (isLoading) return;

        isLoading = true;

// Réinitialiser les données avant de charger de nouveaux étudiants
        items = new ArrayList<>();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        EtudiantService etudiantService = RetrofitClient.getInstance().create(EtudiantService.class);
        String token = new PreferenceManager(requireContext()).getAuthToken();

        Call<EtudiantResponse> call = etudiantService.getEtudiants("Bearer " + token, currentPage, 10);

        call.enqueue(new Callback<EtudiantResponse>() {
            @Override
            public void onResponse(@NonNull Call<EtudiantResponse> call, @NonNull Response<EtudiantResponse> response) {
                isLoading = false;

                if (response.isSuccessful() && response.body() != null) {
                    List<Etudiant> newEtudiants = response.body().getEtudiants();

                    if (!newEtudiants.isEmpty()) {
                        if (adapter == null) {
                            items = newEtudiants;
                            adapter = new EtudiantAdapter((List<Etudiant>) items);
                            recyclerView.setAdapter(adapter);
                        } else {
                            int startPosition = items.size();
                            ((List<Etudiant>) items).addAll(newEtudiants);
                            adapter.notifyItemRangeInserted(startPosition, newEtudiants.size());
                        }

                        totalPages = response.body().getTotalPages();
                    } else {
                        textView.setText("Aucun étudiant trouvé");
                    }
                } else {
                    showToast("Erreur lors du chargement des étudiants.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<EtudiantResponse> call, @NonNull Throwable t) {
                isLoading = false;
                showToast("Erreur : " + t.getMessage());
            }
        });
    }

    private void setupScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading && layoutManager != null &&
                        layoutManager.findLastVisibleItemPosition() == items.size() - 1) {
                    if (currentPage < totalPages) {
                        currentPage++;
                        if ("Entreprise".equals(userRole)) {
                            loadEtudiants(); // Charger la page suivante d’étudiants
                        } else if ("Etudiant".equals(userRole)) {
                            loadEntreprises(); // Charger la page suivante d’entreprises
                        }
                    }
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}