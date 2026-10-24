package com.example.desporto24.model.dto.community;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import java.time.Instant;
import java.util.List;

public record CreateCommunityRoundRequest(
        @NotBlank @Size(max = 100) String name,
        @NotNull Instant playedAt,
        @NotEmpty List<@Valid CommunityRoundEntryRequest> entries,
        @Size(max = 100) String sport,
        String matchFormat,
        List<@Valid CommunitySetScore> setScores,
        @Size(max = 48) String eventType,
        @Size(max = 32) String eventDiscipline,
        List<@Valid CommunityGroupRoutineScore> groupScores,
        @Valid CommunityBasketballScore basketballScore,
        @Valid CommunityTeamFinalScore teamScore,
        @Valid CommunityCricketDetails cricketDetails,
        @Size(max = 40) String sprintSeriesKey,
        @Min(1) @Max(3) Integer sprintHeatNumber
) {
    // Preserve existing canonical callers; the new fields are optional.
    public CreateCommunityRoundRequest(String name, Instant playedAt,
            List<CommunityRoundEntryRequest> entries, String sport,
            String matchFormat, List<CommunitySetScore> setScores,
            String eventType, String eventDiscipline,
            List<CommunityGroupRoutineScore> groupScores,
            CommunityBasketballScore basketballScore,
            CommunityTeamFinalScore teamScore,
            CommunityCricketDetails cricketDetails) {
        this(name, playedAt, entries, sport, matchFormat, setScores,
                eventType, eventDiscipline, groupScores, basketballScore,
                teamScore, cricketDetails, null, null);
    }

    // Existing 11-field canonical signature must remain source-compatible.
    public CreateCommunityRoundRequest(String name, Instant playedAt,
            List<CommunityRoundEntryRequest> entries, String sport,
            String matchFormat, List<CommunitySetScore> setScores,
            String eventType, String eventDiscipline,
            List<CommunityGroupRoutineScore> groupScores,
            CommunityBasketballScore basketballScore,
            CommunityTeamFinalScore teamScore) {
        this(name, playedAt, entries, sport, matchFormat, setScores,
                eventType, eventDiscipline, groupScores, basketballScore, teamScore, null);
    }

    // Keep the old 10-argument canonical signature source compatible.
    public CreateCommunityRoundRequest(String name, Instant playedAt,
            List<CommunityRoundEntryRequest> entries, String sport,
            String matchFormat, List<CommunitySetScore> setScores,
            String eventType, String eventDiscipline,
            List<CommunityGroupRoutineScore> groupScores,
            CommunityBasketballScore basketballScore) {
        this(name, playedAt, entries, sport, matchFormat, setScores,
                eventType, eventDiscipline, groupScores, basketballScore, null);
    }

    // Keep the previous group-results canonical constructor source compatible.
    public CreateCommunityRoundRequest(String name, Instant playedAt,
            List<CommunityRoundEntryRequest> entries, String sport,
            String matchFormat, List<CommunitySetScore> setScores,
            String eventType, String eventDiscipline,
            List<CommunityGroupRoutineScore> groupScores) {
        this(name, playedAt, entries, sport, matchFormat, setScores,
                eventType, eventDiscipline, groupScores, null);
    }

    // Previous canonical constructor must remain source compatible.
    public CreateCommunityRoundRequest(String name, Instant playedAt,
            List<CommunityRoundEntryRequest> entries, String sport,
            String matchFormat, List<CommunitySetScore> setScores,
            String eventType, String eventDiscipline) {
        this(name, playedAt, entries, sport, matchFormat, setScores,
                eventType, eventDiscipline, null);
    }

    // Source compatibility for callers prior to optional discipline support.
    public CreateCommunityRoundRequest(String name, Instant playedAt,
            List<CommunityRoundEntryRequest> entries, String sport,
            String matchFormat, List<CommunitySetScore> setScores, String eventType) {
        this(name, playedAt, entries, sport, matchFormat, setScores, eventType, null);
    }
    // Older callers/clients do not require structured racket scores.
    public CreateCommunityRoundRequest(String name, Instant playedAt,
            List<CommunityRoundEntryRequest> entries) {
        this(name, playedAt, entries, null, null, null, null, null);
    }
    public CreateCommunityRoundRequest(String name, Instant playedAt,
            List<CommunityRoundEntryRequest> entries, String sport) {
        this(name, playedAt, entries, sport, null, null, null, null);
    }
    public CreateCommunityRoundRequest(String name, Instant playedAt,
            List<CommunityRoundEntryRequest> entries, String sport,
            String matchFormat, List<CommunitySetScore> setScores) {
        this(name, playedAt, entries, sport, matchFormat, setScores, null, null);
    }
}
