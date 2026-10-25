package com.example.desporto24.service;

import com.example.desporto24.enumeration.RegistrationOpeningMode;
import com.example.desporto24.enumeration.SessionRecurrence;
import com.example.desporto24.model.dto.session.CreateSessionRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommunitySessionScheduleTest {
    private static final ZoneId LISBON = ZoneId.of("Europe/Lisbon");
    private static final Instant NOW = Instant.parse("2027-03-20T12:00:00Z");
    private static final Instant FIRST = Instant.parse("2027-03-24T20:00:00Z");
    private static final Instant SECOND = Instant.parse("2027-03-26T20:00:00Z");

    private CreateSessionRequest request(int weeks, List<Instant> extra,
                                         RegistrationOpeningMode openingMode) {
        return new CreateSessionRequest("Futebol", FIRST, "Lisboa", "Rua do Campo",
                10, BigDecimal.ZERO, false, null, null, null, 7L, null,
                SessionRecurrence.WEEKLY, weeks, 0, openingMode,
                openingMode == RegistrationOpeningMode.SCHEDULED
                        ? Instant.parse("2027-03-23T12:00:00Z") : null,
                extra, LISBON.getId());
    }

    @Test
    void twoPerWeekOverFourWeeksCreatesEightOrderedIndependentOccurrences() {
        var plan = CommunitySessionSchedule.expand(
                request(4, List.of(SECOND), RegistrationOpeningMode.MANUAL), NOW);
        assertEquals(8, plan.size());
        assertEquals(FIRST, plan.get(0).kickoff());
        assertEquals(SECOND, plan.get(1).kickoff());
        assertEquals("20:00", plan.get(2).kickoff().atZone(LISBON).toLocalTime().toString());
        assertEquals("2027-03-31", plan.get(2).kickoff()
                .atZone(LISBON).toLocalDate().toString());
        assertTrue(plan.stream().allMatch(slot -> slot.registrationOpensAt() == null));
    }

    @Test
    void threePerWeekCreatesTwelveAndRetainsLocalClockAcrossDst() {
        var plan = CommunitySessionSchedule.expand(request(4, List.of(
                Instant.parse("2027-03-25T20:00:00Z"), SECOND),
                RegistrationOpeningMode.IMMEDIATE), NOW);
        assertEquals(12, plan.size());
        assertEquals("20:00", plan.get(3).kickoff().atZone(LISBON).toLocalTime().toString());
        assertEquals("20:00", plan.get(4).kickoff().atZone(LISBON).toLocalTime().toString());
        assertEquals("20:00", plan.get(5).kickoff().atZone(LISBON).toLocalTime().toString());
    }

    @Test
    void scheduledOpeningIsRelativeToEachGameAndRespectsLocalClockAfterDst() {
        var plan = CommunitySessionSchedule.expand(request(2, List.of(SECOND),
                RegistrationOpeningMode.SCHEDULED), NOW);
        assertEquals(4, plan.size());
        assertEquals("2027-03-23T12:00:00Z", plan.get(0).registrationOpensAt().toString());
        assertEquals("2027-03-25T12:00:00Z", plan.get(1).registrationOpensAt().toString());
        assertEquals("12:00", plan.get(2).registrationOpensAt()
                .atZone(LISBON).toLocalTime().toString());
        assertTrue(plan.stream().allMatch(slot ->
                slot.registrationOpensAt().isBefore(slot.kickoff())));
    }

    @Test
    void acceptsLegacyOnePerWeekRequestWithoutNewFields() {
        var old = new CreateSessionRequest("Futebol", FIRST, "Lisboa", "Campo 1",
                10, BigDecimal.ZERO, false, null, null, null, 7L, null,
                SessionRecurrence.WEEKLY, 4, 0,
                RegistrationOpeningMode.IMMEDIATE, null);
        assertEquals(4, CommunitySessionSchedule.expand(old, NOW).size());
    }

    @Test
    void rejectsOverLimitBeforeAnyDatabaseWrite() {
        var error = assertThrows(IllegalArgumentException.class, () ->
                CommunitySessionSchedule.expand(request(27, List.of(SECOND),
                        RegistrationOpeningMode.IMMEDIATE), NOW));
        assertTrue(error.getMessage().contains("52 sessões"));
    }

    @Test
    void rejectsAdditionalSlotOutsideFirstWeekAndDuplicates() {
        var afterWeek = FIRST.plusSeconds(8 * 24 * 3600);
        assertThrows(IllegalArgumentException.class, () ->
                CommunitySessionSchedule.expand(request(2, List.of(afterWeek),
                        RegistrationOpeningMode.MANUAL), NOW));
        assertThrows(IllegalArgumentException.class, () ->
                CommunitySessionSchedule.expand(request(2, List.of(SECOND, SECOND),
                        RegistrationOpeningMode.MANUAL), NOW));
    }

    @Test
    void rejectsNonWeeklyExtrasAndBadTimezone() {
        var weekly = request(2, List.of(SECOND), RegistrationOpeningMode.MANUAL);
        var biweekly = new CreateSessionRequest(
                weekly.sport(), weekly.gameDate(), weekly.city(), weekly.address(),
                weekly.maxParticipants(), weekly.priceAmount(), weekly.privateGame(),
                weekly.password(), weekly.latitude(), weekly.longitude(),
                weekly.communityId(), weekly.venueId(), SessionRecurrence.BIWEEKLY,
                weekly.recurrenceOccurrences(), weekly.externalConfirmedParticipants(),
                weekly.registrationOpeningMode(), weekly.registrationOpensAt(),
                weekly.additionalWeeklyGameDates(), weekly.recurrenceTimeZone());
        assertThrows(IllegalArgumentException.class,
                () -> CommunitySessionSchedule.expand(biweekly, NOW));
        var invalidZone = new CreateSessionRequest(
                weekly.sport(), weekly.gameDate(), weekly.city(), weekly.address(),
                weekly.maxParticipants(), weekly.priceAmount(), weekly.privateGame(),
                weekly.password(), weekly.latitude(), weekly.longitude(),
                weekly.communityId(), weekly.venueId(), SessionRecurrence.WEEKLY,
                weekly.recurrenceOccurrences(), weekly.externalConfirmedParticipants(),
                weekly.registrationOpeningMode(), weekly.registrationOpensAt(),
                weekly.additionalWeeklyGameDates(), "Invalid/Zone");
        assertThrows(IllegalArgumentException.class,
                () -> CommunitySessionSchedule.expand(invalidZone, NOW));
    }
}
