package com.example.desporto24.model.dto.session;

import com.example.desporto24.enumeration.SessionRecurrence;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateSessionRequest(
        @NotBlank String sport,
        @NotNull Instant gameDate,
        @NotBlank
        @Size(max = 120)
        @Pattern(
                regexp = "^[\\p{L}\\p{M}][\\p{L}\\p{M}\\d .,'’()/-]{1,119}$",
                message = "A localidade deve conter um nome válido"
        )
        String city,
        @NotBlank String address,
        @NotNull @Min(2) @Max(100) Integer maxParticipants,
        @DecimalMin("0.00") BigDecimal priceAmount,
        boolean privateGame,
        String password,
        Double latitude,
        Double longitude,
        Long communityId,
        Long venueId,
        SessionRecurrence recurrence,
        @Min(2) @Max(52) Integer recurrenceOccurrences,
        // Excludes the organiser, who is automatically registered in Jynlo.
        @Min(0) @Max(99) Integer externalConfirmedParticipants,
        com.example.desporto24.enumeration.RegistrationOpeningMode registrationOpeningMode,
        Instant registrationOpensAt,
        // First week's additional kickoffs. The first kickoff remains gameDate.
        @Size(max = 6) java.util.List<@NotNull Instant> additionalWeeklyGameDates,
        // IANA zone keeps the same local weekday/time across DST transitions.
        @Size(max = 64) String recurrenceTimeZone,
        com.example.desporto24.enumeration.SessionPlayFormat playFormat
) {
    // Preserve all 19-field callers of the former canonical record constructor.
    public CreateSessionRequest(
            String sport, Instant gameDate, String city, String address,
            Integer maxParticipants, BigDecimal priceAmount, boolean privateGame,
            String password, Double latitude, Double longitude, Long communityId,
            Long venueId, SessionRecurrence recurrence, Integer recurrenceOccurrences,
            Integer externalConfirmedParticipants,
            com.example.desporto24.enumeration.RegistrationOpeningMode registrationOpeningMode,
            Instant registrationOpensAt, java.util.List<Instant> additionalWeeklyGameDates,
            String recurrenceTimeZone) {
        this(sport, gameDate, city, address, maxParticipants, priceAmount,
                privateGame, password, latitude, longitude, communityId, venueId,
                recurrence, recurrenceOccurrences, externalConfirmedParticipants,
                registrationOpeningMode, registrationOpensAt,
                additionalWeeklyGameDates, recurrenceTimeZone, null);
    }

    // Keep all existing 17-field callers source-compatible.
    public CreateSessionRequest(
            String sport, Instant gameDate, String city, String address,
            Integer maxParticipants, BigDecimal priceAmount, boolean privateGame,
            String password, Double latitude, Double longitude, Long communityId,
            Long venueId, SessionRecurrence recurrence, Integer recurrenceOccurrences,
            Integer externalConfirmedParticipants,
            com.example.desporto24.enumeration.RegistrationOpeningMode registrationOpeningMode,
            Instant registrationOpensAt) {
        this(sport, gameDate, city, address, maxParticipants, priceAmount,
                privateGame, password, latitude, longitude, communityId, venueId,
                recurrence, recurrenceOccurrences, externalConfirmedParticipants,
                registrationOpeningMode, registrationOpensAt, null, null);
    }

    // Preserve legacy service/controller/test callers (15-argument constructor).
    public CreateSessionRequest(
            String sport, Instant gameDate, String city, String address,
            Integer maxParticipants, BigDecimal priceAmount, boolean privateGame,
            String password, Double latitude, Double longitude, Long communityId,
            Long venueId, SessionRecurrence recurrence, Integer recurrenceOccurrences,
            Integer externalConfirmedParticipants) {
        this(sport, gameDate, city, address, maxParticipants, priceAmount,
                privateGame, password, latitude, longitude, communityId,
                venueId, recurrence, recurrenceOccurrences,
                externalConfirmedParticipants, null, null);
    }
    // Keep existing Java callers compatible with the former record constructor.
    public CreateSessionRequest(
            String sport,
            Instant gameDate,
            String city,
            String address,
            Integer maxParticipants,
            BigDecimal priceAmount,
            boolean privateGame,
            String password,
            Double latitude,
            Double longitude,
            Long communityId,
            Long venueId,
            SessionRecurrence recurrence,
            Integer recurrenceOccurrences) {
        this(sport, gameDate, city, address, maxParticipants, priceAmount,
                privateGame, password, latitude, longitude, communityId,
                venueId, recurrence, recurrenceOccurrences, null);
    }

    public CreateSessionRequest(
            String sport,
            Instant gameDate,
            String city,
            String address,
            Integer maxParticipants,
            BigDecimal priceAmount,
            boolean privateGame,
            String password,
            Double latitude,
            Double longitude) {
        this(
                sport,
                gameDate,
                city,
                address,
                maxParticipants,
                priceAmount,
                privateGame,
                password,
                latitude,
                longitude,
                null,
                null,
                SessionRecurrence.NONE,
                null
        );
    }

    public CreateSessionRequest(
            String sport,
            Instant gameDate,
            String city,
            String address,
            Integer maxParticipants,
            BigDecimal priceAmount,
            boolean privateGame,
            String password,
            Double latitude,
            Double longitude,
            Long communityId) {
        this(
                sport,
                gameDate,
                city,
                address,
                maxParticipants,
                priceAmount,
                privateGame,
                password,
                latitude,
                longitude,
                communityId,
                null,
                SessionRecurrence.NONE,
                null
        );
    }

    public CreateSessionRequest(
            String sport,
            Instant gameDate,
            String city,
            String address,
            Integer maxParticipants,
            BigDecimal priceAmount,
            boolean privateGame,
            String password,
            Double latitude,
            Double longitude,
            Long communityId,
            Long venueId) {
        this(
                sport,
                gameDate,
                city,
                address,
                maxParticipants,
                priceAmount,
                privateGame,
                password,
                latitude,
                longitude,
                communityId,
                venueId,
                SessionRecurrence.NONE,
                null
        );
    }
}
