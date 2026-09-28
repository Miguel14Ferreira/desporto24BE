package com.example.desporto24.model.dto.idea;

import com.example.desporto24.enumeration.IdeaStatus;
import java.time.Instant;

public record AdminIdeaResponse(
        Long id,
        String name,
        String city,
        String email,
        String gender,
        String subject,
        String problem,
        IdeaStatus status,
        Instant createdAt
) {
}
