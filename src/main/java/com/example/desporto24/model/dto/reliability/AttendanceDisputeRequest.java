package com.example.desporto24.model.dto.reliability;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AttendanceDisputeRequest(
        @NotNull Long participantId,
        @NotBlank String reason
) {
}
