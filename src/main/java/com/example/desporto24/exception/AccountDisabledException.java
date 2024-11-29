package com.example.desporto24.exception;

public class AccountDisabledException extends RuntimeException{
    public AccountDisabledException(String message) {
        super(message);
    }
}
