package com.example.notification_passport_service.service;

public interface Sender<T> {
    void sendMessage(T info);
}
