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
        String eventType,
        String eventDiscipline,
        List<CommunityGroupRoutineScore> groupScores,
        CommunityBasketballScore basketballScore,
        CommunityTeamFinalScore teamScore
) {
    public CommunityRoundResponse(Long id, String name, Instant playedAt,
            List<CommunityRoundEntryResponse> entries, String sport,
            String matchFormat, List<CommunitySetScore> setScores,
            String eventType, String eventDiscipline,
            List<CommunityGroupRoutineScore> groupScores,
            CommunityBasketballScore basketballScore) {
        this(id, name, playedAt, entries, sport, matchFormat,
                setScores, eventType, eventDiscipline, groupScores, basketballScore, null);
    }

    public CommunityRoundResponse(Long id, String name, Instant playedAt,
            List<CommunityRoundEntryResponse> entries, String sport,
            String matchFormat, List<CommunitySetScore> setScores,
            String eventType, String eventDiscipline,
            List<CommunityGroupRoutineScore> groupScores) {
        this(id, name, playedAt, entries, sport, matchFormat,
                setScores, eventType, eventDiscipline, groupScores, null);
    }

    // Keep existing Java constructors compatible with older tests/callers.
    public CommunityRoundResponse(Long id, String name, Instant playedAt,
            List<CommunityRoundEntryResponse> entries, String sport,
            String matchFormat, List<CommunitySetScore> setScores,
            String eventType, String eventDiscipline) {
        this(id, name, playedAt, entries, sport, matchFormat,
                setScores, eventType, eventDiscipline, List.of());
    }

    public CommunityRoundResponse(Long id, String name, Instant playedAt,
            List<CommunityRoundEntryResponse> entries, String sport,
            String matchFormat, List<CommunitySetScore> setScores, String eventType) {
        this(id, name, playedAt, entries, sport, matchFormat, setScores, eventType, null);
    }
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
