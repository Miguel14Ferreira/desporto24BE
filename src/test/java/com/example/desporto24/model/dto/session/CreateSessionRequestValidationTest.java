package com.example.desporto24.model.dto.session;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import com.example.desporto24.enumeration.SessionRecurrence;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateSessionRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validPortugueseLocalityIsAccepted() {
        assertTrue(validator.validate(requestWithCity("São Domingos de Rana")).isEmpty());
    }

    @Test
    void hyphenatedLocalityIsAccepted() {
        assertTrue(validator.validate(requestWithCity("A-dos-Cunhados")).isEmpty());
    }

    @Test
    void numericOnlyLocalityIsRejected() {
        assertFalse(validator.validate(requestWithCity("1500")).isEmpty());
    }

    @Test
    void localityStartingWithNumberIsRejected() {
        assertFalse(validator.validate(requestWithCity("1500 Lisboa")).isEmpty());
    }

    @Test
    void blankLocalityIsRejected() {
        assertFalse(validator.validate(requestWithCity("")).isEmpty());
    }

    @Test
    void negativeExternalConfirmedParticipantsAreRejected() {
        assertFalse(validator.validate(requestWithExternal(-1)).isEmpty());
    }

    @Test
    void externalConfirmedParticipantsWithinRangeAreAccepted() {
        assertTrue(validator.validate(requestWithExternal(7)).isEmpty());
    }

    @Test
    void arrivalInstructionsAreOptionalAndCappedAt240Characters() {
        CreateSessionRequest base = requestWithCity("Lisboa");
        assertTrue(validator.validate(base).isEmpty());
        CreateSessionRequest valid = withMeetingPoint(base, "Portão lateral junto à receção");
        assertTrue(validator.validate(valid).isEmpty());
        assertFalse(validator.validate(withMeetingPoint(base, "a".repeat(241))).isEmpty());
        assertFalse(validator.validate(withMeetingPoint(base, "porta\nsecreta")).isEmpty());
    }

    private CreateSessionRequest withMeetingPoint(CreateSessionRequest r, String note) {
        return new CreateSessionRequest(r.sport(), r.gameDate(), r.city(), r.address(),
                r.maxParticipants(), r.priceAmount(), r.privateGame(), r.password(),
                r.latitude(), r.longitude(), r.communityId(), r.venueId(), r.recurrence(),
                r.recurrenceOccurrences(), r.externalConfirmedParticipants(),
                r.registrationOpeningMode(), r.registrationOpensAt(),
                r.additionalWeeklyGameDates(), r.recurrenceTimeZone(), r.playFormat(),
                r.communityShareIds(), r.seasonCompetitionCommunityId(),
                r.confirmActivateRankings(), note);
    }

    private CreateSessionRequest requestWithExternal(int external) {
        return new CreateSessionRequest(
                "Futsal", Instant.now().plusSeconds(3600), "Lisboa", "Rua de Teste 1",
                10, BigDecimal.ZERO, false, null, 38.7223, -9.1393,
                null, null, SessionRecurrence.NONE, null, external);
    }

    private CreateSessionRequest requestWithCity(String city) {
        return new CreateSessionRequest(
                "Futsal",
                Instant.now().plusSeconds(3600),
                city,
                "Rua de Teste 1",
                10,
                BigDecimal.ZERO,
                false,
                null,
                38.7223,
                -9.1393,
                null,
                null
        );
    }
}
