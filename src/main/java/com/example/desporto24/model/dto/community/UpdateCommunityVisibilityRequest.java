package com.example.desporto24.model.dto.community;

import com.example.desporto24.enumeration.CommunityVisibility;
import jakarta.validation.constraints.NotNull;

public record UpdateCommunityVisibilityRequest(
        @NotNull CommunityVisibility visibility
) {
}
