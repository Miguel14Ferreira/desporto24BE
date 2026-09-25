package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.session.CreateSessionRequest;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionWaitlistRepository;
import com.example.desporto24.security.PasswordHashService;
import com.example.desporto24.service.impl.SessionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionCreationServiceTest {

    @Mock private SessionRepository sessionRepository;
    @Mock private ProfileRepository profileRepository;
    @Mock private SessionParticipantRepository participantRepository;
    @Mock private SessionWaitlistRepository waitlistRepository;
    @Mock private SessionViewMapper sessionViewMapper;
    @Mock private SessionDistanceCalculator distanceCalculator;
    @Mock private PasswordHashService passwordHashService;
    @Mock private BCryptPasswordEncoder passwordEncoder;
    @Mock private WaitlistPromotionService waitlistPromotionService;
    @Mock private ReliabilityService reliabilityService;
    @Mock private SessionNotificationService sessionNotificationService;

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

        var participantCaptor = ArgumentCaptor.forClass(com.example.desporto24.model.session.SessionParticipant.class);
        verify(participantRepository).save(participantCaptor.capture());
        assertSame(creator, participantCaptor.getValue().getProfile());
        assertEquals(ParticipantState.ACTIVE, participantCaptor.getValue().getState());
        assertEquals(AttendanceStatus.PENDING, participantCaptor.getValue().getAttendanceStatus());
    }

    private SessionServiceImpl service() {
        return new SessionServiceImpl(
                sessionRepository,
                profileRepository,
                participantRepository,
                waitlistRepository,
                sessionViewMapper,
                distanceCalculator,
                passwordHashService,
                passwordEncoder,
                waitlistPromotionService,
                reliabilityService,
                sessionNotificationService
        );
    }
}
