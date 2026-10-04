package com.example.desporto24.model.moderation;

import com.example.desporto24.enumeration.UserReportStatus;
import com.example.desporto24.model.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "user_report")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reporter_profile_id", nullable = false)
    private Profile reporter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reported_profile_id", nullable = false)
    private Profile reported;

    @Column(nullable = false, length = 1000)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private UserReportStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
