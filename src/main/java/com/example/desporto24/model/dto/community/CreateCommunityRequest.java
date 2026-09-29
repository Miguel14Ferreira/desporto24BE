package com.example.desporto24.model.dto.community;

import com.example.desporto24.enumeration.CommunityVisibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCommunityRequest(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 2000) String description,
        @Size(max = 100) String sport,
        @NotNull CommunityVisibility visibility
) {
}
