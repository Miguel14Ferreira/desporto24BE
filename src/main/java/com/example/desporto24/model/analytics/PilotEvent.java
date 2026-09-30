package com.example.desporto24.model.analytics;

import com.example.desporto24.enumeration.PilotEventType;
import com.example.desporto24.model.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "pilot_event",
        indexes = {
                @Index(name = "idx_pilot_event_type_created_at", columnList = "event_type, created_at"),
                @Index(name = "idx_pilot_event_profile_id", columnList = "profile_id")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PilotEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 40)
    private PilotEventType eventType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(name = "device_id", length = 128)
    private String deviceId;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
