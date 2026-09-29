package com.example.desporto24.model.venue;

import com.example.desporto24.model.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "venue_photo")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenuePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @Column(name = "photo_url", nullable = false, length = 1200)
    private String photoUrl;

    @Column(name = "storage_key", length = 800)
    private String storageKey;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by_profile_id", nullable = false)
    private Profile uploadedBy;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
