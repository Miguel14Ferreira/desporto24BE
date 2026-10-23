package com.example.desporto24.model.dto.community;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;

public record CreateCommunityRoundRequest(
        @NotBlank @Size(max = 100) String name,
        @NotNull Instant playedAt,
        @NotEmpty List<@Valid CommunityRoundEntryRequest> entries,
        @Size(max = 100) String sport,
        String matchFormat,
        List<@Valid CommunitySetScore> setScores
) {
    // Older callers/clients do not require structured racket scores.
    public CreateCommunityRoundRequest(String name, Instant playedAt,
            List<CommunityRoundEntryRequest> entries) {
        this(name, playedAt, entries, null, null, null);
    }
    public CreateCommunityRoundRequest(String name, Instant playedAt,
            List<CommunityRoundEntryRequest> entries, String sport) {
        this(name, playedAt, entries, sport, null, null);
    }
}
