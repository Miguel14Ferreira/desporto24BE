package com.example.desporto24.model.dto.venue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateVenueSportConditionRequest(
        @NotBlank @Size(max = 80) String sport,
        @NotBlank @Size(max = 1500) String conditions
) {
}
