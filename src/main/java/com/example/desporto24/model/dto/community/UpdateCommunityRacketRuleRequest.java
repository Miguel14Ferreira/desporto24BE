package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateCommunityRacketRuleRequest(
        @NotBlank @Size(max = 100) String sport,
        @NotBlank @Pattern(regexp = "SINGLES|DOUBLES") String matchFormat,
        @Min(0) @Max(100) int winPoints,
        @Min(0) @Max(100) int lossPoints,
        @NotBlank @Pattern(regexp = "SETS_FIRST|SCORE_FIRST") String tieBreak
) {}
