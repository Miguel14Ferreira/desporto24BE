package com.example.desporto24.model.dto.analytics;

public record PilotParticipationResponse(
        long confirmedParticipants,
        long completedSessions,
        long minimumConfirmedParticipants,
        long minimumCompletedSessions,
        boolean participantsTargetMet,
        boolean completedSessionsTargetMet
) {
}
