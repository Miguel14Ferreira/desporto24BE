package com.example.desporto24.exception;

public class PhoneNotFoundException extends RuntimeException{
    public PhoneNotFoundException(String message) {
        super(message);
    }
}
