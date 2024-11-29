package com.example.desporto24.exception;

public class AlreadyConfirmedTokenException extends RuntimeException{
    public AlreadyConfirmedTokenException(String message) {
        super(message);
    }
}
