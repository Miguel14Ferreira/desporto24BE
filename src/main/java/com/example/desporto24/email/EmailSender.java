package com.example.desporto24.email;

import javax.mail.Message;
import javax.mail.MessagingException;

public interface EmailSender {
    Message send(String to, String email) throws MessagingException;
}
