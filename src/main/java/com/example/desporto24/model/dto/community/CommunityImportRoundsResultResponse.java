package com.example.desporto24.model.dto.community;

public record CommunityImportRoundsResultResponse(
        int createdRounds,
        int skippedDuplicateRounds,
        int createdEntries
) {}
