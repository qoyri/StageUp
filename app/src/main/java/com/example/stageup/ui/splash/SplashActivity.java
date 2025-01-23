package com.example.stageup.ui.splash;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stageup.MainActivity;
import com.example.stageup.R;
import com.example.stageup.data.model.PingResponse;
import com.example.stageup.data.model.TokenResponse;
import com.example.stageup.data.network.HealthService;
import com.example.stageup.data.network.RetrofitClient;
import com.example.stageup.ui.login.LoginActivity;
import com.example.stageup.utils.PreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Trouve le logo et applique l'animation
        ImageView appLogo = findViewById(R.id.app_logo);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        appLogo.startAnimation(fadeIn);

        // Init préférences
        preferenceManager = new PreferenceManager(this);

        // Vérifie d'abord si le serveur est en ligne
        checkServerStatus();
    }

    private void checkServerStatus() {
        HealthService apiService = RetrofitClient.getInstance().create(HealthService.class);

        apiService.pingServer().enqueue(new Callback<PingResponse>() {
            @Override
            public void onResponse(Call<PingResponse> call, Response<PingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Le serveur est en ligne, vérifie le token
                    checkAuthToken();
                } else {
                    showErrorAndExit("Le serveur est indisponible. Veuillez réessayer plus tard.");
                }
            }

            @Override
            public void onFailure(Call<PingResponse> call, Throwable t) {
                showErrorAndExit("Impossible de rejoindre le serveur. Vérifiez votre connexion internet.");
            }
        });
    }

    private void checkAuthToken() {
        String authToken = preferenceManager.getAuthToken();

        if (authToken == null || authToken.isEmpty()) {
            navigateToLogin();
            return;
        }

        HealthService apiService = RetrofitClient.getInstance().create(HealthService.class);
        String authorizationHeader = "Bearer " + authToken;

        apiService.checkToken(authorizationHeader).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful()) {
                    TokenResponse tokenResponse = response.body();

                    if (tokenResponse != null) {
                        // Si le token est valide
                        if ("Token is valid.".equalsIgnoreCase(tokenResponse.getMessage())) {
                            navigateToMain();
                        } else {
                            handleInvalidToken();
                        }
                    }
                } else {
                    handleInvalidToken();
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                t.printStackTrace();
                handleInvalidToken();
            }
        });
    }

    private void copyToClipboard(String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
        System.out.println("Copied to clipboard: " + text); // Pour te permettre de vérifier dans les logs.
    }

    private void handleInvalidToken() {
        preferenceManager.clearAuthToken();
        navigateToLogin();
    }

    private void navigateToMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showErrorAndExit(String message) {
        // Affiche un message d'erreur
        Toast.makeText(SplashActivity.this, message, Toast.LENGTH_LONG).show();

        // Ferme l'application après une courte pause
        new android.os.Handler().postDelayed(this::finish, 3000);
    }
}