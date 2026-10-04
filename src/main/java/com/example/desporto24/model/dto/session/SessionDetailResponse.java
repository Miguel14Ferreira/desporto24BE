package com.example.desporto24.model.dto.session;

import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.enumeration.SessionProblemType;
import com.example.desporto24.model.dto.venue.VenueResponse;

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
        String gamePhoto,
        VenueResponse venue,
        SessionAudience audience,
        String recurrenceSeriesId,
        SessionProblemType problemType
) {
    public SessionDetailResponse(
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
            String gamePhoto) {
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
                creatorUsername,
                address,
                canSeeExactAddress,
                joined,
                waitlisted,
                waitlistPosition,
                gamePhoto,
                null,
                null,
                null,
                null
        );
    }
}
