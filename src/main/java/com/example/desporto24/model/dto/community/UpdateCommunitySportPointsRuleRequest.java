package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UpdateCommunitySportPointsRuleRequest(
        @NotBlank String sport,
        @Min(0) @Max(1000) int winPoints,
        @Min(0) @Max(1000) int drawPoints,
        @Min(0) @Max(1000) int lossPoints
) {}
