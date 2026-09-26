package com.example.desporto24.model.dto.session;

import com.example.desporto24.enumeration.AttendanceStatus;

import java.time.Instant;

public record SessionParticipantResponse(
        Long id,
        String username,
        AttendanceStatus attendanceStatus,
        Instant joinedAt
) {
}
