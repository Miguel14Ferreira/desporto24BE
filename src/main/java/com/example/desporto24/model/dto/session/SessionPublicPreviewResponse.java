package com.example.desporto24.model.dto.session;

import com.example.desporto24.enumeration.SessionStatus;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Information safe to show to guests without a Jynlo account.
 * Normal public preview excludes the exact location, identities and community data.
 * Only the validated 256-bit invitation preview may include its meeting address.
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
        String address
) {
    // Keep the unauthenticated public preview safe and existing constructors compatible.
    public SessionPublicPreviewResponse(Long id, String sport, String city, Instant gameDate,
                                        Integer maxParticipants, Integer participantCount,
                                        BigDecimal priceAmount, SessionStatus status, String gamePhoto) {
        this(id,sport,city,gameDate,maxParticipants,participantCount,priceAmount,
                status,gamePhoto,null,null);
    }
}
