package com.example.desporto24.model.dto.session;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Optional one-line arrival note. null or blank clears a previous value. */
public record UpdateSessionMeetingPointRequest(
        @Size(max = 240)
        @Pattern(regexp = "^[^\\p{Cc}]*$", message = "As instruções devem ter uma única linha")
        String meetingPointInstructions
) {}
