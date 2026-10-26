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

    @Column(name="decision_type",length=32)
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


    // UCI 3.2.048(1): the rider not at fault chooses inside/outside.
    // These are human-transcribed facts, not a race result.
    @Column(name="fall_fault_rider",length=100)
    private String fallFaultRider;

    @Column(name="fall_non_fault_rider",length=100)
    private String fallNonFaultRider;

    @Column(name="fall_chosen_start_position",length=8)
    private String fallChosenStartPosition;

    // UCI 3.2.048(3): pre-sprint balance or contact, not a fall.
    @Column(name="pre_sprint_incident_rider",length=100)
    private String preSprintIncidentRider;

    @Column(name="pre_sprint_non_fault_rider",length=100)
    private String preSprintNonFaultRider;

    @Column(name="pre_sprint_start_position",length=8)
    private String preSprintStartPosition;

    @Column(name="pre_sprint_before_start_confirmed")
    private Boolean preSprintBeforeStartConfirmed;

    @Column(name="pre_sprint_starter_stopped_confirmed")
    private Boolean preSprintStarterStoppedConfirmed;

    // Explicit subsequent restart order: never change the original ruling.
    @Column(name="rerun_riders_json",columnDefinition="TEXT")
    private String rerunRidersJson;

    @Column(name="rerun_reference",length=255)
    private String rerunReference;

    @Column(name="rerun_reason",length=500)
    private String rerunReason;

    @Column(name="rerun_authorized_by",length=100)
    private String rerunAuthorizedBy;

    @Column(name="rerun_authorized_at")
    private Instant rerunAuthorizedAt;

    // Declared panel placing at the instant of a no-fault incident, not
    // a physical heat. Two ordered rider identities are stored separately.
    @Column(name="official_positions_json",columnDefinition="TEXT")
    private String officialPositionsJson;

    // A separate official award of a single two-up heat, never a raced result.
    @Column(name="award_winner", length=100)
    private String awardWinner;

    @Column(name="award_reference", length=255)
    private String awardReference;

    @Column(name="award_reason", length=500)
    private String awardReason;

    @Column(name="award_confirmed_by", length=100)
    private String awardConfirmedBy;

    @Column(name="award_confirmed_at")
    private Instant awardConfirmedAt;

    @Column(name="decided_by",length=100)
    private String decidedBy;

    @Column(name="decided_at")
    private Instant decidedAt;
}
