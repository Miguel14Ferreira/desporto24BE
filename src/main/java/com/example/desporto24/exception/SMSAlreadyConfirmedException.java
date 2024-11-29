package com.example.desporto24.exception;

public class SMSAlreadyConfirmedException extends RuntimeException{
    public SMSAlreadyConfirmedException(String message) {
        super(message);
    }
}
