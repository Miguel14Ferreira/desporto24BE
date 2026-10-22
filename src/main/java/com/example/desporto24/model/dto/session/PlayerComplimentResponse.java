package com.example.desporto24.model.dto.session;

import com.example.desporto24.enumeration.PlayerComplimentType;

import java.time.Instant;

/** Shown only to the authenticated giver or receiver; no public player rankings. */
public record PlayerComplimentResponse(
        String senderUsername,
        String recipientUsername,
        PlayerComplimentType type,
        Instant updatedAt) {}
