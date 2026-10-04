package com.example.desporto24.model.dto.moderation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserReportRequest(
        @NotBlank
        @Size(max = 1000)
        String reason
) {
}
