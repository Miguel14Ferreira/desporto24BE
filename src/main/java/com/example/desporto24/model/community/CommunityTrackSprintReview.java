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
    private String action; // REQUESTED, UPHELD, OVERTURNED

    @Column(name="official_name",nullable=false,length=120)
    private String officialName; // Declared president, NOT platform identity

    @Column(name="official_reference",nullable=false,length=255)
    private String officialReference;

    @Column(name="panel_reference",length=255)
    private String panelReference;

    @Column(name="reason",nullable=false,length=500)
    private String reason;

    @Column(name="recorded_by",nullable=false,length=100)
    private String recordedBy;

    @Column(name="recorded_at",nullable=false)
    private Instant recordedAt;
}
