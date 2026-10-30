package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/**
 * Append-only transcription of the official UCI 3.2.011 procedure.
 * The account recording an attestation does not thereby become a commissaire.
 * Never edit or delete an event in order to "correct" a sporting outcome.
 */
@Entity
@Table(name="community_track_sprint_review")
@Getter @Builder @NoArgsConstructor(access=AccessLevel.PROTECTED) @AllArgsConstructor
public class CommunityTrackSprintReview {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="incident_id",nullable=false)
    private CommunityTrackSprintIncident incident;

    @Column(name="action",nullable=false,length=16)
    private String action; // REQUESTED, UPHELD, OVERTURNED, RECTIFIED

    @Column(name="official_name",nullable=false,length=120)
    private String officialName; // Declared president, NOT platform identity

    @Column(name="official_reference",nullable=false,length=255)
    private String officialReference;

    @Column(name="panel_reference",length=255)
    private String panelReference;

    // Only the RECTIFIED event contains a documented restart instruction.
    // Do not rewrite the original decision or any physically raced heat.
    @Column(name="remedy_kind",length=32)
    private String remedyKind;

    @Column(name="ordered_riders_json",columnDefinition="TEXT")
    private String orderedRidersJson;

    @Column(name="next_heat_number")
    private Integer nextHeatNumber;

    @Column(name="reason",nullable=false,length=500)
    private String reason;

    @Column(name="recorded_by",nullable=false,length=100)
    private String recordedBy;

    @Column(name="recorded_at",nullable=false)
    private Instant recordedAt;
}
