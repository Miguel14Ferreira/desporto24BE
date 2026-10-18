package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProposeHistoricalPlayerLinkRequest(
        @NotNull Long playerId,
        @NotBlank String targetUsername
) {}
