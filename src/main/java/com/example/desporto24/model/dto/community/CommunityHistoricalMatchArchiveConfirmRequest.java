package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommunityHistoricalMatchArchiveConfirmRequest(
        @NotBlank @Size(min = 2, max = 120) String title,
        @NotBlank String entryType,
        Boolean createTournament,
        @Size(max = 100) String sport,
        String format
) {
    public CommunityHistoricalMatchArchiveConfirmRequest(String title, String entryType) {
        this(title, entryType, false, null, null);
    }
}
