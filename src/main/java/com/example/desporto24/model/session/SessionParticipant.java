package com.example.desporto24.model.session;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "session_participant",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_session_participant_session_profile",
                columnNames = {"session_id", "profile_id"}
        )
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipantState state;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status", nullable = false)
    private AttendanceStatus attendanceStatus;

    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt;

    @Column(name = "exited_at")
    private Instant exitedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_reported_by_profile_id")
    private Profile attendanceReportedBy;

    @Column(name = "attendance_recorded_at")
    private Instant attendanceRecordedAt;

    @Column(name = "feed_hidden", nullable = false)
    private boolean feedHidden;

    @Column(name = "team_code", length = 1)
    private String teamCode;
}
