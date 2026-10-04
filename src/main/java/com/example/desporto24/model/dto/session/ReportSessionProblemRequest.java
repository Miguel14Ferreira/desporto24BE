package com.example.desporto24.model.dto.session;

import com.example.desporto24.enumeration.SessionProblemType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReportSessionProblemRequest(
        @NotNull SessionProblemType type,
        @Size(max = 500) String details
) {
}
