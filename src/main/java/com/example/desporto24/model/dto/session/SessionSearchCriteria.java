package com.example.desporto24.model.dto.session;

import java.time.Instant;

public record SessionSearchCriteria(
        String sport,
        String city,
        Instant from,
        Instant to,
        Double latitude,
        Double longitude,
        Double radiusKm,
        String availability,
        Boolean privateGame,
        Boolean freeOnly,
        String sort
) {
}
