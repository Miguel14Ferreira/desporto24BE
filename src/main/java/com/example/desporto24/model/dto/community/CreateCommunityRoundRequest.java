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
        @Size(max = 100) String sport
) {
    // Existing Java callers remain source compatible.
    public CreateCommunityRoundRequest(String name, Instant playedAt,
            List<CommunityRoundEntryRequest> entries) {
        this(name, playedAt, entries, null);
    }
}
