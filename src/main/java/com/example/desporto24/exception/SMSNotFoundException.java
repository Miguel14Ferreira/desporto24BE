package com.example.desporto24.exception;

public class SMSNotFoundException extends RuntimeException{
    public SMSNotFoundException(String message) {
        super(message);
    }
}
