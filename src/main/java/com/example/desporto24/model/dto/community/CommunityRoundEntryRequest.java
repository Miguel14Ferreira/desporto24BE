package com.example.desporto24.model.dto.community;

import com.example.desporto24.enumeration.CommunityMatchResult;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommunityRoundEntryRequest(
        @NotBlank String username,
        String team,
        @NotNull CommunityMatchResult result,
        @Min(0) Integer goals,
        @Min(0) Integer assists,
        boolean mvp,
        boolean cleanSheet
) {
}
