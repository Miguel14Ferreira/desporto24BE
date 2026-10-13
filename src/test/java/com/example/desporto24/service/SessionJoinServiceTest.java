package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.JoinState;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.session.JoinSessionRequest;
import com.example.desporto24.model.dto.session.JoinSessionResponse;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.analytics.PilotEventRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionOrganizerExclusionRepository;
import com.example.desporto24.model.session.SessionOrganizerExclusion;
import com.example.desporto24.repository.session.SessionWaitlistRepository;
import com.example.desporto24.security.PasswordHashService;
import com.example.desporto24.configuration.ReliabilityPolicyProperties;
import com.example.desporto24.service.impl.SessionServiceImpl;
import com.example.desporto24.service.interfaces.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:session-join;MODE=LEGACY;NON_KEYWORDS=SESSION;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.hikari.schema=PUBLIC"
})
@Import({
        SessionServiceImpl.class,
        SessionViewMapper.class,
        VenueService.class,
        SessionDistanceCalculator.class,
        WaitlistPromotionService.class,
        SessionNotificationService.class,
        PilotEventService.class,
        ReliabilityService.class,
        ReliabilityPolicyProperties.class,
        PasswordHashService.class,
        SessionJoinServiceTest.PasswordConfig.class
})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class SessionJoinServiceTest {

    @TestConfiguration
    static class PasswordConfig {
        @Bean
        BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired private SessionService sessionService;
    @Autowired private ProfileRepository profileRepository;
    @Autowired private SessionRepository sessionRepository;
    @Autowired private SessionParticipantRepository participantRepository;
    @Autowired private SessionOrganizerExclusionRepository exclusionRepository;
    @Autowired private SessionWaitlistRepository waitlistRepository;
    @Autowired private PilotEventRepository pilotEventRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    private Profile creator;

    @BeforeEach
    void setUp() {
        pilotEventRepository.deleteAll();
        waitlistRepository.deleteAll();
        participantRepository.deleteAll();
        exclusionRepository.deleteAll();
        sessionRepository.deleteAll();
        profileRepository.deleteAll();

        creator = createProfile("creator");
    }

    @Test
    void publicSessionWithCapacityCreatesParticipant() {
        Session session = createSession(false, null, 2);
        Profile joiner = createProfile("joiner");

        JoinSessionResponse response = sessionService.join(
                session.getId(), new JoinSessionRequest(null), joiner.getUsername());

        assertEquals(JoinState.PARTICIPANT, response.state());
        assertNull(response.waitlistPosition());
        assertTrue(participantRepository.existsBySessionIdAndProfileIdAndState(
                session.getId(), joiner.getId(), ParticipantState.ACTIVE));
        assertTrue(pilotEventRepository.existsByEventTypeAndProfileIdAndSubjectId(
                com.example.desporto24.enumeration.PilotEventType.SESSION_JOINED,
                joiner.getId(),
                session.getId()));
        assertEquals(SessionStatus.FULL, sessionRepository.findById(session.getId()).orElseThrow().getStatus());
    }

    @Test
    void excludedPlayerCannotJoinOrWaitlistOnAnyFutureSessionFromSameOrganizer() {
        Session session = createSession(false, null, 2);
        Profile excluded = createProfile("excluded");
        exclusionRepository.saveAndFlush(SessionOrganizerExclusion.builder()
                .organizer(creator).excluded(excluded).createdAt(Instant.now()).build());

        assertThrows(AccessDeniedException.class, () -> sessionService.join(
                session.getId(), new JoinSessionRequest(null), excluded.getUsername()));
        assertFalse(participantRepository.existsBySessionIdAndProfileIdAndState(
                session.getId(), excluded.getId(), ParticipantState.ACTIVE));
        assertTrue(waitlistRepository.findBySessionIdAndProfileId(
                session.getId(), excluded.getId()).isEmpty());

        Session next = createSession(false, null, 3);
        assertThrows(AccessDeniedException.class, () -> sessionService.join(
                next.getId(), new JoinSessionRequest(null), excluded.getUsername()));
    }

    @Test
    void anAlreadyEnrolledPlayerIsNotExpelledByExclusion() {
        Session session = createSession(false, null, 3);
        Profile player = createProfile("alreadyJoined");
        sessionService.join(session.getId(), new JoinSessionRequest(null), player.getUsername());
        exclusionRepository.saveAndFlush(SessionOrganizerExclusion.builder()
                .organizer(creator).excluded(player).createdAt(Instant.now()).build());

        assertEquals(JoinState.PARTICIPANT, sessionService.join(
                session.getId(), new JoinSessionRequest(null), player.getUsername()).state());
        assertEquals(2, participantRepository.countBySessionIdAndState(
                session.getId(), ParticipantState.ACTIVE));
    }

    @Test
    void sessionRemainsOpenWhenCapacityStillExists() {
        Session session = createSession(false, null, 3);
        Profile joiner = createProfile("joiner");

        JoinSessionResponse response = sessionService.join(
                session.getId(), new JoinSessionRequest(null), joiner.getUsername());

        assertEquals(JoinState.PARTICIPANT, response.state());
        assertEquals(SessionStatus.OPEN, sessionRepository.findById(session.getId()).orElseThrow().getStatus());
    }

    @Test
    void privateSessionWithCorrectPasswordCreatesParticipant() {
        Session session = createSession(true, passwordEncoder.encode("segredo"), 2);
        Profile joiner = createProfile("joiner");

        JoinSessionResponse response = sessionService.join(
                session.getId(), new JoinSessionRequest("segredo"), joiner.getUsername());

        assertEquals(JoinState.PARTICIPANT, response.state());
    }

    @Test
    void privateSessionWithWrongPasswordCreatesNothing() {
        Session session = createSession(true, passwordEncoder.encode("segredo"), 2);
        Profile joiner = createProfile("joiner");

        assertThrows(BadCredentialsException.class, () -> sessionService.join(
                session.getId(), new JoinSessionRequest("errada"), joiner.getUsername()));

        assertFalse(participantRepository.existsBySessionIdAndProfileIdAndState(
                session.getId(), joiner.getId(), ParticipantState.ACTIVE));
        assertTrue(waitlistRepository.findBySessionIdAndProfileId(
                session.getId(), joiner.getId()).isEmpty());
    }

    @Test
    void duplicateJoinIsIdempotent() {
        Session session = createSession(false, null, 3);
        Profile joiner = createProfile("joiner");

        JoinSessionResponse first = sessionService.join(
                session.getId(), new JoinSessionRequest(null), joiner.getUsername());
        JoinSessionResponse second = sessionService.join(
                session.getId(), new JoinSessionRequest(null), joiner.getUsername());

        assertEquals(JoinState.PARTICIPANT, first.state());
        assertEquals(JoinState.PARTICIPANT, second.state());
        assertEquals(2L, participantRepository.countBySessionIdAndState(
                session.getId(), ParticipantState.ACTIVE));
    }

    @Test
    void externalConfirmationsLeaveExactlyTwoPlacesForAppRegistrations() {
        Session session = createSession(false, null, 10);
        // The organiser already occupies one active seat in the app.
        session.setExternalConfirmedParticipants(7);
        sessionRepository.saveAndFlush(session);

        Profile first = createProfile("firstExternal");
        Profile second = createProfile("secondExternal");
        Profile third = createProfile("thirdExternal");

        assertEquals(8, SessionCapacity.totalConfirmed(session, 1L));
        assertEquals(2, SessionCapacity.remainingPlaces(session, 1L));

        assertEquals(JoinState.PARTICIPANT, sessionService.join(
                session.getId(), new JoinSessionRequest(null), first.getUsername()).state());
        assertEquals(JoinState.PARTICIPANT, sessionService.join(
                session.getId(), new JoinSessionRequest(null), second.getUsername()).state());
        assertEquals(JoinState.WAITLISTED, sessionService.join(
                session.getId(), new JoinSessionRequest(null), third.getUsername()).state());

        assertEquals(3L, participantRepository.countBySessionIdAndState(
                session.getId(), ParticipantState.ACTIVE));
        assertEquals(10, SessionCapacity.totalConfirmed(session, 3L));
        assertEquals(SessionStatus.FULL,
                sessionRepository.findById(session.getId()).orElseThrow().getStatus());
    }

    @Test
    void creatorCanCorrectExternalConfirmationsWithoutAddingProfiles() {
        Session session = createSession(false, null, 10);

        var detail = sessionService.updateExternalConfirmations(
                session.getId(), 7, creator.getUsername());

        assertEquals(7, detail.externalConfirmedParticipants());
        assertEquals(8, detail.participantCount());
        assertEquals(SessionStatus.OPEN, detail.status());
        assertEquals(7, sessionRepository.findById(session.getId())
                .orElseThrow().getExternalConfirmedParticipants());
        assertEquals(1, participantRepository.countBySessionIdAndState(
                session.getId(), ParticipantState.ACTIVE));
    }

    @Test
    void externalConfirmationsCannotDisplaceRegisteredMembers() {
        Session session = createSession(false, null, 3);
        Profile joiner = createProfile("playerRegistered");
        sessionService.join(session.getId(), new JoinSessionRequest(null), joiner.getUsername());

        assertThrows(IllegalArgumentException.class, () -> sessionService
                .updateExternalConfirmations(session.getId(), 2, creator.getUsername()));

        assertEquals(0, sessionRepository.findById(session.getId())
                .orElseThrow().getExternalConfirmedParticipants());
    }

    @Test
    void onlyCreatorCanChangeExternalConfirmationsOnUpcomingSessions() {
        Session session = createSession(false, null, 10);
        Profile other = createProfile("notCreator");
        assertThrows(AccessDeniedException.class, () -> sessionService
                .updateExternalConfirmations(session.getId(), 7, other.getUsername()));

        session.setStatus(SessionStatus.CANCELLED);
        sessionRepository.saveAndFlush(session);
        assertThrows(IllegalStateException.class, () -> sessionService
                .updateExternalConfirmations(session.getId(), 7, creator.getUsername()));
    }

    @Test
    void reducingExternalCountPromotesWaitlistedPlayersIntoVacatedPlaces() {
        Session session = createSession(false, null, 10);
        sessionService.updateExternalConfirmations(session.getId(), 9, creator.getUsername());
        Profile first = createProfile("queueFirst");
        Profile second = createProfile("queueSecond");
        assertEquals(JoinState.WAITLISTED, sessionService.join(
                session.getId(), new JoinSessionRequest(null), first.getUsername()).state());
        assertEquals(JoinState.WAITLISTED, sessionService.join(
                session.getId(), new JoinSessionRequest(null), second.getUsername()).state());

        var updated = sessionService.updateExternalConfirmations(
                session.getId(), 7, creator.getUsername());

        assertEquals(7, updated.externalConfirmedParticipants());
        assertEquals(10, updated.participantCount());
        assertEquals(SessionStatus.FULL, updated.status());
        assertEquals(3L, participantRepository.countBySessionIdAndState(
                session.getId(), ParticipantState.ACTIVE));
        assertTrue(waitlistRepository.findBySessionIdAndProfileId(
                session.getId(), first.getId()).isEmpty());
        assertTrue(waitlistRepository.findBySessionIdAndProfileId(
                session.getId(), second.getId()).isEmpty());
    }

    @Test
    void fullSessionPlacesUserOnWaitlist() {
        Session session = createSession(false, null, 1);
        Profile joiner = createProfile("joiner");

        JoinSessionResponse response = sessionService.join(
                session.getId(), new JoinSessionRequest(null), joiner.getUsername());

        assertEquals(JoinState.WAITLISTED, response.state());
        assertEquals(1, response.waitlistPosition());
        assertTrue(waitlistRepository.findBySessionIdAndProfileId(
                session.getId(), joiner.getId()).isPresent());
        assertFalse(pilotEventRepository.existsByEventTypeAndProfileIdAndSubjectId(
                com.example.desporto24.enumeration.PilotEventType.SESSION_JOINED,
                joiner.getId(),
                session.getId()));
    }

    @Test
    void waitlistPositionsFollowInsertionOrderWithinSamePriorityTier() {
        Session session = createSession(false, null, 1);
        Profile first = createProfile("first");
        Profile second = createProfile("second");

        JoinSessionResponse firstResult = sessionService.join(
                session.getId(), new JoinSessionRequest(null), first.getUsername());
        JoinSessionResponse secondResult = sessionService.join(
                session.getId(), new JoinSessionRequest(null), second.getUsername());

        assertEquals(JoinState.WAITLISTED, firstResult.state());
        assertEquals(1, firstResult.waitlistPosition());
        assertEquals(JoinState.WAITLISTED, secondResult.state());
        assertEquals(2, secondResult.waitlistPosition());
        assertEquals(SessionStatus.FULL, sessionRepository.findById(session.getId()).orElseThrow().getStatus());
    }

    @Test
    void reliabilityScoreAtRestrictionThresholdBlocksJoin() {
        Session session = createSession(false, null, 3);
        Profile joiner = createProfile("restricted");
        recordNoShows(joiner, 4);

        assertThrows(AccessDeniedException.class, () -> sessionService.join(
                session.getId(), new JoinSessionRequest(null), joiner.getUsername()));

        assertFalse(participantRepository.existsBySessionIdAndProfileIdAndState(
                session.getId(), joiner.getId(), ParticipantState.ACTIVE));
        assertTrue(waitlistRepository.findBySessionIdAndProfileId(
                session.getId(), joiner.getId()).isEmpty());
    }

    @Test
    void reliabilityScoreAtReducedPriorityThresholdCreatesTierOneWaitlistEntry() {
        Session session = createSession(false, null, 1);
        Profile joiner = createProfile("reduced");
        recordNoShows(joiner, 3);

        JoinSessionResponse response = sessionService.join(
                session.getId(), new JoinSessionRequest(null), joiner.getUsername());

        assertEquals(JoinState.WAITLISTED, response.state());
        assertEquals(1,
                waitlistRepository.findBySessionIdAndProfileId(session.getId(), joiner.getId())
                        .orElseThrow()
                        .getPriorityTier());
    }

    @Test
    void twoConcurrentJoinsForLastSlotProduceOneParticipantAndOneWaitlistedUser() throws Exception {
        Session session = createSession(false, null, 2);
        Profile first = createProfile("first");
        Profile second = createProfile("second");

        ExecutorService pool = Executors.newFixedThreadPool(2);
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        try {
            Future<JoinSessionResponse> firstResult = pool.submit(() -> {
                ready.countDown();
                start.await();
                return sessionService.join(session.getId(), new JoinSessionRequest(null), first.getUsername());
            });
            Future<JoinSessionResponse> secondResult = pool.submit(() -> {
                ready.countDown();
                start.await();
                return sessionService.join(session.getId(), new JoinSessionRequest(null), second.getUsername());
            });

            ready.await();
            start.countDown();

            List<JoinState> states = List.of(firstResult.get().state(), secondResult.get().state());

            assertEquals(1, states.stream().filter(s -> s == JoinState.PARTICIPANT).count());
            assertEquals(1, states.stream().filter(s -> s == JoinState.WAITLISTED).count());
            assertEquals(2L, participantRepository.countBySessionIdAndState(
                    session.getId(), ParticipantState.ACTIVE));
            assertEquals(1, waitlistRepository.findBySessionIdOrderByPriorityTierAscCreatedAtAsc(
                    session.getId()).size());
        } finally {
            pool.shutdownNow();
        }
    }

    private void recordNoShows(Profile profile, int count) {
        for (int i = 1; i <= count; i++) {
            Instant eventAt = Instant.now().minusSeconds(i * 24L * 60L * 60L);

            Session past = new Session();
            past.setProfile(creator);
            past.setUserId(creator.getUserId());
            past.setSport("Futebol");
            past.setCity("Cacem");
            past.setAddress("Rua Historial " + i);
            past.setGameDate(Date.from(eventAt));
            past.setMaxParticipants(2);
            past.setPrivateGame(false);
            past.setStatus(SessionStatus.COMPLETED);
            past = sessionRepository.saveAndFlush(past);

            participantRepository.saveAndFlush(SessionParticipant.builder()
                    .session(past)
                    .profile(profile)
                    .state(ParticipantState.ACTIVE)
                    .attendanceStatus(AttendanceStatus.NO_SHOW)
                    .joinedAt(eventAt.minusSeconds(3600))
                    .attendanceRecordedAt(eventAt)
                    .build());
        }
    }

    private Profile createProfile(String username) {
        Profile profile = new Profile();
        profile.setUsername(username);
        profile.setUserId(username + "-id");
        profile.setPassword(passwordEncoder.encode("pw"));
        return profileRepository.saveAndFlush(profile);
    }

    private Session createSession(boolean privateGame, String passwordHash, int maxParticipants) {
        Session session = new Session();
        session.setProfile(creator);
        session.setUsername(creator.getUsername());
        session.setUserId(creator.getUserId());
        session.setSport("Futebol");
        session.setCity("Cacem");
        session.setAddress("Rua do Campo");
        session.setGameDate(Date.from(Instant.now().plusSeconds(3600)));
        session.setMaxParticipants(maxParticipants);
        session.setPrivateGame(privateGame);
        session.setPasswordHash(passwordHash);
        session.setStatus(SessionStatus.OPEN);
        session.setLatitude(38.766);
        session.setLongitude(-9.297);
        session = sessionRepository.saveAndFlush(session);

        participantRepository.saveAndFlush(SessionParticipant.builder()
                .session(session)
                .profile(creator)
                .state(ParticipantState.ACTIVE)
                .attendanceStatus(AttendanceStatus.PENDING)
                .joinedAt(Instant.now())
                .build());

        return session;
    }
}
