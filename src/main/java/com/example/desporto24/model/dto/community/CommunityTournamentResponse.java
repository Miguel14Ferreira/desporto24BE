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
        Instant completedAt
) {
}
