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
    @Autowired private SessionWaitlistRepository waitlistRepository;
    @Autowired private PilotEventRepository pilotEventRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    private Profile creator;

    @BeforeEach
    void setUp() {
        pilotEventRepository.deleteAll();
        waitlistRepository.deleteAll();
        participantRepository.deleteAll();
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
