package com.example.desporto24.model.dto.community;

import com.example.desporto24.enumeration.TournamentFormat;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.enumeration.TournamentEntryType;
import com.example.desporto24.enumeration.TournamentTeamTieBreakPriority;

import java.time.Instant;

public record CommunityTournamentResponse(
        Long id,
        String name,
        TournamentFormat format,
        TournamentStatus status,
        int winPoints,
        int drawPoints,
        int lossPoints,
        Instant createdAt,
        Instant startedAt,
        Instant completedAt,
        Long championProfileId,
        String championUsername,
        String championDisplayName,
        Long championPoints,
        boolean championTied,
        String sport,
        TournamentEntryType entryType,
        Integer playersPerTeam,
        Long championTeamId,
        String championTeamName,
        int roundRobinLegs,
        TournamentTeamTieBreakPriority teamTieBreakPriority
) {
    // Source compatibility for clients using the existing 21-field constructor.
    public CommunityTournamentResponse(Long id, String name, TournamentFormat format,
            TournamentStatus status, int winPoints, int drawPoints, int lossPoints,
            Instant createdAt, Instant startedAt, Instant completedAt,
            Long championProfileId, String championUsername, String championDisplayName,
            Long championPoints, boolean championTied, String sport,
            TournamentEntryType entryType, Integer playersPerTeam,
            Long championTeamId, String championTeamName, int roundRobinLegs) {
        this(id, name, format, status, winPoints, drawPoints, lossPoints,
                createdAt, startedAt, completedAt, championProfileId, championUsername,
                championDisplayName, championPoints, championTied, sport,
                entryType, playersPerTeam, championTeamId, championTeamName,
                roundRobinLegs, TournamentTeamTieBreakPriority.GOAL_DIFFERENCE_FIRST);
    }
    // Preserve clients which instantiate the 20-field representation.
    public CommunityTournamentResponse(Long id, String name, TournamentFormat format,
            TournamentStatus status, int winPoints, int drawPoints, int lossPoints,
            Instant createdAt, Instant startedAt, Instant completedAt,
            Long championProfileId, String championUsername, String championDisplayName,
            Long championPoints, boolean championTied, String sport,
            TournamentEntryType entryType, Integer playersPerTeam,
            Long championTeamId, String championTeamName) {
        this(id, name, format, status, winPoints, drawPoints, lossPoints, createdAt,
                startedAt, completedAt, championProfileId, championUsername, championDisplayName,
                championPoints, championTied, sport, entryType, playersPerTeam,
                championTeamId, championTeamName, 1);
    }

    public CommunityTournamentResponse(Long id, String name, TournamentFormat format,
            TournamentStatus status, int winPoints, int drawPoints, int lossPoints,
            Instant createdAt, Instant startedAt, Instant completedAt,
            Long championProfileId, String championUsername, String championDisplayName,
            Long championPoints, boolean championTied, String sport,
            TournamentEntryType entryType, Integer playersPerTeam) {
        this(id, name, format, status, winPoints, drawPoints, lossPoints, createdAt,
                startedAt, completedAt, championProfileId, championUsername, championDisplayName,
                championPoints, championTied, sport, entryType, playersPerTeam, null, null, 1);
    }
    public CommunityTournamentResponse(Long id, String name, TournamentFormat format,
            TournamentStatus status, int winPoints, int drawPoints, int lossPoints,
            Instant createdAt, Instant startedAt, Instant completedAt,
            Long championProfileId, String championUsername, String championDisplayName,
            Long championPoints, boolean championTied, String sport) {
        this(id, name, format, status, winPoints, drawPoints, lossPoints,
                createdAt, startedAt, completedAt, championProfileId, championUsername,
                championDisplayName, championPoints, championTied, sport,
                TournamentEntryType.INDIVIDUAL, null);
    }

    // Maintain source compatibility for existing 15-field callers.
    public CommunityTournamentResponse(
            Long id, String name, TournamentFormat format, TournamentStatus status,
            int winPoints, int drawPoints, int lossPoints,
            Instant createdAt, Instant startedAt, Instant completedAt,
            Long championProfileId, String championUsername, String championDisplayName,
            Long championPoints, boolean championTied) {
        this(id, name, format, status, winPoints, drawPoints, lossPoints,
                createdAt, startedAt, completedAt, championProfileId, championUsername,
                championDisplayName, championPoints, championTied, null);
    }

    // Source compatibility for older 10-field direct constructors.
    public CommunityTournamentResponse(
            Long id, String name, TournamentFormat format, TournamentStatus status,
            int winPoints, int drawPoints, int lossPoints,
            Instant createdAt, Instant startedAt, Instant completedAt) {
        this(id, name, format, status, winPoints, drawPoints, lossPoints,
                createdAt, startedAt, completedAt, null, null, null, null, false);
    }

    public static CommunityTournamentResponse from(
            com.example.desporto24.model.community.CommunityTournament tournament) {
        return new CommunityTournamentResponse(
                tournament.getId(), tournament.getName(), tournament.getFormat(),
                tournament.getStatus(), tournament.getWinPoints(), tournament.getDrawPoints(),
                tournament.getLossPoints(), tournament.getCreatedAt(), tournament.getStartedAt(),
                tournament.getCompletedAt(), tournament.getChampionProfileId(),
                tournament.getChampionUsername(), tournament.getChampionDisplayName(),
                tournament.getChampionPoints(), tournament.isChampionTied(), tournament.getSport(),
                tournament.getEntryType(), tournament.getPlayersPerTeam(),
                tournament.getChampionTeamId(), tournament.getChampionTeamName(),
                tournament.getRoundRobinLegs(), tournament.getTeamTieBreakPriority());
    }
}
