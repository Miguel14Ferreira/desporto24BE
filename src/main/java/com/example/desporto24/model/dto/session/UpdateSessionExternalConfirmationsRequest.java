package com.example.desporto24.model.dto.session;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateSessionExternalConfirmationsRequest(
        @NotNull @Min(0) @Max(99) Integer externalConfirmedParticipants
) {
}
