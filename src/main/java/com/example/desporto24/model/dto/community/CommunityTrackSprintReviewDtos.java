package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.*;
import java.time.Instant;

public final class CommunityTrackSprintReviewDtos {
    private CommunityTrackSprintReviewDtos() {}

    /** A declaration transcribing the president's formal request, not an ordinary appeal. */
    public record Request(
            @NotBlank @Size(min=3,max=120) String presidentName,
            @NotBlank @Size(min=12,max=255) String requestReference,
            @NotBlank @Size(min=12,max=500) String reason,
            @NotNull Boolean originalJudgeRefereeDecision) {}

    /** Only after a review by the panel can its president retain/overturn. */
    public record Resolve(
            @NotBlank String outcome,
            @NotBlank @Size(min=3,max=120) String presidentName,
            @NotBlank @Size(min=12,max=255) String panelReviewReference,
            @NotBlank @Size(min=12,max=255) String presidentDecisionReference,
            @NotBlank @Size(min=12,max=500) String reason) {}

    public record View(Long id, Long incidentId, String action,
                       String officialName, String officialReference,
                       String panelReference, String reason,
                       String recordedBy, Instant recordedAt) {}
}
