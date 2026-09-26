package com.example.desporto24.model.dto.reliability;

import com.example.desporto24.enumeration.DisputeStatus;

import java.time.Instant;

public record AttendanceDisputeResponse(
        Long id,
        Long participantId,
        Long sessionId,
        String sport,
        String city,
        Instant gameDate,
        String participantUsername,
        String reporterUsername,
        String reason,
        DisputeStatus status,
        Instant createdAt,
        Instant resolvedAt
) {
}
