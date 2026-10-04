package com.example.desporto24.model.dto.venue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResolveVenueMapLinkRequest(
        @NotBlank @Size(max = 2048) String url
) {
}
