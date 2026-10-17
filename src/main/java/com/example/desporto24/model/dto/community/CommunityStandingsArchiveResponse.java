package com.example.desporto24.model.dto.community;

import java.time.Instant;
import java.util.List;

public record CommunityStandingsArchiveResponse(
        Long id,
        String title,
        String entryType,
        Long seasonId,
        String sourceFilename,
        Instant createdAt,
        List<CommunityStandingsArchivePreviewResponse.StandingRow> rows
) {}
