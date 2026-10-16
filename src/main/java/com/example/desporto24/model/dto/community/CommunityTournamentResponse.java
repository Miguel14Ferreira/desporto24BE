package com.example.desporto24.model.dto.community;

import com.example.desporto24.enumeration.TournamentFormat;
import com.example.desporto24.enumeration.TournamentStatus;

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
        String sport
) {
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
                tournament.getChampionPoints(), tournament.isChampionTied(), tournament.getSport());
    }
}
