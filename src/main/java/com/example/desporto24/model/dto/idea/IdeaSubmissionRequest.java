package com.example.desporto24.model.dto.idea;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IdeaSubmissionRequest(
        @NotBlank @Size(max = 120) String name,
        @Size(max = 120) String city,
        @NotBlank @Email @Size(max = 255) String email,
        @Size(max = 32) String gender,
        @NotBlank @Size(max = 160) String subject,
        @NotBlank @Size(max = 4000) String problem
) {
}
