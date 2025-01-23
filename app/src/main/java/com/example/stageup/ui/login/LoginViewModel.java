package com.example.stageup.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stageup.repository.LoginRepository;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        new Thread(() -> {
            try {
                // Appelle LoginRepository pour l'authentification
                String token = LoginRepository.login(username, password);
                loginResult.postValue(new LoginResult(token, null)); // Succès
            } catch (Exception e) {
                loginResult.postValue(new LoginResult(null, e.getMessage())); // En cas d'échec
            }
        }).start();
    }

    public static class LoginResult {
        private final String token;
        private final String error;

        public LoginResult(String token, String error) {
            this.token = token;
            this.error = error;
        }

        public String getToken() {
            return token;
        }

        public String getError() {
            return error;
        }

        public boolean isSuccess() {
            return token != null;
        }
    }
}