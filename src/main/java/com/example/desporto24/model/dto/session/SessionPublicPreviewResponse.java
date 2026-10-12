package com.example.desporto24.model.dto.session;

import com.example.desporto24.enumeration.SessionStatus;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Information safe to show to guests without a Jynlo account.
 * Excludes exact addresses, participant identities and community data.
 */
public record SessionPublicPreviewResponse(
        Long id,
        String sport,
        String city,
        Instant gameDate,
        Integer maxParticipants,
        BigDecimal priceAmount,
        SessionStatus status,
        String gamePhoto
) {
}
