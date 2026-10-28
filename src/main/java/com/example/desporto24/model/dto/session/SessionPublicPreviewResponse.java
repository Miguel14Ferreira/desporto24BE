package com.example.desporto24.model.dto.session;

import com.example.desporto24.enumeration.SessionStatus;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Information safe to show to guests without a Jynlo account.
 * Public previews exclude exact addresses, participant identities and community data.
 * Signed organizer invitations can include a venue and exact meeting location.
 */
public record SessionPublicPreviewResponse(
        Long id,
        String sport,
        String city,
        Instant gameDate,
        Integer maxParticipants,
        Integer participantCount,
        BigDecimal priceAmount,
        SessionStatus status,
        String gamePhoto,
        String venueName,
        String address,
        Double latitude,
        Double longitude
) {
    /** Legacy/public endpoint intentionally withholds exact location. */
    public SessionPublicPreviewResponse(Long id, String sport, String city, Instant gameDate,
                                        Integer maxParticipants, Integer participantCount,
                                        BigDecimal priceAmount, SessionStatus status, String gamePhoto) {
        this(id, sport, city, gameDate, maxParticipants, participantCount, priceAmount,
                status, gamePhoto, null, null, null, null);
    }
}
