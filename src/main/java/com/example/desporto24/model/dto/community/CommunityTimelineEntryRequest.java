package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommunityTimelineEntryRequest(
        @NotBlank @Size(max = 2000) String body) {
}
