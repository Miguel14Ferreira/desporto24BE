package com.example.desporto24.model.dto.analytics;

public record PilotEventCountsResponse(
        long access,
        long registrationCompleted,
        long sessionJoined,
        long attendanceConfirmed,
        long sessionCompleted
) {
}
