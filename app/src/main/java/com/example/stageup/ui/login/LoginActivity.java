package com.example.stageup.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.stageup.MainActivity;
import com.example.stageup.R;
import com.example.stageup.utils.PreferenceManager;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialiser le PreferenceManager
        preferenceManager = new PreferenceManager(this);

        // Initialiser ViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Lier les éléments du layout
        EditText etUsername = findViewById(R.id.et_username);
        EditText etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        TextView tvError = findViewById(R.id.tv_error);

        // Action du bouton de connexion
        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Valide les entrées utilisateur
            if (username.isEmpty() || password.isEmpty()) {
                tvError.setText("Both fields are required");
                tvError.setVisibility(View.VISIBLE);
                return;
            }

            // Appelle la méthode de connexion du ViewModel
            tvError.setVisibility(View.GONE); // Masque les erreurs précédentes
            loginViewModel.login(username, password);
        });

        // Observe le résultat de la connexion
        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult != null) {
                if (loginResult.isSuccess()) {
                    // Connexion réussie : Enregistrer le token
                    String token = loginResult.getToken();
                    preferenceManager.saveAuthToken(token);

                    // Navigation vers MainActivity
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Empêche de revenir à la page de login
                } else {
                    // Affiche une erreur
                    tvError.setText(loginResult.getError());
                    tvError.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}