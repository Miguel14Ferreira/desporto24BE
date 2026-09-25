package com.example.desporto24.repository.session;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.model.session.SessionWaitlist;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:session-domain;MODE=LEGACY;NON_KEYWORDS=SESSION;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.hikari.schema=PUBLIC"
})
class SessionDomainRepositoryTest {

    @Autowired private ProfileRepository profileRepository;
    @Autowired private SessionRepository sessionRepository;
    @Autowired private SessionParticipantRepository participantRepository;
    @Autowired private SessionWaitlistRepository waitlistRepository;

    private Profile profile;
    private Session session;

    @BeforeEach
    void setUp() {
        profile = new Profile();
        profile.setUsername("miguel");
        profile.setUserId("1234567890");
        profile = profileRepository.saveAndFlush(profile);

        session = new Session();
        session.setSport("Futebol");
        session.setGameDate(new Date(System.currentTimeMillis() + 3_600_000));
        session.setCity("Cacem");
        session.setProfile(profile);
        session = sessionRepository.saveAndFlush(session);
    }

    @Test
    void participantMembershipIsUniquePerSessionAndProfile() {
        participantRepository.saveAndFlush(participant());
        assertThrows(DataIntegrityViolationException.class,
                () -> participantRepository.saveAndFlush(participant()));
    }

    @Test
    void waitlistMembershipIsUniquePerSessionAndProfile() {
        waitlistRepository.saveAndFlush(waitlist());
        assertThrows(DataIntegrityViolationException.class,
                () -> waitlistRepository.saveAndFlush(waitlist()));
    }

    private SessionParticipant participant() {
        SessionParticipant participant = new SessionParticipant();
        participant.setSession(session);
        participant.setProfile(profile);
        participant.setState(ParticipantState.ACTIVE);
        participant.setAttendanceStatus(AttendanceStatus.PENDING);
        participant.setJoinedAt(Instant.now());
        return participant;
    }

    private SessionWaitlist waitlist() {
        SessionWaitlist waitlist = new SessionWaitlist();
        waitlist.setSession(session);
        waitlist.setProfile(profile);
        waitlist.setPriorityTier(0);
        waitlist.setCreatedAt(Instant.now());
        return waitlist;
    }
}
