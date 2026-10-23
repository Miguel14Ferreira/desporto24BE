package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCommunitySeasonRequest(
        @NotBlank @Size(max = 120) String name
) {
}
