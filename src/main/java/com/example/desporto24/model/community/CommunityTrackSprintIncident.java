package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/**
 * A human-filed race incident is NOT a sporting result.
 * A documented commissaires' decision only unlocks a subsequent explicit
 * heat/result entry; no winner or points are inferred.
 */
@Entity
@Table(name="community_track_sprint_incident")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityTrackSprintIncident {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false,fetch=FetchType.LAZY)
    @JoinColumn(name="bracket_id",nullable=false)
    private CommunityTrackSprintBracket bracket;

    @Column(name="series_key",nullable=false,length=40)
    private String seriesKey;

    @Column(name="heat_number",nullable=false)
    private int heatNumber;

    @Column(name="incident_type",nullable=false,length=32)
    private String incidentType;

    @Column(name="details",nullable=false,length=500)
    private String details;

    @Column(name="status",nullable=false,length=28)
    private String status;

    @Column(name="reported_by",nullable=false,length=100)
    private String reportedBy;

    @Column(name="reported_at",nullable=false)
    private Instant reportedAt;

    @Column(name="decision_type",length=24)
    private String decisionType;

    @Column(name="decision_reference",length=255)
    private String decisionReference;

    @Column(name="decision_reason",length=500)
    private String decisionReason;

    // An official sanction is evidence, never a generated sporting result.
    // Relegation / DQ remain on hold until a supported amended bracket flow.
    @Column(name="sanction_type",length=24)
    private String sanctionType;

    @Column(name="sanctioned_username",length=100)
    private String sanctionedUsername;

    @Column(name="decided_by",length=100)
    private String decidedBy;

    @Column(name="decided_at")
    private Instant decidedAt;
}
