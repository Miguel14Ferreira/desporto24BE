package com.example.desporto24.model.dto.community;

import java.time.Instant;

public record CommunityHistoricalMatchArchiveSummaryResponse(
        Long id,
        String title,
        String entryType,
        String sourceFilename,
        Instant createdAt
) {}
