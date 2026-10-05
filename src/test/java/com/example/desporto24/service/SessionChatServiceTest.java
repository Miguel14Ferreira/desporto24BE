package com.example.desporto24.service;

import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.session.SessionMessage;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionMessageRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.desporto24.websocket.SessionChatMessageCreatedEvent;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionChatServiceTest {

    private SessionRepository sessionRepository;
    private ProfileRepository profileRepository;
    private SessionParticipantRepository participantRepository;
    private SessionMessageRepository messageRepository;
    private ApplicationEventPublisher eventPublisher;
    private SessionChatService service;

    @BeforeEach
    void setUp() {
        sessionRepository = mock(SessionRepository.class);
        profileRepository = mock(ProfileRepository.class);
        participantRepository = mock(SessionParticipantRepository.class);
        messageRepository = mock(SessionMessageRepository.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        service = new SessionChatService(
                sessionRepository,
                profileRepository,
                participantRepository,
                messageRepository,
                eventPublisher
        );
    }

    @Test
    void nonParticipantCannotReadOrWrite() {
        Profile creator = profile(1L, "creator");
        Profile outsider = profile(2L, "outsider");
        Session session = session(10L, creator);
        stubActor(session, outsider);
        when(participantRepository.existsBySessionIdAndProfileIdAndState(
                10L, 2L, ParticipantState.ACTIVE)).thenReturn(false);

        assertThrows(AccessDeniedException.class,
                () -> service.messages(10L, "outsider"));
        assertThrows(AccessDeniedException.class,
                () -> service.post(10L, "hello", "outsider"));

        verifyNoInteractions(messageRepository);
    }

    @Test
    void creatorCanReadMessages() {
        Profile creator = profile(1L, "creator");
        Session session = session(10L, creator);
        stubActor(session, creator);

        SessionMessage message = SessionMessage.builder()
                .id(30L)
                .session(session)
                .senderProfile(creator)
                .body("olá")
                .createdAt(Instant.parse("2026-09-25T12:00:00Z"))
                .build();
        when(messageRepository.findBySessionIdOrderByCreatedAtDesc(eq(10L), any()))
                .thenReturn(new java.util.ArrayList<>(List.of(message)));

        var messages = service.messages(10L, "creator");

        assertEquals(1, messages.size());
        assertEquals("creator", messages.getFirst().senderUsername());
        assertEquals("olá", messages.getFirst().body());
    }

    @Test
    void activeParticipantCanPostAndWhitespaceIsTrimmed() {
        Profile creator = profile(1L, "creator");
        Profile participant = profile(2L, "miguel");
        Session session = session(10L, creator);
        stubActor(session, participant);
        when(participantRepository.existsBySessionIdAndProfileIdAndState(
                10L, 2L, ParticipantState.ACTIVE)).thenReturn(true);
        when(messageRepository.save(any(SessionMessage.class)))
                .thenAnswer(invocation -> {
                    SessionMessage saved = invocation.getArgument(0);
                    saved.setId(40L);
                    return saved;
                });

        var response = service.post(10L, "  bora jogar  ", "miguel");

        assertEquals("bora jogar", response.body());
        assertEquals("miguel", response.senderUsername());
        verify(messageRepository).save(argThat(m -> "bora jogar".equals(m.getBody())));
    }

    @Test
    void postPublishesRealtimeEventForCreatorAndActiveParticipants() {
        Profile creator = profile(1L, "creator");
        Profile sender = profile(2L, "miguel");
        Profile teammate = profile(3L, "teammate");
        Session session = session(10L, creator);
        stubActor(session, sender);

        when(participantRepository.existsBySessionIdAndProfileIdAndState(
                10L, 2L, ParticipantState.ACTIVE)).thenReturn(true);
        when(participantRepository.findBySessionIdAndStateOrderByJoinedAtAsc(
                10L, ParticipantState.ACTIVE)).thenReturn(List.of(
                SessionParticipant.builder()
                        .session(session)
                        .profile(sender)
                        .state(ParticipantState.ACTIVE)
                        .build(),
                SessionParticipant.builder()
                        .session(session)
                        .profile(teammate)
                        .state(ParticipantState.ACTIVE)
                        .build()
        ));
        when(messageRepository.save(any(SessionMessage.class)))
                .thenAnswer(invocation -> {
                    SessionMessage saved = invocation.getArgument(0);
                    saved.setId(41L);
                    return saved;
                });

        service.post(10L, "bora", "miguel");

        ArgumentCaptor<SessionChatMessageCreatedEvent> eventCaptor =
                ArgumentCaptor.forClass(SessionChatMessageCreatedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        SessionChatMessageCreatedEvent event = eventCaptor.getValue();
        assertEquals(41L, event.message().id());
        assertEquals(Set.of("creator", "miguel", "teammate"), event.recipients());
    }

    @Test
    void removedParticipantCannotPost() {
        Profile creator = profile(1L, "creator");
        Profile removed = profile(2L, "removed");
        Session session = session(10L, creator);
        stubActor(session, removed);
        when(participantRepository.existsBySessionIdAndProfileIdAndState(
                10L, 2L, ParticipantState.ACTIVE)).thenReturn(false);

        assertThrows(AccessDeniedException.class,
                () -> service.post(10L, "still here?", "removed"));

        verify(messageRepository, never()).save(any());
    }

    @Test
    void messageLongerThanTwoThousandCharactersIsRejected() {
        Profile creator = profile(1L, "creator");
        Session session = session(10L, creator);
        stubActor(session, creator);

        assertThrows(IllegalArgumentException.class,
                () -> service.post(10L, "x".repeat(2001), "creator"));

        verify(messageRepository, never()).save(any());
    }

    private void stubActor(Session session, Profile actor) {
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsername(actor.getUsername())).thenReturn(Optional.of(actor));
    }

    private Profile profile(Long id, String username) {
        Profile profile = new Profile();
        profile.setId(id);
        profile.setUsername(username);
        return profile;
    }

    private Session session(Long id, Profile creator) {
        Session session = new Session();
        session.setId(id);
        session.setProfile(creator);
        return session;
    }
}
