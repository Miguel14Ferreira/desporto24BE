package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateCommunityRacePenaltyRequest(
        @Min(0) @Max(1000) int penaltyPoints,
        @Size(max = 255) String reason
) {}
