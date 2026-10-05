package com.example.desporto24.service;

import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.session.SessionSearchCriteria;
import com.example.desporto24.model.dto.session.SessionSummaryResponse;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionDiscoveryServiceTest {

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
    private final java.util.Map<Long, Integer> participantCounts = new java.util.HashMap<>();

    @BeforeEach
    void setUp() {
        participantCounts.clear();
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
    void authenticatedDiscoveryRecordsPilotSearchAfterSuccessfulDiscovery() {
        Profile profile = new Profile();
        profile.setId(7L);
        profile.setUsername("miguel");

        when(profileRepository.findUserByUsername("miguel")).thenReturn(Optional.of(profile));
        when(sessionRepository.searchDiscoverable(
                anyCollection(),
                any(Date.class),
                anyString(),
                anyString(),
                nullable(Date.class),
                nullable(Date.class),
                nullable(Boolean.class),
                anyBoolean(),
                nullable(BigDecimal.class),
                nullable(BigDecimal.class)))
                .thenReturn(List.of());

        List<SessionSummaryResponse> result = service.discover(criteria(
                null, null, null, null, null, null, null,
                null, null, null, null
        ), "miguel");

        assertTrue(result.isEmpty());
        verify(pilotEventService).recordSessionSearched(profile);
    }

    @Test
    void coordinatesUseDefaultTwentyFiveKilometreRadiusAndSortByDistance() {
        Session near = session(1L, "Futebol", "Queluz", 38.75, -9.25, 5, false, 10, 2);
        Session nearer = session(2L, "Futebol", "Amadora", 38.76, -9.24, 0, false, 10, 4);
        Session far = session(3L, "Futebol", "Sintra", 38.80, -9.40, 0, false, 10, 1);

        stubCandidates(near, nearer, far);
        when(distanceCalculator.kilometres(38.75, -9.25, near.getLatitude(), near.getLongitude())).thenReturn(10.0);
        when(distanceCalculator.kilometres(38.75, -9.25, nearer.getLatitude(), nearer.getLongitude())).thenReturn(3.0);
        when(distanceCalculator.kilometres(38.75, -9.25, far.getLatitude(), far.getLongitude())).thenReturn(30.0);
        stubSummary(near, 10.0, 2);
        stubSummary(nearer, 3.0, 4);
        stubSummary(far, 30.0, 1);

        List<SessionSummaryResponse> result = service.discover(criteria(
                null, null, null, null, 38.75, -9.25, null,
                null, null, null, null
        ));

        assertEquals(List.of(2L, 1L), result.stream().map(SessionSummaryResponse::id).toList());
        assertTrue(result.stream().allMatch(v -> v.distanceKm() <= 25.0));
    }

    @Test
    void radiusIsNotAppliedWhenCoordinatesAreMissing() {
        Session first = session(1L, "Futebol", "Queluz", 38.75, -9.25, 0, false, 10, 1);
        Session second = session(2L, "Padel", "Sintra", 38.80, -9.40, 10, true, 4, 4);

        stubCandidates(first, second);
        stubSummary(first, null, 1);
        stubSummary(second, null, 4);

        List<SessionSummaryResponse> result = service.discover(criteria(
                null, null, null, null, null, null, 1.0,
                null, null, null, null
        ));

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(v -> v.distanceKm() == null));
        verifyNoInteractions(distanceCalculator);
    }

    @Test
    void appliesSportCityDateAccessFreeAndAvailabilityFilters() {
        Instant from = Instant.now().plusSeconds(1800);
        Instant to = from.plusSeconds(7200);

        Session match = session(1L, "Futebol", "Queluz", null, null, 0, false, 10, 4);
        match.setGameDate(Date.from(from.plusSeconds(1800)));

        Session wrongSport = session(2L, "Padel", "Queluz", null, null, 0, false, 10, 4);
        wrongSport.setGameDate(Date.from(from.plusSeconds(1800)));

        Session full = session(3L, "Futebol", "Queluz", null, null, 0, false, 4, 4);
        full.setGameDate(Date.from(from.plusSeconds(1800)));

        Session paid = session(4L, "Futebol", "Queluz", null, null, 8, false, 10, 4);
        paid.setGameDate(Date.from(from.plusSeconds(1800)));

        Session privateSession = session(5L, "Futebol", "Queluz", null, null, 0, true, 10, 4);
        privateSession.setGameDate(Date.from(from.plusSeconds(1800)));

        Session wrongCity = session(6L, "Futebol", "Lisboa", null, null, 0, false, 10, 4);
        wrongCity.setGameDate(Date.from(from.plusSeconds(1800)));

        stubCandidates(match, full);
        stubSummary(match, null, 4);
        stubSummary(full, null, 4);

        List<SessionSummaryResponse> result = service.discover(criteria(
                "futebol", "quel", from, to, null, null, null,
                "available", false, true, "date"
        ));

        assertEquals(List.of(1L), result.stream().map(SessionSummaryResponse::id).toList());
    }

    @Test
    void filtersByMinimumAndMaximumPrice() {
        Session cheap = session(1L, "Futebol", "Queluz", null, null, 2, false, 10, 2);
        Session match = session(2L, "Futebol", "Queluz", null, null, 6, false, 10, 2);
        Session expensive = session(3L, "Futebol", "Queluz", null, null, 12, false, 10, 2);

        stubCandidates(match);
        stubSummary(match, null, 2);

        SessionSearchCriteria criteria = new SessionSearchCriteria(
                null, null, null, null, null, null, null,
                null, null, null, BigDecimal.valueOf(5), BigDecimal.valueOf(10), "date"
        );

        List<SessionSummaryResponse> result = service.discover(criteria);

        assertEquals(List.of(2L), result.stream().map(SessionSummaryResponse::id).toList());
    }

    @Test
    void supportsVacancyAndPriceSorting() {
        Session a = session(1L, "Futebol", "Queluz", null, null, 8, false, 10, 8);
        Session b = session(2L, "Futebol", "Queluz", null, null, 2, false, 10, 2);
        Session c = session(3L, "Futebol", "Queluz", null, null, 5, false, 10, 5);

        stubCandidates(a, b, c);
        stubSummary(a, null, 8);
        stubSummary(b, null, 2);
        stubSummary(c, null, 5);

        List<SessionSummaryResponse> byVacancies = service.discover(criteria(
                null, null, null, null, null, null, null,
                null, null, null, "vacancies"
        ));
        assertEquals(List.of(2L, 3L, 1L), byVacancies.stream().map(SessionSummaryResponse::id).toList());

        List<SessionSummaryResponse> byPrice = service.discover(criteria(
                null, null, null, null, null, null, null,
                null, null, null, "price"
        ));
        assertEquals(List.of(2L, 3L, 1L), byPrice.stream().map(SessionSummaryResponse::id).toList());
    }

    private void stubCandidates(Session... sessions) {
        when(sessionRepository.searchDiscoverable(
                anyCollection(),
                any(Date.class),
                anyString(),
                anyString(),
                nullable(Date.class),
                nullable(Date.class),
                nullable(Boolean.class),
                anyBoolean(),
                nullable(BigDecimal.class),
                nullable(BigDecimal.class)))
                .thenReturn(List.of(sessions));
        when(participantRepository.countBySessionIdsAndState(anyCollection(), eq(com.example.desporto24.enumeration.ParticipantState.ACTIVE)))
                .thenAnswer(invocation -> {
                    java.util.Collection<Long> sessionIds = invocation.getArgument(0);
                    return sessionIds.stream()
                            .filter(participantCounts::containsKey)
                            .map(id -> new Object[]{id, participantCounts.get(id).longValue()})
                            .toList();
                });
    }

    private void stubSummary(Session session, Double distance, int participants) {
        participantCounts.put(session.getId(), participants);
        when(sessionViewMapper.toSummary(session, distance, null, participants)).thenReturn(new SessionSummaryResponse(
                session.getId(),
                session.getSport(),
                session.getCity(),
                session.getGameDate().toInstant(),
                distance,
                participants,
                session.getMaxParticipants(),
                session.getPriceAmount(),
                Boolean.TRUE.equals(session.getPrivateGame()),
                session.getStatus()
        ));
    }

    private Session session(Long id, String sport, String city, Double lat, Double lon,
                            int price, boolean privateGame, int max, int participants) {
        Session session = new Session();
        session.setId(id);
        session.setSport(sport);
        session.setCity(city);
        session.setLatitude(lat);
        session.setLongitude(lon);
        session.setPriceAmount(BigDecimal.valueOf(price));
        session.setPrivateGame(privateGame);
        session.setMaxParticipants(max);
        session.setStatus(participants >= max ? SessionStatus.FULL : SessionStatus.OPEN);
        session.setGameDate(Date.from(Instant.now().plusSeconds(3600 + id * 60)));
        return session;
    }

    private SessionSearchCriteria criteria(
            String sport, String city, Instant from, Instant to,
            Double latitude, Double longitude, Double radiusKm,
            String availability, Boolean privateGame, Boolean freeOnly, String sort) {
        return new SessionSearchCriteria(
                sport, city, from, to, latitude, longitude, radiusKm,
                availability, privateGame, freeOnly, null, null, sort
        );
    }
}
