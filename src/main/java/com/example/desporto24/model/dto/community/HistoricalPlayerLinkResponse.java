package com.example.desporto24.model.dto.community;

public record HistoricalPlayerLinkResponse(
        Long id,
        Long playerId,
        String historicalName,
        String targetUsername,
        String status
) {}
