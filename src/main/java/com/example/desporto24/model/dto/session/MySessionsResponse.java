package com.example.desporto24.model.dto.session;

import java.util.List;

public record MySessionsResponse(
        List<SessionSummaryResponse> created,
        List<SessionSummaryResponse> joined,
        List<SessionSummaryResponse> waitlisted
) {
}
