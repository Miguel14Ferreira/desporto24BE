package com.example.desporto24.model.dto.session;

import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.enumeration.SessionProblemType;
import com.example.desporto24.model.dto.venue.VenueResponse;

import java.math.BigDecimal;
import java.time.Instant;

public record SessionDetailResponse(
        Long id,
        String sport,
        String city,
        Instant gameDate,
        Double distanceKm,
        int participantCount,
        int maxParticipants,
        BigDecimal priceAmount,
        boolean privateGame,
        SessionStatus status,
        String creatorUsername,
        String address,
        boolean canSeeExactAddress,
        boolean joined,
        boolean waitlisted,
        Integer waitlistPosition,
        String gamePhoto,
        VenueResponse venue,
        SessionAudience audience,
        String recurrenceSeriesId,
        SessionProblemType problemType,
        String problemDetails,
        Long communityId,
        boolean canSharePublicly,
        int externalConfirmedParticipants,
        com.example.desporto24.enumeration.RegistrationOpeningMode registrationOpeningMode,
        Instant registrationOpensAt,
        boolean teamBalanceSupported,
        com.example.desporto24.enumeration.SessionPlayFormat playFormat,
        String meetingPointInstructions,
        int minParticipants
) {
    /** Compatible with existing detail response callers. */
    public SessionDetailResponse(
            Long id, String sport, String city, Instant gameDate, Double distanceKm,
            int participantCount, int maxParticipants, BigDecimal priceAmount,
            boolean privateGame, SessionStatus status, String creatorUsername,
            String address, boolean canSeeExactAddress, boolean joined,
            boolean waitlisted, Integer waitlistPosition, String gamePhoto,
            VenueResponse venue, SessionAudience audience, String recurrenceSeriesId,
            SessionProblemType problemType, String problemDetails, Long communityId,
            boolean canSharePublicly, int externalConfirmedParticipants,
            com.example.desporto24.enumeration.RegistrationOpeningMode registrationOpeningMode,
            Instant registrationOpensAt, boolean teamBalanceSupported,
            com.example.desporto24.enumeration.SessionPlayFormat playFormat,
            String meetingPointInstructions) {
        this(id, sport, city, gameDate, distanceKm, participantCount, maxParticipants,
                priceAmount, privateGame, status, creatorUsername, address,
                canSeeExactAddress, joined, waitlisted, waitlistPosition, gamePhoto,
                venue, audience, recurrenceSeriesId, problemType, problemDetails,
                communityId, canSharePublicly, externalConfirmedParticipants,
                registrationOpeningMode, registrationOpensAt, teamBalanceSupported,
                playFormat, meetingPointInstructions,
                com.example.desporto24.service.SessionMinimumPlayers.suggest(sport, maxParticipants));
    }
    /** Preserve all callers of the previous complete detail response. */
    public SessionDetailResponse(
            Long id, String sport, String city, Instant gameDate, Double distanceKm,
            int participantCount, int maxParticipants, BigDecimal priceAmount,
            boolean privateGame, SessionStatus status, String creatorUsername,
            String address, boolean canSeeExactAddress, boolean joined,
            boolean waitlisted, Integer waitlistPosition, String gamePhoto,
            VenueResponse venue, SessionAudience audience, String recurrenceSeriesId,
            SessionProblemType problemType, String problemDetails, Long communityId,
            boolean canSharePublicly, int externalConfirmedParticipants,
            com.example.desporto24.enumeration.RegistrationOpeningMode registrationOpeningMode,
            Instant registrationOpensAt, boolean teamBalanceSupported,
            com.example.desporto24.enumeration.SessionPlayFormat playFormat) {
        this(id, sport, city, gameDate, distanceKm, participantCount, maxParticipants,
                priceAmount, privateGame, status, creatorUsername, address,
                canSeeExactAddress, joined, waitlisted, waitlistPosition, gamePhoto,
                venue, audience, recurrenceSeriesId, problemType, problemDetails,
                communityId, canSharePublicly, externalConfirmedParticipants,
                registrationOpeningMode, registrationOpensAt,
                teamBalanceSupported, playFormat, null);
    }

    // Retain the previous full response constructor for integrations and tests.
    public SessionDetailResponse(
            Long id, String sport, String city, Instant gameDate, Double distanceKm,
            int participantCount, int maxParticipants, BigDecimal priceAmount,
            boolean privateGame, SessionStatus status, String creatorUsername,
            String address, boolean canSeeExactAddress, boolean joined,
            boolean waitlisted, Integer waitlistPosition, String gamePhoto,
            VenueResponse venue, SessionAudience audience, String recurrenceSeriesId,
            SessionProblemType problemType, String problemDetails, Long communityId,
            boolean canSharePublicly, int externalConfirmedParticipants,
            com.example.desporto24.enumeration.RegistrationOpeningMode registrationOpeningMode,
            Instant registrationOpensAt, boolean teamBalanceSupported) {
        this(id, sport, city, gameDate, distanceKm, participantCount, maxParticipants,
                priceAmount, privateGame, status, creatorUsername, address,
                canSeeExactAddress, joined, waitlisted, waitlistPosition, gamePhoto,
                venue, audience, recurrenceSeriesId, problemType, problemDetails,
                communityId, canSharePublicly, externalConfirmedParticipants,
                registrationOpeningMode, registrationOpensAt, teamBalanceSupported, null);
    }

    // Compatibility with existing callers of the previous 28-argument response.
    public SessionDetailResponse(
            Long id, String sport, String city, Instant gameDate, Double distanceKm,
            int participantCount, int maxParticipants, BigDecimal priceAmount,
            boolean privateGame, SessionStatus status, String creatorUsername,
            String address, boolean canSeeExactAddress, boolean joined,
            boolean waitlisted, Integer waitlistPosition, String gamePhoto,
            VenueResponse venue, SessionAudience audience, String recurrenceSeriesId,
            SessionProblemType problemType, String problemDetails, Long communityId,
            boolean canSharePublicly, int externalConfirmedParticipants,
            com.example.desporto24.enumeration.RegistrationOpeningMode registrationOpeningMode,
            Instant registrationOpensAt) {
        this(id, sport, city, gameDate, distanceKm, participantCount, maxParticipants,
                priceAmount, privateGame, status, creatorUsername, address,
                canSeeExactAddress, joined, waitlisted, waitlistPosition, gamePhoto,
                venue, audience, recurrenceSeriesId, problemType, problemDetails,
                communityId, canSharePublicly, externalConfirmedParticipants,
                registrationOpeningMode, registrationOpensAt, false);
    }

    // Existing callers and tests retain their 26-argument constructor.
    public SessionDetailResponse(
            Long id, String sport, String city, Instant gameDate, Double distanceKm,
            int participantCount, int maxParticipants, BigDecimal priceAmount,
            boolean privateGame, SessionStatus status, String creatorUsername,
            String address, boolean canSeeExactAddress, boolean joined,
            boolean waitlisted, Integer waitlistPosition, String gamePhoto,
            VenueResponse venue, SessionAudience audience, String recurrenceSeriesId,
            SessionProblemType problemType, String problemDetails, Long communityId,
            boolean canSharePublicly, int externalConfirmedParticipants) {
        this(id, sport, city, gameDate, distanceKm, participantCount, maxParticipants,
                priceAmount, privateGame, status, creatorUsername, address,
                canSeeExactAddress, joined, waitlisted, waitlistPosition, gamePhoto,
                venue, audience, recurrenceSeriesId, problemType, problemDetails,
                communityId, canSharePublicly, externalConfirmedParticipants,
                com.example.desporto24.enumeration.RegistrationOpeningMode.IMMEDIATE, null);
    }
    // Keep existing constructors for callers using the previous response shape.
    public SessionDetailResponse(
            Long id,
            String sport,
            String city,
            Instant gameDate,
            Double distanceKm,
            int participantCount,
            int maxParticipants,
            BigDecimal priceAmount,
            boolean privateGame,
            SessionStatus status,
            String creatorUsername,
            String address,
            boolean canSeeExactAddress,
            boolean joined,
            boolean waitlisted,
            Integer waitlistPosition,
            String gamePhoto,
            VenueResponse venue,
            SessionAudience audience,
            String recurrenceSeriesId,
            SessionProblemType problemType,
            String problemDetails,
            Long communityId,
            boolean canSharePublicly) {
        this(id, sport, city, gameDate, distanceKm, participantCount, maxParticipants,
                priceAmount, privateGame, status, creatorUsername, address, canSeeExactAddress,
                joined, waitlisted, waitlistPosition, gamePhoto, venue, audience,
                recurrenceSeriesId, problemType, problemDetails, communityId,
                canSharePublicly, 0);
    }

    public SessionDetailResponse(
            Long id,
            String sport,
            String city,
            Instant gameDate,
            Double distanceKm,
            int participantCount,
            int maxParticipants,
            BigDecimal priceAmount,
            boolean privateGame,
            SessionStatus status,
            String creatorUsername,
            String address,
            boolean canSeeExactAddress,
            boolean joined,
            boolean waitlisted,
            Integer waitlistPosition,
            String gamePhoto) {
        this(
                id,
                sport,
                city,
                gameDate,
                distanceKm,
                participantCount,
                maxParticipants,
                priceAmount,
                privateGame,
                status,
                creatorUsername,
                address,
                canSeeExactAddress,
                joined,
                waitlisted,
                waitlistPosition,
                gamePhoto,
                null,
                null,
                null,
                null,
                null,
                null,
                false
        );
    }
}
