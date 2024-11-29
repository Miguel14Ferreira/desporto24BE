package com.example.desporto24.exception;

public class SMSExpiredException extends RuntimeException{
    public SMSExpiredException(String message) {
        super(message);
    }
}
