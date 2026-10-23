package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommunityImportContextRequest(
        @NotNull Long seasonId,
        @Min(1900) @Max(2100) Integer defaultYear,
        @Size(max = 60) String sourceCompetition,
        @Size(max = 40) String sourceRound,
        @Size(max = 100) String sport
) {
    // Keep Java callers and old JSON payloads compatible.
    public CommunityImportContextRequest(Long seasonId, Integer defaultYear) {
        this(seasonId, defaultYear, null, null, null);
    }

    public CommunityImportContextRequest(Long seasonId, Integer defaultYear,
            String sourceCompetition, String sourceRound) {
        this(seasonId, defaultYear, sourceCompetition, sourceRound, null);
    }
}
