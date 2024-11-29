package com.example.desporto24.exception;

public class EqualUsernameAndPasswordException extends RuntimeException{
    public EqualUsernameAndPasswordException(String message) {
        super(message);
    }
}
