package com.example.desporto24.model.dto.analytics;

public record PilotRecurringSupplyResponse(
        long organizersWithCreatedSessions,
        long recurringOrganizers,
        long minimumRecurringOrganizers,
        boolean targetMet
) {
}
