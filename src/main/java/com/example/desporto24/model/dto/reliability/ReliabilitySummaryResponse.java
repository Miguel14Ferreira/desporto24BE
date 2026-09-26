package com.example.desporto24.model.dto.reliability;

public record ReliabilitySummaryResponse(
        double attendanceRate,
        int completedSessions,
        int noShows,
        int lateCancellations,
        double penaltyScore,
        boolean warning,
        boolean reducedPriority,
        boolean joinRestricted
) {
}
