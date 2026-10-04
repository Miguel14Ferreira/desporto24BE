package com.example.desporto24.model.dto.analytics;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PilotAccessEventRequest(
        @NotBlank
        @Size(max = 128)
        String deviceId,

        @Size(max = 64)
        String cohort
) {
}
