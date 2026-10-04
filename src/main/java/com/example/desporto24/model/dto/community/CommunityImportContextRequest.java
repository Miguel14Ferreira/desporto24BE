package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CommunityImportContextRequest(
        @NotNull Long seasonId,
        @Min(1900) @Max(2100) Integer defaultYear
) {}
