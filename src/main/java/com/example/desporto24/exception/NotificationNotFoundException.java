package com.example.desporto24.exception;

import org.jetbrains.annotations.NotNull;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(String message) {
        super(message);
    }
}
