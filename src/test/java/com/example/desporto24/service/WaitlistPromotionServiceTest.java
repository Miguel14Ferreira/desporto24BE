package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.model.session.SessionWaitlist;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionWaitlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:waitlist-promotion;MODE=LEGACY;NON_KEYWORDS=SESSION;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.hikari.schema=PUBLIC"
})
@Import(WaitlistPromotionService.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class WaitlistPromotionServiceTest {

    @Autowired private WaitlistPromotionService service;
    @Autowired private ProfileRepository profileRepository;
    @Autowired private SessionRepository sessionRepository;
    @Autowired private SessionParticipantRepository participantRepository;
    @Autowired private SessionWaitlistRepository waitlistRepository;

    private Profile creator;
    private Session session;

    @BeforeEach
    void setUp() {
        waitlistRepository.deleteAll();
        participantRepository.deleteAll();
        sessionRepository.deleteAll();
        profileRepository.deleteAll();

        creator = profile("creator");
        session = session(2);
        activeParticipant(creator);
    }

    @Test
    void promotesFirstWaitlistedUserAndMarksSessionFull() {
        Profile first = profile("first");
        Profile second = profile("second");
        waitlisted(first, Instant.parse("2026-09-25T10:00:00Z"));
        waitlisted(second, Instant.parse("2026-09-25T10:01:00Z"));

        Optional<SessionParticipant> promoted = service.promoteNext(session, Instant.now());

        assertTrue(promoted.isPresent());
        assertEquals(first.getId(), promoted.get().getProfile().getId());
        assertEquals(ParticipantState.ACTIVE, promoted.get().getState());
        assertTrue(waitlistRepository.findBySessionIdAndProfileId(session.getId(), first.getId()).isEmpty());
        assertTrue(waitlistRepository.findBySessionIdAndProfileId(session.getId(), second.getId()).isPresent());
        assertEquals(SessionStatus.FULL, sessionRepository.findById(session.getId()).orElseThrow().getStatus());
    }

    @Test
    void lowerPriorityTierIsPromotedBeforeEarlierReducedPriorityEntry() {
        Profile reducedPriority = profile("reduced");
        Profile normalPriority = profile("normal");

        waitlisted(reducedPriority, 1, Instant.parse("2026-09-25T10:00:00Z"));
        waitlisted(normalPriority, 0, Instant.parse("2026-09-25T10:05:00Z"));

        SessionParticipant promoted = service.promoteNext(session, Instant.now()).orElseThrow();

        assertEquals(normalPriority.getId(), promoted.getProfile().getId());
        assertTrue(waitlistRepository.findBySessionIdAndProfileId(
                session.getId(), normalPriority.getId()).isEmpty());
        assertTrue(waitlistRepository.findBySessionIdAndProfileId(
                session.getId(), reducedPriority.getId()).isPresent());
    }

    @Test
    void noWaitlistedUserLeavesSessionOpenWhenCapacityIsAvailable() {
        session.setStatus(SessionStatus.FULL);
        sessionRepository.saveAndFlush(session);

        Optional<SessionParticipant> promoted = service.promoteNext(session, Instant.now());

        assertTrue(promoted.isEmpty());
        assertEquals(SessionStatus.OPEN,
                sessionRepository.findById(session.getId()).orElseThrow().getStatus());
    }

    @Test
    void reactivatesExistingMembershipWhenWaitlistedUserHadPreviouslyLeft() {
        Profile returning = profile("returning");
        SessionParticipant old = activeParticipant(returning);
        old.setState(ParticipantState.LEFT);
        old.setExitedAt(Instant.now().minusSeconds(300));
        participantRepository.saveAndFlush(old);
        waitlisted(returning, Instant.now().minusSeconds(60));

        SessionParticipant promoted = service.promoteNext(session, Instant.now()).orElseThrow();

        assertEquals(old.getId(), promoted.getId());
        assertEquals(ParticipantState.ACTIVE, promoted.getState());
        assertNull(promoted.getExitedAt());
    }

    @Test
    void cancelledSessionDoesNotPromoteWaitlist() {
        Profile waiting = profile("waiting");
        waitlisted(waiting, Instant.now());
        session.setStatus(SessionStatus.CANCELLED);
        sessionRepository.saveAndFlush(session);

        Optional<SessionParticipant> promoted = service.promoteNext(session, Instant.now());

        assertTrue(promoted.isEmpty());
        assertTrue(waitlistRepository.findBySessionIdAndProfileId(session.getId(), waiting.getId()).isPresent());
        assertEquals(SessionStatus.CANCELLED, sessionRepository.findById(session.getId()).orElseThrow().getStatus());
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

    private SessionParticipant activeParticipant(Profile profile) {
        return participantRepository.saveAndFlush(SessionParticipant.builder()
                .session(session)
                .profile(profile)
                .state(ParticipantState.ACTIVE)
                .attendanceStatus(AttendanceStatus.PENDING)
                .joinedAt(Instant.now())
                .build());
    }

    private SessionWaitlist waitlisted(Profile profile, Instant createdAt) {
        return waitlisted(profile, 0, createdAt);
    }

    private SessionWaitlist waitlisted(Profile profile, int priorityTier, Instant createdAt) {
        return waitlistRepository.saveAndFlush(SessionWaitlist.builder()
                .session(session)
                .profile(profile)
                .priorityTier(priorityTier)
                .createdAt(createdAt)
                .build());
    }
}
