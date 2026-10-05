package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.session.MySessionsResponse;
import com.example.desporto24.model.dto.session.SessionSummaryResponse;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.model.session.SessionWaitlist;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionWaitlistRepository;
import com.example.desporto24.security.PasswordHashService;
import com.example.desporto24.service.impl.SessionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionMineServiceTest {

    @Mock private SessionRepository sessionRepository;
    @Mock private ProfileRepository profileRepository;
    @Mock private CommunityRepository communityRepository;
    @Mock private CommunityMemberRepository communityMemberRepository;
    @Mock private VenueRepository venueRepository;
    @Mock private SessionParticipantRepository participantRepository;
    @Mock private SessionWaitlistRepository waitlistRepository;
    @Mock private SessionViewMapper sessionViewMapper;
    @Mock private SessionDistanceCalculator distanceCalculator;
    @Mock private PasswordHashService passwordHashService;
    @Mock private BCryptPasswordEncoder passwordEncoder;
    @Mock private WaitlistPromotionService waitlistPromotionService;
    @Mock private ReliabilityService reliabilityService;
    @Mock private SessionNotificationService sessionNotificationService;
    @Mock private PilotEventService pilotEventService;
    @Mock private ReferenceCatalogService referenceCatalogService;

    private SessionServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new SessionServiceImpl(
                sessionRepository,
                profileRepository,
                communityRepository,
                communityMemberRepository,
                venueRepository,
                participantRepository,
                waitlistRepository,
                sessionViewMapper,
                distanceCalculator,
                passwordHashService,
                passwordEncoder,
                waitlistPromotionService,
                reliabilityService,
                sessionNotificationService,
                pilotEventService,
                referenceCatalogService
        );
    }

    @Test
    void groupsCreatedJoinedAndWaitlistedWithoutDuplicatingCreatedAsJoined() {
        Profile miguel = new Profile();
        miguel.setId(7L);
        miguel.setUsername("miguel");

        Session created = session(1L, miguel, "Futebol");
        Session joined = session(2L, profile(8L, "ana"), "Padel");
        Session waitlisted = session(3L, profile(9L, "rui"), "Ténis");

        SessionParticipant creatorMembership = participant(created, miguel);
        SessionParticipant joinedMembership = participant(joined, miguel);
        SessionWaitlist waitlistEntry = waitlist(waitlisted, miguel);

        when(profileRepository.findUserByUsername("miguel")).thenReturn(Optional.of(miguel));
        when(sessionRepository.findByProfileIdOrderByGameDateAsc(7L)).thenReturn(List.of(created));
        when(participantRepository.findByProfileIdAndStateOrderByJoinedAtAsc(7L, ParticipantState.ACTIVE))
                .thenReturn(List.of(creatorMembership, joinedMembership));
        when(waitlistRepository.findByProfileIdOrderByCreatedAtAsc(7L)).thenReturn(List.of(waitlistEntry));

        SessionSummaryResponse createdSummary = summary(created);
        SessionSummaryResponse joinedSummary = summary(joined);
        SessionSummaryResponse waitlistedSummary = summary(waitlisted);

        when(sessionViewMapper.toSummary(created, null)).thenReturn(createdSummary);
        when(sessionViewMapper.toSummary(joined, null)).thenReturn(joinedSummary);
        when(sessionViewMapper.toSummary(waitlisted, null)).thenReturn(waitlistedSummary);

        MySessionsResponse result = service.mine("miguel");

        assertEquals(List.of(createdSummary), result.created());
        assertEquals(List.of(joinedSummary), result.joined());
        assertEquals(List.of(waitlistedSummary), result.waitlisted());

        verify(sessionViewMapper, times(1)).toSummary(created, null);
    }

    private Profile profile(Long id, String username) {
        Profile profile = new Profile();
        profile.setId(id);
        profile.setUsername(username);
        return profile;
    }

    private Session session(Long id, Profile creator, String sport) {
        Session session = new Session();
        session.setId(id);
        session.setProfile(creator);
        session.setSport(sport);
        session.setCity("Lisboa");
        session.setGameDate(Date.from(Instant.now().plusSeconds(3600 + id * 60)));
        session.setMaxParticipants(10);
        session.setPriceAmount(BigDecimal.ZERO);
        session.setPrivateGame(false);
        session.setStatus(SessionStatus.OPEN);
        return session;
    }

    private SessionParticipant participant(Session session, Profile profile) {
        SessionParticipant participant = new SessionParticipant();
        participant.setSession(session);
        participant.setProfile(profile);
        participant.setState(ParticipantState.ACTIVE);
        participant.setAttendanceStatus(AttendanceStatus.PENDING);
        participant.setJoinedAt(Instant.now());
        return participant;
    }

    private SessionWaitlist waitlist(Session session, Profile profile) {
        SessionWaitlist waitlist = new SessionWaitlist();
        waitlist.setId(50L + session.getId());
        waitlist.setSession(session);
        waitlist.setProfile(profile);
        waitlist.setPriorityTier(0);
        waitlist.setCreatedAt(Instant.now());
        return waitlist;
    }

    private SessionSummaryResponse summary(Session session) {
        return new SessionSummaryResponse(
                session.getId(),
                session.getSport(),
                session.getCity(),
                session.getGameDate().toInstant(),
                null,
                1,
                session.getMaxParticipants(),
                session.getPriceAmount(),
                Boolean.TRUE.equals(session.getPrivateGame()),
                session.getStatus()
        );
    }
}
