package com.example.desporto24.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface EmailSender {
    MimeMessage send(String email, String buildEmail) throws MessagingException;
}
