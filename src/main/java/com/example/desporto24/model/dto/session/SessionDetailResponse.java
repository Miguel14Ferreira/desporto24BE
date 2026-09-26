package com.example.desporto24.model.dto.session;

import com.example.desporto24.enumeration.SessionStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record SessionDetailResponse(
        Long id,
        String sport,
        String city,
        Instant gameDate,
        Double distanceKm,
        int participantCount,
        int maxParticipants,
        BigDecimal priceAmount,
        boolean privateGame,
        SessionStatus status,
        String creatorUsername,
        String address,
        boolean canSeeExactAddress,
        boolean joined,
        boolean waitlisted,
        Integer waitlistPosition,
        String gamePhoto
) {
}
