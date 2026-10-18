package com.example.desporto24.model.dto.community;

public record HistoricalPlayerCandidateResponse(
        Long playerId,
        String historicalName,
        String linkedUsername,
        String linkStatus
) {}
