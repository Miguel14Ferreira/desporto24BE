package com.example.desporto24.exception;

public class CommunityInvitePendingException extends RuntimeException {

    public CommunityInvitePendingException() {
        super("Já existe um convite pendente para este utilizador. Aguarda a aceitação.");
    }
}
