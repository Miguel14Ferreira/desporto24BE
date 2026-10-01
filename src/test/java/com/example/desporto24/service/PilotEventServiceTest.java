package com.example.desporto24.service;

import com.example.desporto24.enumeration.PilotEventType;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.analytics.PilotEvent;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.analytics.PilotEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PilotEventServiceTest {

    @Mock
    private PilotEventRepository pilotEventRepository;

    @Mock
    private SessionRepository sessionRepository;

    private PilotEventService service;

    @BeforeEach
    void setUp() {
        service = new PilotEventService(pilotEventRepository, sessionRepository);
    }

    @Test
    void returnsCountsForEveryPilotEventType() {
        when(pilotEventRepository.countByEventType(PilotEventType.ACCESS)).thenReturn(200L);
        when(pilotEventRepository.countByEventType(PilotEventType.REGISTRATION_COMPLETED)).thenReturn(100L);
        when(pilotEventRepository.countByEventType(PilotEventType.SESSION_SEARCHED)).thenReturn(70L);
        when(pilotEventRepository.countByEventType(PilotEventType.SESSION_CREATED)).thenReturn(20L);
        when(pilotEventRepository.countByEventType(PilotEventType.SESSION_JOINED)).thenReturn(60L);
        when(pilotEventRepository.countByEventType(PilotEventType.ATTENDANCE_CONFIRMED)).thenReturn(30L);
        when(pilotEventRepository.countByEventType(PilotEventType.SESSION_COMPLETED)).thenReturn(10L);
        when(pilotEventRepository.countByEventType(PilotEventType.OPERATIONAL_INTERVENTION)).thenReturn(4L);

        var counts = service.eventCounts();

        assertEquals(200L, counts.access());
        assertEquals(100L, counts.registrationCompleted());
        assertEquals(70L, counts.sessionSearched());
        assertEquals(20L, counts.sessionCreated());
        assertEquals(60L, counts.sessionJoined());
        assertEquals(30L, counts.attendanceConfirmed());
        assertEquals(10L, counts.sessionCompleted());
        assertEquals(4L, counts.operationalIntervention());
    }

    @Test
    void calculatesActivationOnlyForEligibleRegistrationsAndFirstSevenDays() {
        Instant now = Instant.now();
        PilotEvent registration1 = event(PilotEventType.REGISTRATION_COMPLETED, 1L, now.minus(Duration.ofDays(10)));
        PilotEvent registration2 = event(PilotEventType.REGISTRATION_COMPLETED, 2L, now.minus(Duration.ofDays(9)));
        PilotEvent registration3 = event(PilotEventType.REGISTRATION_COMPLETED, 3L, now.minus(Duration.ofDays(8)));
        PilotEvent registration4 = event(PilotEventType.REGISTRATION_COMPLETED, 4L, now.minus(Duration.ofDays(2)));

        when(pilotEventRepository.findByEventType(PilotEventType.REGISTRATION_COMPLETED))
                .thenReturn(List.of(registration1, registration2, registration3, registration4));
        when(pilotEventRepository.findByEventType(PilotEventType.SESSION_SEARCHED))
                .thenReturn(List.of(
                        event(PilotEventType.SESSION_SEARCHED, 1L,
                                registration1.getCreatedAt().plus(Duration.ofDays(1))),
                        event(PilotEventType.SESSION_SEARCHED, 4L,
                                registration4.getCreatedAt().plus(Duration.ofHours(1)))
                ));
        when(pilotEventRepository.findByEventType(PilotEventType.SESSION_CREATED))
                .thenReturn(List.of(event(
                        PilotEventType.SESSION_CREATED,
                        2L,
                        registration2.getCreatedAt().plus(Duration.ofDays(7))
                )));
        when(pilotEventRepository.findByEventType(PilotEventType.SESSION_JOINED))
                .thenReturn(List.of(event(
                        PilotEventType.SESSION_JOINED,
                        3L,
                        registration3.getCreatedAt().plus(Duration.ofDays(7)).plusSeconds(1)
                )));

        var activation = service.activation7d();

        assertEquals(2L, activation.activatedRegistrations());
        assertEquals(3L, activation.eligibleRegistrations());
        assertEquals(new BigDecimal("66.67"), activation.activationRatePercent());
        assertEquals(50L, activation.minimumEligibleRegistrations());
        assertFalse(activation.sampleSufficient());
    }

    @Test
    void activationRateIsUnknownBeforeAnyRegistrationHasSevenCompleteDays() {
        Instant now = Instant.now();
        when(pilotEventRepository.findByEventType(PilotEventType.REGISTRATION_COMPLETED))
                .thenReturn(List.of(event(
                        PilotEventType.REGISTRATION_COMPLETED,
                        1L,
                        now.minus(Duration.ofDays(2))
                )));
        when(pilotEventRepository.findByEventType(PilotEventType.SESSION_SEARCHED)).thenReturn(List.of());
        when(pilotEventRepository.findByEventType(PilotEventType.SESSION_CREATED)).thenReturn(List.of());
        when(pilotEventRepository.findByEventType(PilotEventType.SESSION_JOINED)).thenReturn(List.of());

        var activation = service.activation7d();

        assertEquals(0L, activation.activatedRegistrations());
        assertEquals(0L, activation.eligibleRegistrations());
        assertNull(activation.activationRatePercent());
        assertFalse(activation.sampleSufficient());
    }

    @Test
    void calculatesRepetitionOnlyForEligibleParticipantsReturningWithinThirtyDays() {
        Instant now = Instant.now();
        PilotEvent first1 = attendanceEvent(1L, 101L, now.minus(Duration.ofDays(40)));
        PilotEvent first2 = attendanceEvent(2L, 201L, now.minus(Duration.ofDays(35)));
        PilotEvent first3 = attendanceEvent(3L, 301L, now.minus(Duration.ofDays(31)));
        PilotEvent first4 = attendanceEvent(4L, 401L, now.minus(Duration.ofDays(10)));

        when(pilotEventRepository.findByEventType(PilotEventType.ATTENDANCE_CONFIRMED))
                .thenReturn(List.of(
                        first1,
                        attendanceEvent(1L, 102L, first1.getCreatedAt().plus(Duration.ofDays(10))),
                        first2,
                        attendanceEvent(2L, 202L, first2.getCreatedAt().plus(Duration.ofDays(30))),
                        first3,
                        attendanceEvent(3L, 301L, first3.getCreatedAt().plus(Duration.ofDays(1))),
                        attendanceEvent(3L, 302L, first3.getCreatedAt().plus(Duration.ofDays(30)).plusSeconds(1)),
                        first4,
                        attendanceEvent(4L, 402L, first4.getCreatedAt().plus(Duration.ofDays(1)))
                ));

        var repetition = service.repetition30d();

        assertEquals(2L, repetition.repeatedParticipants());
        assertEquals(3L, repetition.eligibleParticipants());
        assertEquals(new BigDecimal("66.67"), repetition.repetitionRatePercent());
        assertEquals(20L, repetition.minimumEligibleParticipants());
        assertFalse(repetition.sampleSufficient());
    }

    @Test
    void repetitionRateIsUnknownBeforeAnyParticipantHasThirtyCompleteDays() {
        Instant now = Instant.now();
        when(pilotEventRepository.findByEventType(PilotEventType.ATTENDANCE_CONFIRMED))
                .thenReturn(List.of(attendanceEvent(
                        1L,
                        101L,
                        now.minus(Duration.ofDays(10))
                )));

        var repetition = service.repetition30d();

        assertEquals(0L, repetition.repeatedParticipants());
        assertEquals(0L, repetition.eligibleParticipants());
        assertNull(repetition.repetitionRatePercent());
        assertFalse(repetition.sampleSufficient());
    }

    @Test
    void participationCountsUniqueConfirmedParticipantsAndCompletedSessions() {
        Instant now = Instant.now();

        when(pilotEventRepository.findByEventType(PilotEventType.ATTENDANCE_CONFIRMED))
                .thenReturn(List.of(
                        attendanceEvent(1L, 101L, now.minus(Duration.ofDays(2))),
                        attendanceEvent(1L, 102L, now.minus(Duration.ofDays(1))),
                        attendanceEvent(2L, 103L, now.minus(Duration.ofHours(12)))
                ));
        when(pilotEventRepository.findByEventType(PilotEventType.SESSION_COMPLETED))
                .thenReturn(List.of(
                        completedSessionEvent(101L, now.minus(Duration.ofDays(2))),
                        completedSessionEvent(102L, now.minus(Duration.ofDays(1))),
                        completedSessionEvent(102L, now.minus(Duration.ofHours(6)))
                ));

        var participation = service.participation();

        assertEquals(2L, participation.confirmedParticipants());
        assertEquals(2L, participation.completedSessions());
        assertEquals(30L, participation.minimumConfirmedParticipants());
        assertEquals(10L, participation.minimumCompletedSessions());
        assertFalse(participation.participantsTargetMet());
        assertFalse(participation.completedSessionsTargetMet());
    }

    @Test
    void realizationRequiresCompletionAndAtLeastTwoConfirmedParticipants() {
        Instant now = Instant.now();
        when(pilotEventRepository.findByEventType(PilotEventType.SESSION_CREATED))
                .thenReturn(List.of(
                        subjectEvent(PilotEventType.SESSION_CREATED, 101L, now.minus(Duration.ofDays(10))),
                        subjectEvent(PilotEventType.SESSION_CREATED, 102L, now.minus(Duration.ofDays(9))),
                        subjectEvent(PilotEventType.SESSION_CREATED, 103L, now.minus(Duration.ofDays(8))),
                        subjectEvent(PilotEventType.SESSION_CREATED, 104L, now.minus(Duration.ofDays(1))),
                        subjectEvent(PilotEventType.SESSION_CREATED, 105L, now.minus(Duration.ofDays(7)))
                ));
        when(sessionRepository.findAllById(anySet())).thenReturn(List.of(
                session(101L, SessionStatus.COMPLETED, now.minus(Duration.ofDays(2))),
                session(102L, SessionStatus.CANCELLED, now.minus(Duration.ofDays(1))),
                session(103L, SessionStatus.COMPLETED, now.minus(Duration.ofHours(1))),
                session(104L, SessionStatus.OPEN, now.plus(Duration.ofDays(1))),
                session(105L, SessionStatus.COMPLETED, now.minus(Duration.ofHours(2)))
        ));
        when(pilotEventRepository.findByEventType(PilotEventType.SESSION_COMPLETED))
                .thenReturn(List.of(
                        completedSessionEvent(101L, now.minus(Duration.ofDays(2))),
                        completedSessionEvent(103L, now.minus(Duration.ofHours(1))),
                        completedSessionEvent(105L, now.minus(Duration.ofHours(2)))
                ));
        when(pilotEventRepository.findByEventType(PilotEventType.ATTENDANCE_CONFIRMED))
                .thenReturn(List.of(
                        attendanceEvent(1L, 101L, now.minus(Duration.ofDays(2))),
                        attendanceEvent(2L, 101L, now.minus(Duration.ofDays(2))),
                        attendanceEvent(3L, 102L, now.minus(Duration.ofDays(1))),
                        attendanceEvent(4L, 102L, now.minus(Duration.ofDays(1))),
                        attendanceEvent(5L, 103L, now.minus(Duration.ofHours(1))),
                        attendanceEvent(6L, 105L, now.minus(Duration.ofHours(2))),
                        attendanceEvent(7L, 105L, now.minus(Duration.ofHours(2))),
                        attendanceEvent(7L, 105L, now.minus(Duration.ofHours(2)))
                ));

        var realization = service.realization();

        assertEquals(2L, realization.realizedSessions());
        assertEquals(4L, realization.eligiblePlannedSessions());
        assertEquals(new BigDecimal("50.00"), realization.realizationRatePercent());
        assertEquals(new BigDecimal("70.00"), realization.minimumRatePercent());
        assertFalse(realization.targetMet());
    }

    @Test
    void realizationRateIsUnknownBeforeAnyTrackedSessionIsEligible() {
        Instant now = Instant.now();
        when(pilotEventRepository.findByEventType(PilotEventType.SESSION_CREATED))
                .thenReturn(List.of(subjectEvent(
                        PilotEventType.SESSION_CREATED,
                        101L,
                        now.minus(Duration.ofHours(1))
                )));
        when(sessionRepository.findAllById(anySet())).thenReturn(List.of(
                session(101L, SessionStatus.OPEN, now.plus(Duration.ofDays(1)))
        ));

        var realization = service.realization();

        assertEquals(0L, realization.realizedSessions());
        assertEquals(0L, realization.eligiblePlannedSessions());
        assertNull(realization.realizationRatePercent());
        assertFalse(realization.targetMet());
    }

    @Test
    void recurringSupplyCountsOrganizersWithAtLeastTwoDistinctCreatedSessions() {
        Instant now = Instant.now();
        when(pilotEventRepository.findByEventType(PilotEventType.SESSION_CREATED))
                .thenReturn(List.of(
                        sessionCreatedEvent(1L, 101L, now.minus(Duration.ofDays(6))),
                        sessionCreatedEvent(1L, 102L, now.minus(Duration.ofDays(5))),
                        sessionCreatedEvent(1L, 102L, now.minus(Duration.ofDays(4))),
                        sessionCreatedEvent(2L, 201L, now.minus(Duration.ofDays(4))),
                        sessionCreatedEvent(2L, 202L, now.minus(Duration.ofDays(3))),
                        sessionCreatedEvent(3L, 301L, now.minus(Duration.ofDays(3))),
                        sessionCreatedEvent(3L, 302L, now.minus(Duration.ofDays(2))),
                        sessionCreatedEvent(4L, 401L, now.minus(Duration.ofDays(1)))
                ));

        var supply = service.recurringSupply();

        assertEquals(4L, supply.organizersWithCreatedSessions());
        assertEquals(3L, supply.recurringOrganizers());
        assertEquals(3L, supply.minimumRecurringOrganizers());
        assertTrue(supply.targetMet());
    }

    @Test
    void recordsOnlyFirstAccessForDevice() {
        when(pilotEventRepository.existsByEventTypeAndDeviceId(
                PilotEventType.ACCESS,
                "device-123"
        )).thenReturn(false, true);

        service.recordFirstAccess("  device-123  ");
        service.recordFirstAccess("device-123");

        ArgumentCaptor<PilotEvent> captor = ArgumentCaptor.forClass(PilotEvent.class);
        verify(pilotEventRepository, times(1)).save(captor.capture());

        PilotEvent event = captor.getValue();
        assertEquals(PilotEventType.ACCESS, event.getEventType());
        assertEquals("device-123", event.getDeviceId());
        assertNull(event.getProfile());
        assertNull(event.getSubjectId());
        assertNotNull(event.getCreatedAt());
    }

    @Test
    void recordsRegistrationOnlyOncePerProfile() {
        Profile profile = new Profile();
        profile.setId(42L);

        when(pilotEventRepository.existsByEventTypeAndProfileId(
                PilotEventType.REGISTRATION_COMPLETED,
                42L
        )).thenReturn(false, true);

        service.recordRegistrationCompleted(profile);
        service.recordRegistrationCompleted(profile);

        ArgumentCaptor<PilotEvent> captor = ArgumentCaptor.forClass(PilotEvent.class);
        verify(pilotEventRepository, times(1)).save(captor.capture());

        PilotEvent event = captor.getValue();
        assertEquals(PilotEventType.REGISTRATION_COMPLETED, event.getEventType());
        assertEquals(profile, event.getProfile());
        assertNull(event.getSubjectId());
        assertNotNull(event.getCreatedAt());
    }

    @Test
    void recordsSessionSearchOnlyOncePerProfile() {
        Profile profile = new Profile();
        profile.setId(42L);

        when(pilotEventRepository.existsByEventTypeAndProfileId(
                PilotEventType.SESSION_SEARCHED,
                42L
        )).thenReturn(false, true);

        service.recordSessionSearched(profile);
        service.recordSessionSearched(profile);

        ArgumentCaptor<PilotEvent> captor = ArgumentCaptor.forClass(PilotEvent.class);
        verify(pilotEventRepository, times(1)).save(captor.capture());

        PilotEvent event = captor.getValue();
        assertEquals(PilotEventType.SESSION_SEARCHED, event.getEventType());
        assertEquals(profile, event.getProfile());
        assertNull(event.getSubjectId());
        assertNotNull(event.getCreatedAt());
    }

    @Test
    void recordsSessionCreationOnlyOncePerSession() {
        Profile creator = new Profile();
        creator.setId(42L);
        Profile anotherProfile = new Profile();
        anotherProfile.setId(43L);

        when(pilotEventRepository.existsByEventTypeAndSubjectId(
                PilotEventType.SESSION_CREATED,
                99L
        )).thenReturn(false, true);

        service.recordSessionCreated(creator, 99L);
        service.recordSessionCreated(anotherProfile, 99L);

        ArgumentCaptor<PilotEvent> captor = ArgumentCaptor.forClass(PilotEvent.class);
        verify(pilotEventRepository, times(1)).save(captor.capture());

        PilotEvent event = captor.getValue();
        assertEquals(PilotEventType.SESSION_CREATED, event.getEventType());
        assertEquals(creator, event.getProfile());
        assertEquals(99L, event.getSubjectId());
        assertNotNull(event.getCreatedAt());
    }

    @Test
    void recordsSessionJoinOnlyOncePerProfileAndSession() {
        Profile profile = new Profile();
        profile.setId(42L);

        when(pilotEventRepository.existsByEventTypeAndProfileIdAndSubjectId(
                PilotEventType.SESSION_JOINED,
                42L,
                99L
        )).thenReturn(false, true);

        service.recordSessionJoined(profile, 99L);
        service.recordSessionJoined(profile, 99L);

        ArgumentCaptor<PilotEvent> captor = ArgumentCaptor.forClass(PilotEvent.class);
        verify(pilotEventRepository, times(1)).save(captor.capture());

        PilotEvent event = captor.getValue();
        assertEquals(PilotEventType.SESSION_JOINED, event.getEventType());
        assertEquals(profile, event.getProfile());
        assertEquals(99L, event.getSubjectId());
        assertNotNull(event.getCreatedAt());
    }

    @Test
    void recordsAttendanceOnlyOncePerProfileAndSession() {
        Profile profile = new Profile();
        profile.setId(42L);

        when(pilotEventRepository.existsByEventTypeAndProfileIdAndSubjectId(
                PilotEventType.ATTENDANCE_CONFIRMED,
                42L,
                99L
        )).thenReturn(false, true);

        service.recordAttendanceConfirmed(profile, 99L);
        service.recordAttendanceConfirmed(profile, 99L);

        ArgumentCaptor<PilotEvent> captor = ArgumentCaptor.forClass(PilotEvent.class);
        verify(pilotEventRepository, times(1)).save(captor.capture());

        PilotEvent event = captor.getValue();
        assertEquals(PilotEventType.ATTENDANCE_CONFIRMED, event.getEventType());
        assertEquals(profile, event.getProfile());
        assertEquals(99L, event.getSubjectId());
        assertNotNull(event.getCreatedAt());
    }

    @Test
    void recordsOperationalInterventionOnlyOncePerSession() {
        when(sessionRepository.existsById(99L)).thenReturn(true);
        when(pilotEventRepository.existsByEventTypeAndSubjectId(
                PilotEventType.OPERATIONAL_INTERVENTION,
                99L
        )).thenReturn(false, true);

        service.recordOperationalIntervention(99L);
        service.recordOperationalIntervention(99L);

        ArgumentCaptor<PilotEvent> captor = ArgumentCaptor.forClass(PilotEvent.class);
        verify(pilotEventRepository, times(1)).save(captor.capture());

        PilotEvent event = captor.getValue();
        assertEquals(PilotEventType.OPERATIONAL_INTERVENTION, event.getEventType());
        assertNull(event.getProfile());
        assertEquals(99L, event.getSubjectId());
        assertNotNull(event.getCreatedAt());
    }

    @Test
    void operationalInterventionRejectsUnknownSession() {
        when(sessionRepository.existsById(99L)).thenReturn(false);

        assertThrows(
                com.example.desporto24.exception.SessionNotFound.class,
                () -> service.recordOperationalIntervention(99L)
        );

        verify(pilotEventRepository, never()).save(any());
    }

    @Test
    void recordsSessionCompletionOnlyOncePerSession() {
        Profile creator = new Profile();
        creator.setId(42L);
        Profile anotherProfile = new Profile();
        anotherProfile.setId(43L);

        when(pilotEventRepository.existsByEventTypeAndSubjectId(
                PilotEventType.SESSION_COMPLETED,
                99L
        )).thenReturn(false, true);

        service.recordSessionCompleted(creator, 99L);
        service.recordSessionCompleted(anotherProfile, 99L);

        ArgumentCaptor<PilotEvent> captor = ArgumentCaptor.forClass(PilotEvent.class);
        verify(pilotEventRepository, times(1)).save(captor.capture());

        PilotEvent event = captor.getValue();
        assertEquals(PilotEventType.SESSION_COMPLETED, event.getEventType());
        assertEquals(creator, event.getProfile());
        assertEquals(99L, event.getSubjectId());
        assertNotNull(event.getCreatedAt());
    }

    @Test
    void recordsBusinessEventFromTrustedDomainService() {
        Profile profile = new Profile();
        profile.setId(42L);

        service.recordBusinessEvent(
                PilotEventType.SESSION_JOINED,
                profile,
                99L
        );

        ArgumentCaptor<PilotEvent> captor = ArgumentCaptor.forClass(PilotEvent.class);
        verify(pilotEventRepository).save(captor.capture());

        PilotEvent event = captor.getValue();
        assertEquals(PilotEventType.SESSION_JOINED, event.getEventType());
        assertEquals(profile, event.getProfile());
        assertEquals(99L, event.getSubjectId());
        assertNotNull(event.getCreatedAt());
    }

    private Session session(Long id, SessionStatus status, Instant gameDate) {
        Session session = new Session();
        session.setId(id);
        session.setStatus(status);
        session.setGameDate(Date.from(gameDate));
        return session;
    }

    private PilotEvent subjectEvent(PilotEventType type, Long subjectId, Instant createdAt) {
        return PilotEvent.builder()
                .eventType(type)
                .subjectId(subjectId)
                .createdAt(createdAt)
                .build();
    }

    private PilotEvent completedSessionEvent(Long sessionId, Instant createdAt) {
        return PilotEvent.builder()
                .eventType(PilotEventType.SESSION_COMPLETED)
                .subjectId(sessionId)
                .createdAt(createdAt)
                .build();
    }

    private PilotEvent sessionCreatedEvent(Long profileId, Long sessionId, Instant createdAt) {
        Profile profile = new Profile();
        profile.setId(profileId);
        return PilotEvent.builder()
                .eventType(PilotEventType.SESSION_CREATED)
                .profile(profile)
                .subjectId(sessionId)
                .createdAt(createdAt)
                .build();
    }

    private PilotEvent attendanceEvent(Long profileId, Long sessionId, Instant createdAt) {
        Profile profile = new Profile();
        profile.setId(profileId);
        return PilotEvent.builder()
                .eventType(PilotEventType.ATTENDANCE_CONFIRMED)
                .profile(profile)
                .subjectId(sessionId)
                .createdAt(createdAt)
                .build();
    }

    private PilotEvent event(PilotEventType type, Long profileId, Instant createdAt) {
        Profile profile = new Profile();
        profile.setId(profileId);
        return PilotEvent.builder()
                .eventType(type)
                .profile(profile)
                .createdAt(createdAt)
                .build();
    }

    @Test
    void accessCannotBeRecordedAsBusinessEvent() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.recordBusinessEvent(PilotEventType.ACCESS, new Profile(), null)
        );

        verify(pilotEventRepository, never()).save(any());
    }
}
