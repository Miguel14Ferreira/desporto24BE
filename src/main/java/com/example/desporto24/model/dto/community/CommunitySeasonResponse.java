package com.example.desporto24.model.dto.community;

import java.time.Instant;

public record CommunitySeasonResponse(
        Long id,
        String name,
        Instant startsAt,
        Instant endedAt,
        boolean active,
        String championUsername,
        String championDisplayName,
        Integer championPoints
) {
}
