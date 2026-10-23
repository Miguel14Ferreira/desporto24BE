package com.example.desporto24.model.dto.community;

import java.time.Instant;
import java.util.List;

public record CommunityRoundResponse(
        Long id,
        String name,
        Instant playedAt,
        List<CommunityRoundEntryResponse> entries,
        String sport,
        String matchFormat,
        List<CommunitySetScore> setScores,
        String eventType
) {
    public CommunityRoundResponse(Long id, String name, Instant playedAt,
            List<CommunityRoundEntryResponse> entries) {
        this(id, name, playedAt, entries, null, null, List.of(), null);
    }
    public CommunityRoundResponse(Long id, String name, Instant playedAt,
            List<CommunityRoundEntryResponse> entries, String sport) {
        this(id, name, playedAt, entries, sport, null, List.of(), null);
    }
    public CommunityRoundResponse(Long id, String name, Instant playedAt,
            List<CommunityRoundEntryResponse> entries, String sport,
            String matchFormat, List<CommunitySetScore> setScores) {
        this(id, name, playedAt, entries, sport, matchFormat, setScores, null);
    }
}
