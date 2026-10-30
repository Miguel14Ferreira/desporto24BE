package com.example.desporto24.model.dto.community;

import java.time.Instant;
import java.util.List;

public record CommunityHistoricalMatchArchiveResponse(
        Long id,
        String title,
        String entryType,
        String sourceFilename,
        Instant createdAt,
        List<CommunityHistoricalMatchPreviewResponse.MatchRow> rows,
        Long tournamentId
) {
    public CommunityHistoricalMatchArchiveResponse(Long id, String title, String entryType,
            String sourceFilename, Instant createdAt,
            List<CommunityHistoricalMatchPreviewResponse.MatchRow> rows) {
        this(id, title, entryType, sourceFilename, createdAt, rows, null);
    }
}
