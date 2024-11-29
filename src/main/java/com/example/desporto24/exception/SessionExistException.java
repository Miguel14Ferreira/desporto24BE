package com.example.desporto24.exception;

public class SessionExistException extends RuntimeException{
    public SessionExistException(String message) {
        super(message);
    }
}
