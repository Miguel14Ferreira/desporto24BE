package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.*;
import java.time.Instant;

public final class CommunityTrackSprintIncidentDtos {
    private CommunityTrackSprintIncidentDtos() {}

    /** Recorded by the organizer; no result or winner in this payload. */
    public record Report(
            @NotBlank String seriesKey,
            @Min(1) @Max(3) int heatNumber,
            @NotBlank String incidentType,
            @NotBlank @Size(min=12,max=500) String details) {}

    /** Enter only a documented decision actually made by commissaires. */
    public record Decide(
            @NotBlank String decisionType,
            @NotBlank @Size(min=12,max=255) String reference,
            @NotBlank @Size(min=12,max=500) String reason,
            String sanctionType, String sanctionedUsername) {
        public Decide(String decisionType, String reference, String reason) {
            this(decisionType,reference,reason,null,null);
        }
    }

    public record View(Long id, String seriesKey, int heatNumber,
                       String incidentType, String details, String status,
                       String reportedBy, Instant reportedAt, String decisionType,
                       String decisionReference, String decisionReason,
                       String decidedBy, Instant decidedAt,
                       String sanctionType, String sanctionedUsername) {
        public View(Long id, String seriesKey, int heatNumber,
                String incidentType, String details, String status,
                String reportedBy, Instant reportedAt, String decisionType,
                String decisionReference, String decisionReason,
                String decidedBy, Instant decidedAt) {
            this(id,seriesKey,heatNumber,incidentType,details,status,reportedBy,
                 reportedAt,decisionType,decisionReference,decisionReason,decidedBy,
                 decidedAt,null,null);
        }
    }
}
