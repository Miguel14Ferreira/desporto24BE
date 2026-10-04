package com.example.desporto24.model.dto.session;

import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.enumeration.SessionAudience;

import java.math.BigDecimal;
import java.time.Instant;

public record SessionSummaryResponse(
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
        SessionAudience audience,
        boolean joinedByViewer,
        boolean waitlistedByViewer
) {
    public SessionSummaryResponse(
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
            SessionAudience audience) {
        this(
                id,
                sport,
                city,
                gameDate,
                distanceKm,
                participantCount,
                maxParticipants,
                priceAmount,
                privateGame,
                status,
                audience,
                false,
                false
        );
    }

    public SessionSummaryResponse(
            Long id,
            String sport,
            String city,
            Instant gameDate,
            Double distanceKm,
            int participantCount,
            int maxParticipants,
            BigDecimal priceAmount,
            boolean privateGame,
            SessionStatus status) {
        this(
                id,
                sport,
                city,
                gameDate,
                distanceKm,
                participantCount,
                maxParticipants,
                priceAmount,
                privateGame,
                status,
                SessionAudience.PUBLIC,
                false,
                false
        );
    }
}
