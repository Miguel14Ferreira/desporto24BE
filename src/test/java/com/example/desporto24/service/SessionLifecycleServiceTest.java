package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.JoinState;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.enumeration.SessionOutcome;
import com.example.desporto24.enumeration.SessionProblemType;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.session.JoinSessionRequest;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.model.session.SessionWaitlist;
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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:session-lifecycle;MODE=LEGACY;NON_KEYWORDS=SESSION;DB_CLOSE_DELAY=-1",
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
        PasswordHashService.class,
        WaitlistPromotionService.class,
        SessionNotificationService.class,
        PilotEventService.class,
        ReliabilityService.class,
        ReliabilityPolicyProperties.class,
        SessionLifecycleServiceTest.PasswordConfig.class
})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class SessionLifecycleServiceTest {

    @TestConfiguration
    static class PasswordConfig {
        @Bean BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    }

    @Autowired private SessionService service;
    @Autowired private ProfileRepository profileRepository;
    @Autowired private SessionRepository sessionRepository;
    @Autowired private SessionParticipantRepository participantRepository;
    @Autowired private SessionWaitlistRepository waitlistRepository;
    @Autowired private PilotEventRepository pilotEventRepository;

    private Profile creator;
    private Session session;

    @BeforeEach
    void setUp() {
        pilotEventRepository.deleteAll();
        waitlistRepository.deleteAll();
        participantRepository.deleteAll();
        sessionRepository.deleteAll();
        profileRepository.deleteAll();

        creator = profile("creator");
        session = session(3);
        participant(creator);
    }

    @Test
    void leavingDeactivatesMembershipAndPromotesFirstWaitlistedUser() {
        Profile leaving = profile("leaving");
        SessionParticipant leavingMembership = participant(leaving);
        Profile waiting = profile("waiting");
        waitlisted(waiting);

        service.leave(session.getId(), leaving.getUsername());

        SessionParticipant after = participantRepository.findById(leavingMembership.getId()).orElseThrow();
        assertEquals(ParticipantState.LEFT, after.getState());
        assertNotNull(after.getExitedAt());
        assertTrue(participantRepository.existsBySessionIdAndProfileIdAndState(
                session.getId(), waiting.getId(), ParticipantState.ACTIVE));
        assertTrue(pilotEventRepository.existsByEventTypeAndProfileIdAndSubjectId(
                com.example.desporto24.enumeration.PilotEventType.SESSION_JOINED,
                waiting.getId(),
                session.getId()));
        assertTrue(waitlistRepository.findBySessionIdAndProfileId(session.getId(), waiting.getId()).isEmpty());
    }

    @Test
    void waitlistedUserCanLeaveWaitlist() {
        Profile waiting = profile("waiting");
        waitlisted(waiting);

        long participantsBefore = participantRepository.count();

        service.leave(session.getId(), waiting.getUsername());

        assertTrue(
                waitlistRepository
                        .findBySessionIdAndProfileId(session.getId(), waiting.getId())
                        .isEmpty()
        );
        assertEquals(participantsBefore, participantRepository.count());
    }

    @Test
    void creatorCannotLeaveOwnSession() {
        assertThrows(AccessDeniedException.class,
                () -> service.leave(session.getId(), creator.getUsername()));
    }

    @Test
    void creatorCanListActiveParticipantsForAttendance() {
        Profile participant = profile("participant");
        SessionParticipant membership = participant(participant);

        var result = service.participants(session.getId(), creator.getUsername());

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p ->
                p.id().equals(membership.getId())
                        && p.username().equals("participant")
                        && p.attendanceStatus() == AttendanceStatus.PENDING));
    }

    @Test
    void nonCreatorCannotListParticipants() {
        Profile participant = profile("participant");
        participant(participant);

        assertThrows(AccessDeniedException.class,
                () -> service.participants(session.getId(), participant.getUsername()));
    }

    @Test
    void nonCreatorCannotRemoveParticipant() {
        Profile participant = profile("participant");
        SessionParticipant membership = participant(participant);

        assertThrows(AccessDeniedException.class,
                () -> service.removeParticipant(session.getId(), membership.getId(), participant.getUsername()));
    }

    @Test
    void creatorCanRemoveParticipantAndPromoteWaitlist() {
        Profile target = profile("target");
        SessionParticipant membership = participant(target);
        Profile waiting = profile("waiting");
        waitlisted(waiting);

        service.removeParticipant(session.getId(), membership.getId(), creator.getUsername());

        assertEquals(ParticipantState.REMOVED,
                participantRepository.findById(membership.getId()).orElseThrow().getState());
        assertTrue(participantRepository.existsBySessionIdAndProfileIdAndState(
                session.getId(), waiting.getId(), ParticipantState.ACTIVE));
    }

    @Test
    void nonCreatorCannotCancelSession() {
        Profile participant = profile("participant");
        participant(participant);

        assertThrows(AccessDeniedException.class,
                () -> service.cancel(session.getId(), participant.getUsername()));

        assertEquals(SessionStatus.OPEN,
                sessionRepository.findById(session.getId()).orElseThrow().getStatus());
    }

    @Test
    void creatorCancellationKeepsHistoryAndDisablesJoining() {
        Profile waiting = profile("waiting");
        waitlisted(waiting);
        long participantsBefore = participantRepository.count();
        long waitlistBefore = waitlistRepository.count();

        service.cancel(session.getId(), creator.getUsername());

        assertEquals(SessionStatus.CANCELLED, sessionRepository.findById(session.getId()).orElseThrow().getStatus());
        assertEquals(participantsBefore, participantRepository.count());
        assertEquals(waitlistBefore, waitlistRepository.count());

        Profile newcomer = profile("newcomer");
        assertThrows(IllegalStateException.class,
                () -> service.join(session.getId(), new JoinSessionRequest(null), newcomer.getUsername()));
    }

    @Test
    void completedSessionCannotBeCancelled() {
        session.setStatus(SessionStatus.COMPLETED);
        sessionRepository.saveAndFlush(session);

        assertThrows(IllegalStateException.class,
                () -> service.cancel(session.getId(), creator.getUsername()));

        assertEquals(SessionStatus.COMPLETED,
                sessionRepository.findById(session.getId()).orElseThrow().getStatus());
    }

    @Test
    void participantCannotLeaveCompletedSession() {
        Profile participant = profile("participant");
        SessionParticipant membership = participant(participant);
        session.setStatus(SessionStatus.COMPLETED);
        sessionRepository.saveAndFlush(session);

        assertThrows(IllegalStateException.class,
                () -> service.leave(session.getId(), participant.getUsername()));

        assertEquals(ParticipantState.ACTIVE,
                participantRepository.findById(membership.getId()).orElseThrow().getState());
    }

    @Test
    void creatorCannotRemoveParticipantFromCancelledSession() {
        Profile participant = profile("participant");
        SessionParticipant membership = participant(participant);
        session.setStatus(SessionStatus.CANCELLED);
        sessionRepository.saveAndFlush(session);

        assertThrows(IllegalStateException.class,
                () -> service.removeParticipant(session.getId(), membership.getId(), creator.getUsername()));

        assertEquals(ParticipantState.ACTIVE,
                participantRepository.findById(membership.getId()).orElseThrow().getState());
    }

    @Test
    void creatorCanCompleteSessionAfterStart() {
        session.setGameDate(Date.from(Instant.now().minusSeconds(60)));
        sessionRepository.saveAndFlush(session);

        service.complete(session.getId(), creator.getUsername());

        assertEquals(SessionStatus.COMPLETED,
                sessionRepository.findById(session.getId()).orElseThrow().getStatus());
        assertTrue(pilotEventRepository.existsByEventTypeAndSubjectId(
                com.example.desporto24.enumeration.PilotEventType.SESSION_COMPLETED,
                session.getId()));
    }

    @Test
    void sessionCannotBeCompletedBeforeStart() {
        assertThrows(IllegalStateException.class,
                () -> service.complete(session.getId(), creator.getUsername()));

        assertEquals(SessionStatus.OPEN,
                sessionRepository.findById(session.getId()).orElseThrow().getStatus());
    }

    @Test
    void nonCreatorCannotCompleteSession() {
        Profile participant = profile("participant");
        participant(participant);
        session.setGameDate(Date.from(Instant.now().minusSeconds(60)));
        sessionRepository.saveAndFlush(session);

        assertThrows(AccessDeniedException.class,
                () -> service.complete(session.getId(), participant.getUsername()));

        assertEquals(SessionStatus.OPEN,
                sessionRepository.findById(session.getId()).orElseThrow().getStatus());
    }

    @Test
    void cancelledSessionCannotBeCompleted() {
        session.setGameDate(Date.from(Instant.now().minusSeconds(60)));
        session.setStatus(SessionStatus.CANCELLED);
        sessionRepository.saveAndFlush(session);

        assertThrows(IllegalStateException.class,
                () -> service.complete(session.getId(), creator.getUsername()));

        assertEquals(SessionStatus.CANCELLED,
                sessionRepository.findById(session.getId()).orElseThrow().getStatus());
    }

    @Test
    void creatorCanReportSessionDidNotOccurAfterStart() {
        session.setGameDate(Date.from(Instant.now().minusSeconds(60)));
        sessionRepository.saveAndFlush(session);

        service.reportDidNotOccur(session.getId(), creator.getUsername());

        Session saved = sessionRepository.findById(session.getId()).orElseThrow();
        assertEquals(SessionOutcome.NOT_OCCURRED, saved.getOutcome());
        assertEquals(SessionStatus.CANCELLED, saved.getStatus());
    }

    @Test
    void venueProblemDoesNotCloseSession() {
        session.setGameDate(Date.from(Instant.now().minusSeconds(60)));
        sessionRepository.saveAndFlush(session);

        service.reportProblem(
                session.getId(),
                SessionProblemType.VENUE_PROBLEM,
                "Balneários sem água quente",
                creator.getUsername()
        );

        Session saved = sessionRepository.findById(session.getId()).orElseThrow();
        assertEquals(SessionProblemType.VENUE_PROBLEM, saved.getProblemType());
        assertEquals("Balneários sem água quente", saved.getProblemDetails());
        assertEquals(SessionStatus.OPEN, saved.getStatus());
        assertNull(saved.getOutcome());
    }

    @Test
    void problemCannotBeReportedBeforeStart() {
        assertThrows(IllegalStateException.class,
                () -> service.reportDidNotOccur(
                        session.getId(),
                        creator.getUsername()));

        assertNull(sessionRepository.findById(session.getId()).orElseThrow().getOutcome());
    }

    @Test
    void userWhoLeftCanJoinAgainWithoutViolatingUniqueMembershipConstraint() {
        Profile returning = profile("returning");
        SessionParticipant membership = participant(returning);

        service.leave(session.getId(), returning.getUsername());

        var response = service.join(session.getId(), new JoinSessionRequest(null), returning.getUsername());

        assertEquals(JoinState.PARTICIPANT, response.state());
        SessionParticipant reactivated = participantRepository
                .findBySessionIdAndProfileIdAndState(session.getId(), returning.getId(), ParticipantState.ACTIVE)
                .orElseThrow();
        assertEquals(membership.getId(), reactivated.getId());
    }

    private Profile profile(String username) {
        Profile p = new Profile();
        p.setUsername(username);
        p.setUserId(username + "-id");
        return profileRepository.saveAndFlush(p);
    }

    private Session session(int max) {
        Session s = new Session();
        s.setProfile(creator);
        s.setUsername(creator.getUsername());
        s.setSport("Futebol");
        s.setCity("Cacem");
        s.setAddress("Rua do Campo");
        s.setGameDate(Date.from(Instant.now().plusSeconds(3600)));
        s.setMaxParticipants(max);
        s.setStatus(SessionStatus.OPEN);
        return sessionRepository.saveAndFlush(s);
    }

    private SessionParticipant participant(Profile profile) {
        return participantRepository.saveAndFlush(SessionParticipant.builder()
                .session(session)
                .profile(profile)
                .state(ParticipantState.ACTIVE)
                .attendanceStatus(AttendanceStatus.PENDING)
                .joinedAt(Instant.now())
                .build());
    }

    private SessionWaitlist waitlisted(Profile profile) {
        return waitlistRepository.saveAndFlush(SessionWaitlist.builder()
                .session(session)
                .profile(profile)
                .priorityTier(0)
                .createdAt(Instant.now())
                .build());
    }
}
