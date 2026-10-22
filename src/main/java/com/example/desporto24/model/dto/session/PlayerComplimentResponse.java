package com.example.desporto24.model.dto.session;

import com.example.desporto24.enumeration.PlayerComplimentType;

import java.time.Instant;

/** Returned only to the authenticated giver or receiver, never publicly. */
public record PlayerComplimentResponse(
        Long senderProfileId,
        Long recipientProfileId,
        PlayerComplimentType type,
        Instant updatedAt) {}
