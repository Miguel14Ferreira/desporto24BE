package com.example.desporto24.model.dto.community;

import java.time.Instant;
import java.util.List;

public record CommunityRoundResponse(
        Long id,
        String name,
        Instant playedAt,
        List<CommunityRoundEntryResponse> entries
) {
}
