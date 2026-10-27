package com.example.desporto24.model;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.enumeration.SessionOutcome;
import com.example.desporto24.enumeration.SessionProblemType;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.venue.Venue;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(NON_DEFAULT)
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String userId;
    private String sport;
    private String username;
    private String players;
    private Date gameDate;
    private String city;
    private String address;
    private String price;
    private String password;
    private Boolean privateGame;
    private String gamePhoto;
    private Boolean enabled;
    private Date created_at;

    @Enumerated(EnumType.STRING)
    @Column(name = "play_format", length = 16)
    private com.example.desporto24.enumeration.SessionPlayFormat playFormat;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    // Counts confirmed people without a Jynlo membership. The creator is already an ACTIVE participant.
    @Builder.Default
    @Column(name = "external_confirmed_participants", nullable = false)
    private Integer externalConfirmedParticipants = 0;

    /** SHA-256 digest of a one-time issued 256-bit WhatsApp sharing token. */
    @Column(name="guest_link_hash",length=64)
    private String guestLinkHash;

    @Column(name = "price_amount", precision = 10, scale = 2)
    private BigDecimal priceAmount;

    @Column(name = "password_hash")
    private String passwordHash;

    private Double latitude;
    private Double longitude;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "outcome")
    private SessionOutcome outcome;

    @Enumerated(EnumType.STRING)
    @Column(name = "problem_type")
    private SessionProblemType problemType;

    @Column(name = "problem_details", length = 500)
    private String problemDetails;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "post_game_review_reminder_sent_at")
    private Instant postGameReviewReminderSentAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "audience")
    private SessionAudience audience;
    @ManyToOne
    @JoinColumn(nullable = false,
            name = "perfil_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @Enumerated(EnumType.STRING)
    @Column(name = "registration_opening_mode", length = 16)
    private com.example.desporto24.enumeration.RegistrationOpeningMode registrationOpeningMode;

    @Column(name = "registration_opens_at")
    private Instant registrationOpensAt;

    @Column(name = "recurrence_series_id", length = 36)
    private String recurrenceSeriesId;

    public Session(String username, String desporto, String jogadores, Date datadejogo, String localidade, Boolean privado, String morada, String preco, String password) {
        this.username = username;
        this.sport = desporto;
        this.players = jogadores;
        this.gameDate = datadejogo;
        this.city = localidade;
        this.privateGame = privado;
        this.address = morada;
        this.price = preco;
        this.password = password;
    }
}
