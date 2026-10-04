package com.example.desporto24.model.dto.moderation;

import com.example.desporto24.enumeration.UserReportStatus;

import java.time.Instant;

public record AdminUserReportResponse(
        Long id,
        String reporterUsername,
        String reportedUsername,
        String reason,
        UserReportStatus status,
        Instant createdAt
) {
}
