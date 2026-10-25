package com.example.desporto24.model.dto.venue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Map;

/** A PATCH-like suggestion. Only keys in the server allowlist can be submitted. */
public record VenueEditSuggestionRequest(
        @NotEmpty Map<String, String> changes,
        @NotBlank @Size(min = 10, max = 1000) String reason
) {}
