package com.example.desporto24.model.venue;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/** Proposed changes to a published venue: never visible until an admin approves. */
@Entity
@Table(name = "venue_edit_suggestion")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class VenueEditSuggestion {
    public enum Status { PENDING, APPROVED, REJECTED }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    @Builder.Default
    private Status status = Status.PENDING;

    @Column(name = "changes_json", nullable = false, columnDefinition = "text")
    private String changesJson;

    @Column(name = "before_json", nullable = false, columnDefinition = "text")
    private String beforeJson;

    @Column(name = "reason", nullable = false, length = 1000)
    private String reason;

    @Column(name = "submitted_by_username", nullable = false, length = 120)
    private String submittedByUsername;

    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    @Column(name = "reviewed_by_username", length = 120)
    private String reviewedByUsername;

    @Column(name = "reviewed_at")
    private Instant reviewedAt;

    @Column(name = "decision_note", length = 1000)
    private String decisionNote;
}
