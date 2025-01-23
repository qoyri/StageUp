package com.example.stageup;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import com.example.stageup.data.model.TokenResponse;
import com.example.stageup.data.network.HealthService;
import com.example.stageup.data.network.RetrofitClient;
import com.example.stageup.ui.login.LoginActivity;
import com.example.stageup.utils.PreferenceManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stageup.databinding.ActivityMainBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Vérifie si un token d'authentification est présent
        PreferenceManager preferenceManager = new PreferenceManager(this);
        String authToken = preferenceManager.getAuthToken();
        if (authToken == null || authToken.isEmpty()) {
            // Si aucun token, redirige vers l’écran de connexion
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // Ferme l’activité actuelle
            return; // Stoppe l’exécution du onCreate
        }

        // Si un token est trouvé, continue l'initialisation de MainActivity
        checkUserRole(authToken);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_message)
                .setOpenableLayout(drawer)
                .build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void checkUserRole(String authToken) {
        HealthService apiService = RetrofitClient.getInstance().create(HealthService.class);

        Call<TokenResponse> call = apiService.checkToken("Bearer " + authToken);

        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (TokenResponse.ClaimValue claim : response.body().getClaims().getValues()) {
                        if ("role".equals(claim.getType())) {
                            userRole = claim.getValue();
                            break;
                        }
                    }

                    if (userRole != null) {
                        if ("Entreprise".equals(userRole)) {
                            // Définit le rôle dans les préférences

                            // Redirection vers HomeFragment (l'utilisateur aura les 3 barres du drawer)
                            NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                            navController.navigate(R.id.nav_home);
                        } else {
                            Toast.makeText(MainActivity.this, "Rôle : " + userRole, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        logout();
                    }
                } else {
                    logout();
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                logout();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            // Gérer la déconnexion de l'utilisateur
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Méthode de déconnexion
    private void logout() {
        // Supprime le token d'authentification
        PreferenceManager preferenceManager = new PreferenceManager(this);
        preferenceManager.clearAuthToken();

        // Redirige l’utilisateur vers l’écran de connexion
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Ferme MainActivity
    }
}