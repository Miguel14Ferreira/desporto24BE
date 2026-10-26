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


    /**
     * Following an UNINTENTIONAL_FALL finding, the rider NOT at fault
     * expressly chooses the inside or outside starting position.
     * Neither a result nor a disciplinary sanction can be supplied.
     */
    public record UnintentionalFallRestart(
            @NotBlank String faultRider,
            @NotBlank String nonFaultRider,
            @NotBlank String nonFaultStartPosition,
            @NotNull Boolean nonFaultChoiceConfirmed,
            @NotBlank @Size(min=12,max=255) String reference,
            @NotBlank @Size(min=12,max=500) String reason) {}

    /** UCI 3.2.048(3): independent starter-stop and rider choice record. */
    public record PreSprintRestart(
            @NotBlank String incidentRider,
            @NotBlank String nonFaultRider,
            @NotBlank String nonFaultStartPosition,
            @NotNull Boolean nonFaultChoiceConfirmed,
            @NotNull Boolean beforeSprintConfirmed,
            @NotNull Boolean starterStoppedConfirmed,
            @NotBlank @Size(min=12,max=255) String reference,
            @NotBlank @Size(min=12,max=500) String reason) {}

    /**
     * A second explicit panel instruction after a no-fault fall, puncture,
     * or essential bicycle breakage. It cannot create a heat or winner.
     */
    public record NeutralRestart(
            @NotBlank String cause,
            @NotNull Boolean noFaultConfirmed,
            @NotNull @Size(min=2,max=2) java.util.List<String> orderedRiders,
            @NotBlank @Size(min=12,max=255) String reference,
            @NotBlank @Size(min=12,max=500) String reason) {}

    /**
     * Separate signed panel ratification of the final positions at the
     * moment of a neutral incident; first rider is the declared heat winner.
     * No physical round, lap time or race result is fabricated.
     */
    public record RatifyIncidentPositions(
            @NotNull @Size(min=2,max=2) java.util.List<String> orderedRiders,
            @NotNull Boolean panelPositionsConfirmed,
            @NotBlank @Size(min=12,max=255) String reference,
            @NotBlank @Size(min=12,max=500) String reason) {}

    /**
     * A separate signed authorization for a three-up rerun. Must explicitly
     * name and order exactly the two surviving riders; no winner is selected.
     */
    public record AuthorizeRerun(
            @NotNull @Size(min=2,max=2) java.util.List<String> orderedRiders,
            @NotBlank @Size(min=12,max=255) String reference,
            @NotBlank @Size(min=12,max=500) String reason) {}

    /** Separate, witnessed match award following an intentional fall in a single
     * two-rider heat. This is NOT a physical race time/heat recording. */
    public record ConfirmOfficialAward(
            @NotBlank String winnerUsername,
            @NotBlank String basis,
            @NotBlank @Size(min=12,max=255) String reference,
            @NotBlank @Size(min=12,max=500) String reason) {}

    public record View(Long id, String seriesKey, int heatNumber,
                       String incidentType, String details, String status,
                       String reportedBy, Instant reportedAt, String decisionType,
                       String decisionReference, String decisionReason,
                       String decidedBy, Instant decidedAt,
                       String sanctionType, String sanctionedUsername,
                       java.util.List<String> rerunRiders, String rerunReference,
                       String rerunReason, String rerunAuthorizedBy,
                       Instant rerunAuthorizedAt,
                       String awardWinner, String awardReference, String awardReason,
                       String awardConfirmedBy, Instant awardConfirmedAt,
                       java.util.List<String> officialPositions,
                       String fallFaultRider, String fallNonFaultRider,
                       String fallChosenStartPosition,
                       String preSprintIncidentRider, String preSprintNonFaultRider,
                       String preSprintStartPosition, Boolean preSprintBeforeStartConfirmed,
                       Boolean preSprintStarterStoppedConfirmed) {
        public View(Long id, String seriesKey, int heatNumber,
                String incidentType, String details, String status,
                String reportedBy, Instant reportedAt, String decisionType,
                String decisionReference, String decisionReason,
                String decidedBy, Instant decidedAt) {
            this(id,seriesKey,heatNumber,incidentType,details,status,reportedBy,
                 reportedAt,decisionType,decisionReference,decisionReason,decidedBy,
                 decidedAt,null,null,java.util.List.of(),null,null,null,null,
                 null,null,null,null,null,java.util.List.of(),null,null,null,null,null,null,null,null);
        }
    }
}
