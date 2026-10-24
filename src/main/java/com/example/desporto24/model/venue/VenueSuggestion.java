package com.example.desporto24.model.venue;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/** User-proposed installation, kept separate from publicly searchable venues. */
@Entity
@Table(name = "venue_suggestion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VenueSuggestion {
    public enum Status { PENDING, APPROVED, REJECTED }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    @Builder.Default
    private Status status = Status.PENDING;

    @Column(nullable = false, length = 140)
    private String name;
    @Column(nullable = false, length = 120)
    private String city;
    @Column(length = 120)
    private String district;
    @Column(length = 255)
    private String address;
    @Column(length = 255)
    private String sports;

    @Column(name = "submitted_by_username", nullable = false, length = 120)
    private String submittedByUsername;
    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    @Column(name = "request_payload", nullable = false, columnDefinition = "text")
    private String requestPayload;

    @Column(name = "reviewed_by_username", length = 120)
    private String reviewedByUsername;
    @Column(name = "reviewed_at")
    private Instant reviewedAt;
    @Column(name = "published_venue_id")
    private Long publishedVenueId;
}
