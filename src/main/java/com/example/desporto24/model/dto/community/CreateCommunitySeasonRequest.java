package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record CreateCommunitySeasonRequest(
        @NotBlank @Size(max = 120) String name,
        Instant startsAt
) {
}
