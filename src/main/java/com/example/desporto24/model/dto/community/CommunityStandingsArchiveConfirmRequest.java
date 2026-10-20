package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommunityStandingsArchiveConfirmRequest(
        @NotBlank @Size(min = 2, max = 120) String title,
        @NotBlank String entryType,
        Long seasonId
) {}
