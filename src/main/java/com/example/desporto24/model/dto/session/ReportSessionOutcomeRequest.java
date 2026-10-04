package com.example.desporto24.model.dto.session;

import com.example.desporto24.enumeration.SessionOutcome;
import jakarta.validation.constraints.NotNull;

public record ReportSessionOutcomeRequest(
        @NotNull SessionOutcome outcome
) {
}
