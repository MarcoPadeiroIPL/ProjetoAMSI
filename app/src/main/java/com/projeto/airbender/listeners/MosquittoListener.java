package com.projeto.airbender.listeners;

public interface MosquittoListener {
    void onMessage(String topic, String message);
}
