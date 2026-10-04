package com.example.desporto24.email;

public interface EmailSender {
    void send(String email, String buildEmail);

    default void send(String email, String subject, String html) {
        send(email, html);
    }
}
