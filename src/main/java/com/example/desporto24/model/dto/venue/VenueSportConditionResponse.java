package com.example.desporto24.model.dto.venue;

import java.time.Instant;

public record VenueSportConditionResponse(
        Long id,
        String sport,
        String conditions,
        String informationSource,
        String createdByUsername,
        Instant createdAt
) {
}
