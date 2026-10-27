package com.example.desporto24.model.dto.chat;

/** Minimal user-visible identity of a reaction actor, only for members of the chat. */
public record ChatReactionParticipant(
        String username, String displayName, String profilePicUrl,
        String emoji, boolean mine) {}
