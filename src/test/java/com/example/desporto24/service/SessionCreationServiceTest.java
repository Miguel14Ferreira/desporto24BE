package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionRecurrence;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.model.dto.session.CreateSessionRequest;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionOrganizerExclusionRepository;
import com.example.desporto24.repository.session.SessionWaitlistRepository;
import com.example.desporto24.security.PasswordHashService;
import com.example.desporto24.service.impl.SessionServiceImpl;
import com.example.desporto24.storage.MediaStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionCreationServiceTest {

    @Mock private SessionRepository sessionRepository;
    @Mock private ProfileRepository profileRepository;
    @Mock private CommunityRepository communityRepository;
    @Mock private CommunityMemberRepository communityMemberRepository;
    @Mock private VenueRepository venueRepository;
    @Mock private SessionParticipantRepository participantRepository;
    @Mock private SessionOrganizerExclusionRepository exclusionRepository;
    @Mock private SessionWaitlistRepository waitlistRepository;
    @Mock private SessionViewMapper sessionViewMapper;
    @Mock private SessionDistanceCalculator distanceCalculator;
    @Mock private PasswordHashService passwordHashService;
    @Mock private BCryptPasswordEncoder passwordEncoder;
    @Mock private WaitlistPromotionService waitlistPromotionService;
    @Mock private ReliabilityService reliabilityService;
    @Mock private SessionNotificationService sessionNotificationService;
    @Mock private PilotEventService pilotEventService;
    @Mock private MediaStorage mediaStorage;

    @Test
    void relaunchCreationDoesNotWriteLegacySessionFields() {
        Profile creator = new Profile();
        creator.setUsername("miguel");
        creator.setUserId("1234567890");

        when(profileRepository.findUserByUsername("miguel")).thenReturn(Optional.of(creator));
        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateSessionRequest request = new CreateSessionRequest(
                "Futebol",
                Instant.now().plusSeconds(3600),
                "Queluz",
                "Rua de Teste 1",
                10,
                BigDecimal.valueOf(5),
                false,
                null,
                38.756,
                -9.254
        );

        service().create(request, "miguel");

        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository).save(sessionCaptor.capture());

        Session saved = sessionCaptor.getValue();
        assertNull(saved.getUsername());
        assertNull(saved.getPlayers());
        assertNull(saved.getPrice());
        assertNull(saved.getPassword());
        assertNull(saved.getRecurrenceSeriesId());

        var participantCaptor = ArgumentCaptor.forClass(com.example.desporto24.model.session.SessionParticipant.class);
        verify(participantRepository).save(participantCaptor.capture());
        assertSame(creator, participantCaptor.getValue().getProfile());
        assertEquals(ParticipantState.ACTIVE, participantCaptor.getValue().getState());
        assertEquals(AttendanceStatus.PENDING, participantCaptor.getValue().getAttendanceStatus());
    }

    @Test
    void creationStoresExternalConfirmationsAndLeavesExactlyTwoPlaces() {
        Profile creator = new Profile();
        creator.setUsername("miguel");
        creator.setUserId("creator-id");

        when(profileRepository.findUserByUsername("miguel")).thenReturn(Optional.of(creator));
        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateSessionRequest request = withExternalConfirmations(7);
        service().create(request, "miguel");

        ArgumentCaptor<Session> captor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository).save(captor.capture());
        Session saved = captor.getValue();

        assertEquals(7, saved.getExternalConfirmedParticipants());
        assertEquals(8, SessionCapacity.totalConfirmed(saved, 1));
        assertEquals(2, SessionCapacity.remainingPlaces(saved, 1));
        assertEquals(com.example.desporto24.enumeration.SessionStatus.OPEN, saved.getStatus());
        verify(participantRepository).save(any());
    }

    @Test
    void creationMarksSessionFullWhenExternalConfirmationsFillAllRemainingPlaces() {
        Profile creator = new Profile();
        creator.setUsername("miguel");
        creator.setUserId("creator-id");

        when(profileRepository.findUserByUsername("miguel")).thenReturn(Optional.of(creator));
        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service().create(withExternalConfirmations(9), "miguel");

        ArgumentCaptor<Session> captor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository).save(captor.capture());
        assertEquals(com.example.desporto24.enumeration.SessionStatus.FULL, captor.getValue().getStatus());
    }

    @Test
    void creationRejectsExternalCountsThatAreNegativeOrConsumeOrganiserPlace() {
        Profile creator = new Profile();
        creator.setUsername("miguel");
        creator.setUserId("creator-id");

        when(profileRepository.findUserByUsername("miguel")).thenReturn(Optional.of(creator));

        for (int external : new int[]{-1, 10, 100}) {
            assertThrows(IllegalArgumentException.class,
                    () -> service().create(withExternalConfirmations(external), "miguel"));
        }
        verify(sessionRepository, never()).save(any(Session.class));
        verify(participantRepository, never()).save(any());
    }

    private CreateSessionRequest withExternalConfirmations(int external) {
        return new CreateSessionRequest(
                "Futebol", Instant.now().plusSeconds(3600), "Queluz", "Rua de Teste 1",
                10, BigDecimal.ZERO, false, null, 38.756, -9.254,
                null, null, SessionRecurrence.NONE, null, external);
    }

    @Test
    void creationRecordsPilotEventAfterSessionIsPersisted() {
        Profile creator = new Profile();
        creator.setId(7L);
        creator.setUsername("miguel");
        creator.setUserId("1234567890");

        when(profileRepository.findUserByUsername("miguel")).thenReturn(Optional.of(creator));
        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> {
            Session saved = invocation.getArgument(0);
            saved.setId(99L);
            return saved;
        });

        CreateSessionRequest request = new CreateSessionRequest(
                "Futebol",
                Instant.now().plusSeconds(3600),
                "Queluz",
                "Rua de Teste 1",
                10,
                BigDecimal.ZERO,
                false,
                null,
                38.756,
                -9.254
        );

        service().create(request, "miguel");

        verify(pilotEventService).recordSessionCreated(creator, 99L);
    }

    @Test
    void savedVenueOverridesManualLocation() {
        Profile creator = new Profile();
        creator.setId(7L);
        creator.setUsername("miguel");
        creator.setUserId("1234567890");

        Venue venue = Venue.builder()
                .id(44L)
                .name("Pavilhão Central")
                .city("Lisboa")
                .address("Rua do Desporto 10")
                .latitude(38.72)
                .longitude(-9.14)
                .createdBy(creator)
                .build();

        when(profileRepository.findUserByUsername("miguel"))
                .thenReturn(Optional.of(creator));
        when(venueRepository.findById(44L))
                .thenReturn(Optional.of(venue));
        when(sessionRepository.save(any(Session.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateSessionRequest request = new CreateSessionRequest(
                "Futsal",
                Instant.now().plusSeconds(3600),
                "Outra cidade",
                "Outra morada",
                10,
                BigDecimal.ZERO,
                false,
                null,
                null,
                null,
                null,
                44L
        );

        service().create(request, "miguel");

        ArgumentCaptor<Session> captor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository).save(captor.capture());

        Session saved = captor.getValue();
        assertSame(venue, saved.getVenue());
        assertEquals("Lisboa", saved.getCity());
        assertEquals("Rua do Desporto 10", saved.getAddress());
        assertEquals(38.72, saved.getLatitude());
        assertEquals(-9.14, saved.getLongitude());
    }

    @Test
    void optionalPhotoIsStoredAndSavedAsSessionImageUrl() {
        Profile creator = new Profile();
        creator.setId(7L);
        creator.setUsername("miguel");
        creator.setUserId("1234567890");

        when(profileRepository.findUserByUsername("miguel")).thenReturn(Optional.of(creator));
        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> {
            Session session = invocation.getArgument(0);
            if (session.getId() == null) {
                session.setId(99L);
            }
            return session;
        });

        var photo = new MockMultipartFile(
                "photo", "match.png", "image/png", new byte[]{1, 2, 3});
        when(mediaStorage.store(startsWith("sessions/99/"), same(photo)))
                .thenReturn(URI.create("https://media.example/sessions/99/photo.png"));

        CreateSessionRequest request = new CreateSessionRequest(
                "Futebol",
                Instant.now().plusSeconds(3600),
                "Queluz",
                "Rua de Teste 1",
                10,
                BigDecimal.ZERO,
                false,
                null,
                38.756,
                -9.254
        );

        service().create(request, photo, "miguel");

        ArgumentCaptor<Session> captor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository, times(2)).save(captor.capture());
        assertEquals(
                "https://media.example/sessions/99/photo.png",
                captor.getAllValues().getLast().getGamePhoto()
        );
    }


    @Test
    void creationRejectsSportOutsideCatalog() {
        Profile creator = new Profile();
        creator.setUsername("miguel");
        creator.setUserId("1234567890");

        when(profileRepository.findUserByUsername("miguel"))
                .thenReturn(Optional.of(creator));

        CreateSessionRequest request = new CreateSessionRequest(
                "Desporto Inventado",
                Instant.now().plusSeconds(3600),
                "Queluz",
                "Rua de Teste 1",
                10,
                BigDecimal.ZERO,
                false,
                null,
                38.756,
                -9.254
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service().create(request, "miguel")
        );

        assertEquals("Desporto inválido", exception.getMessage());
        verify(sessionRepository, never()).save(any(Session.class));
        verify(participantRepository, never()).save(any());
    }

    @Test
    void creationTrimsValidCatalogSportBeforePersisting() {
        Profile creator = new Profile();
        creator.setUsername("miguel");
        creator.setUserId("1234567890");

        when(profileRepository.findUserByUsername("miguel"))
                .thenReturn(Optional.of(creator));
        when(sessionRepository.save(any(Session.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateSessionRequest request = new CreateSessionRequest(
                "  Futsal  ",
                Instant.now().plusSeconds(3600),
                "Queluz",
                "Rua de Teste 1",
                10,
                BigDecimal.ZERO,
                false,
                null,
                38.756,
                -9.254
        );

        service().create(request, "miguel");

        ArgumentCaptor<Session> captor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository).save(captor.capture());
        assertEquals("Futsal", captor.getValue().getSport());
    }


    @Test
    void weeklyCommunityRecurrenceCreatesRequestedOccurrences() {
        Profile creator = new Profile();
        creator.setId(7L);
        creator.setUsername("miguel");
        creator.setUserId("1234567890");

        Community community = new Community();
        community.setId(12L);

        Instant firstGame = Instant.ofEpochMilli(
                Instant.now().plusSeconds(3600).toEpochMilli()
        );

        when(profileRepository.findUserByUsername("miguel"))
                .thenReturn(Optional.of(creator));
        when(communityRepository.findById(12L))
                .thenReturn(Optional.of(community));
        when(communityMemberRepository.existsByCommunityIdAndProfileId(12L, 7L))
                .thenReturn(true);
        when(sessionRepository.save(any(Session.class)))
                .thenAnswer(invocation -> {
                    Session saved = invocation.getArgument(0);
                    if (saved.getId() == null) {
                        saved.setId(100L);
                    }
                    return saved;
                });

        CreateSessionRequest request = new CreateSessionRequest(
                "Futsal",
                firstGame,
                "Queluz",
                "Rua de Teste 1",
                10,
                BigDecimal.ZERO,
                false,
                null,
                38.756,
                -9.254,
                12L,
                null,
                SessionRecurrence.WEEKLY,
                3
        );

        service().create(request, "miguel");

        ArgumentCaptor<Session> captor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository, times(3)).save(captor.capture());
        verify(participantRepository, times(3)).save(any());

        // java.util.Date stores milliseconds, whereas Instant.now() also has nanoseconds.
        // Keep verifying the weekly offsets at the precision persisted by Session.
        Instant firstGameInDatePrecision = java.util.Date.from(firstGame).toInstant();
        assertEquals(firstGameInDatePrecision, captor.getAllValues().get(0).getGameDate().toInstant());
        assertEquals(firstGameInDatePrecision.plus(7, java.time.temporal.ChronoUnit.DAYS),
                captor.getAllValues().get(1).getGameDate().toInstant());
        assertEquals(firstGameInDatePrecision.plus(14, java.time.temporal.ChronoUnit.DAYS),
                captor.getAllValues().get(2).getGameDate().toInstant());

        String seriesId = captor.getAllValues().get(0).getRecurrenceSeriesId();
        assertNotNull(seriesId);
        assertEquals(seriesId, captor.getAllValues().get(1).getRecurrenceSeriesId());
        assertEquals(seriesId, captor.getAllValues().get(2).getRecurrenceSeriesId());
    }

    @Test
    void recurringSessionRequiresCommunity() {
        Profile creator = new Profile();
        creator.setId(7L);
        creator.setUsername("miguel");
        creator.setUserId("1234567890");

        when(profileRepository.findUserByUsername("miguel"))
                .thenReturn(Optional.of(creator));

        CreateSessionRequest request = new CreateSessionRequest(
                "Futsal",
                Instant.now().plusSeconds(3600),
                "Queluz",
                "Rua de Teste 1",
                10,
                BigDecimal.ZERO,
                false,
                null,
                38.756,
                -9.254,
                null,
                null,
                SessionRecurrence.WEEKLY,
                3
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service().create(request, "miguel")
        );

        assertEquals("Recurring sessions must belong to a community", exception.getMessage());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    private SessionServiceImpl service() {
        SessionServiceImpl service = new SessionServiceImpl(
                sessionRepository,
                profileRepository,
                communityRepository,
                communityMemberRepository,
                venueRepository,
                participantRepository,
                exclusionRepository,
                waitlistRepository,
                sessionViewMapper,
                distanceCalculator,
                passwordHashService,
                passwordEncoder,
                waitlistPromotionService,
                reliabilityService,
                sessionNotificationService,
                pilotEventService
        );
        ReflectionTestUtils.setField(service, "mediaStorage", mediaStorage);
        return service;
    }
}
