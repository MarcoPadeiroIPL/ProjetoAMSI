package com.projeto.airbender.listeners;

public interface LoginListener {
    void onAttemptLogin(String token, int id, String role);
}
