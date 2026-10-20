package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RecordCommunityTournamentMatchResultRequest(
        @NotNull @Min(0) @Max(999) Integer homeScore,
        @NotNull @Min(0) @Max(999) Integer awayScore
) {
}
