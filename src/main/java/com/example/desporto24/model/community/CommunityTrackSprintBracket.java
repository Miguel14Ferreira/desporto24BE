package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/** Fixed eight-rider Sprint finals draw, explicitly created within one season.
 * The existing Sprint heat records remain the only source of athletic results.
 */
@Entity
@Table(name = "community_track_sprint_bracket")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityTrackSprintBracket {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private CommunitySeason season;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
