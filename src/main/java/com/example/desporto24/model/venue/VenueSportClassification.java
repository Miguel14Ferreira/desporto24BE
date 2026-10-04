package com.example.desporto24.model.venue;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "venue_sport_classification")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueSportClassification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_text", nullable = false, length = 180)
    private String matchText;

    @Column(name = "required_match_text", length = 180)
    private String requiredMatchText;

    @Column(nullable = false, length = 255)
    private String sports;

    @Column(nullable = false)
    @Builder.Default
    private int priority = 100;
}
