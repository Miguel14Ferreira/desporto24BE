package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCommunityRaceRuleRequest(
        @NotBlank @Size(max = 100) String sport,
        @NotBlank @Size(max = 160) String pointsByPlace
) {}
