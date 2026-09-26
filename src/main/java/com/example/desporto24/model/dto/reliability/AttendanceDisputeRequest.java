package com.example.desporto24.model.dto.reliability;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AttendanceDisputeRequest(
        @NotNull Long participantId,
        @NotBlank @Size(max = 2000) String reason
) {
}
