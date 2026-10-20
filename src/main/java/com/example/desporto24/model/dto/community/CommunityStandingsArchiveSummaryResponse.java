package com.example.desporto24.model.dto.community;

import java.time.Instant;

public record CommunityStandingsArchiveSummaryResponse(
        Long id,
        String title,
        String entryType,
        Long seasonId,
        String sourceFilename,
        Instant createdAt
) {}
