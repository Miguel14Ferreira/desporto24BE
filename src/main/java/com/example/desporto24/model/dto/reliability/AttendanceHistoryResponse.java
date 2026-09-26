package com.example.desporto24.model.dto.reliability;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.DisputeStatus;

import java.time.Instant;

public record AttendanceHistoryResponse(
        Long participantId,
        Long sessionId,
        String sport,
        String city,
        Instant gameDate,
        AttendanceStatus attendanceStatus,
        Instant attendanceRecordedAt,
        DisputeStatus disputeStatus,
        boolean canDispute
) {
}
