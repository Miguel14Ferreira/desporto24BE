package com.example.desporto24.service;

import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.enumeration.SessionProblemType;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
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
    private SessionGuestRequestService guestRequestService;

    @BeforeEach
    void setUp() {
        sessionRepository = mock(SessionRepository.class);
        profileRepository = mock(ProfileRepository.class);
        participantRepository = mock(SessionParticipantRepository.class);
        messageRepository = mock(SessionMessageRepository.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        guestRequestService = mock(SessionGuestRequestService.class);
        service = new SessionChatService(
                sessionRepository,
                profileRepository,
                participantRepository,
                messageRepository,
                eventPublisher,
                guestRequestService
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
        when(messageRepository.findBySessionIdOrderByCreatedAtAsc(10L))
                .thenReturn(List.of(message));

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
    void chatClosesAtKickoffAndHistoryIsNotExposedThroughActiveChatEndpoint() {
        Profile creator = profile(1L, "creator");
        Session session = session(10L, creator);
        session.setGameDate(Date.from(Instant.now().minusSeconds(1)));
        stubActor(session, creator);
        assertThrows(ResponseStatusException.class,
                () -> service.post(10L, "new message", "creator"));
        assertThrows(AccessDeniedException.class,
                () -> service.messages(10L, "creator"));
        verify(messageRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void startedGameNeverAcceptsNewMessagesEvenDuringFormerGracePeriod() {
        Profile creator = profile(1L, "creator");
        Session session = session(10L, creator);
        session.setGameDate(Date.from(Instant.now().minusSeconds(30 * 60)));
        stubActor(session, creator);
        when(messageRepository.save(any(SessionMessage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertThrows(ResponseStatusException.class,
                () -> service.post(10L, "still playing", "creator"));
        verify(messageRepository, never()).save(any(SessionMessage.class));
    }

    @Test
    void closingOrCancellingTheSessionImmediatelyDisablesNewMessages() {
        Profile creator = profile(1L, "creator");
        Session session = session(10L, creator);
        session.setGameDate(Date.from(Instant.now().plusSeconds(7200)));
        stubActor(session, creator);

        for (SessionStatus status : new SessionStatus[] {
                SessionStatus.COMPLETED, SessionStatus.CANCELLED
        }) {
            session.setStatus(status);
            assertThrows(ResponseStatusException.class,
                    () -> service.post(10L, "after the game", "creator"));
        }

        session.setStatus(SessionStatus.OPEN);
        session.setProblemType(SessionProblemType.NOT_OCCURRED);
        assertThrows(ResponseStatusException.class,
                () -> service.post(10L, "game did not happen", "creator"));
        verify(messageRepository, never()).save(any());
    }

    @Test
    void expiredChatPostReturnsConflictInsteadOfInternalServerError() {
        Profile creator = profile(1L, "creator");
        Session session = session(10L, creator);
        session.setGameDate(Date.from(Instant.now().minusSeconds(2)));
        stubActor(session, creator);

        ResponseStatusException blocked = assertThrows(ResponseStatusException.class,
                () -> service.post(10L, "too late", "creator"));
        assertEquals(HttpStatus.CONFLICT, blocked.getStatusCode());
        verifyNoInteractions(messageRepository);
        verify(eventPublisher, never()).publishEvent(any());
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

    @Test
    void sessionReplyKeepsVerifiedQuoteAndRejectsAnotherSession() {
        Profile creator = profile(1L, "creator");
        Profile teammate = profile(2L, "teammate");
        Session session = session(10L, creator);
        stubActor(session, creator);
        SessionMessage parent = SessionMessage.builder().id(12L).session(session)
                .senderProfile(teammate).body("Traz uma bola?").createdAt(Instant.now()).build();
        when(messageRepository.findById(12L)).thenReturn(Optional.of(parent));
        when(messageRepository.save(any(SessionMessage.class))).thenAnswer(inv -> {
            SessionMessage saved = inv.getArgument(0);
            saved.setId(13L);
            return saved;
        });
        var response = service.post(10L, "Claro", "creator", 12L);
        assertEquals(12L, response.replyToMessageId());
        assertEquals("teammate", response.replyToSender());
        assertEquals("Traz uma bola?", response.replyToText());
        verify(messageRepository).save(argThat(saved -> "Claro".equals(saved.getBody())
                && saved.getReplyToMessageId().equals(12L)));

        parent.setSession(session(99L, creator));
        assertEquals(400, assertThrows(org.springframework.web.server.ResponseStatusException.class,
                () -> service.post(10L, "Citação estranha", "creator", 12L)).getStatusCode().value());
        verify(messageRepository, times(1)).save(any());
    }

    @Test
    void invalidOrMissingSessionReplyCannotPersistOrBroadcast() {
        Profile creator = profile(1L, "creator");
        Session session = session(10L, creator);
        stubActor(session, creator);
        when(messageRepository.findById(66L)).thenReturn(Optional.empty());
        assertEquals(400, assertThrows(org.springframework.web.server.ResponseStatusException.class,
                () -> service.post(10L, "Olá", "creator", 66L)).getStatusCode().value());
        assertEquals(400, assertThrows(org.springframework.web.server.ResponseStatusException.class,
                () -> service.post(10L, "Olá", "creator", -3L)).getStatusCode().value());
        verify(messageRepository, never()).save(any());
        verifyNoInteractions(eventPublisher);
    }


    private com.example.desporto24.storage.PrivateChatFileStorage privateStorage() {
        var storage = mock(com.example.desporto24.storage.PrivateChatFileStorage.class);
        org.springframework.test.util.ReflectionTestUtils.setField(service, "privateChatStorage", storage);
        return storage;
    }

    @Test
    void outsiderCannotProbeStoreOrDownloadSessionAttachments() {
        Profile creator = profile(1L, "creator");
        Profile outsider = profile(2L, "outsider");
        Session session = session(10L, creator);
        stubActor(session, outsider);
        var storage = privateStorage();
        var file = new org.springframework.mock.web.MockMultipartFile(
                "file", "foto.jpg", "image/jpeg", new byte[] {1});
        assertThrows(AccessDeniedException.class,
                () -> service.attachmentsAvailable(10L, "outsider"));
        assertThrows(AccessDeniedException.class,
                () -> service.postAttachment(10L, file, "", "outsider", null));
        assertThrows(AccessDeniedException.class,
                () -> service.downloadAttachment(10L, 90L, "outsider"));
        verifyNoInteractions(storage, messageRepository);
    }

    @Test
    void participantSeesPrivateAttachmentControlsOnlyWhenBucketIsAvailable() {
        Profile creator = profile(1L, "creator");
        Profile teammate = profile(2L, "teammate");
        Session session = session(10L, creator);
        stubActor(session, teammate);
        when(participantRepository.existsBySessionIdAndProfileIdAndState(
                10L, 2L, ParticipantState.ACTIVE)).thenReturn(true);
        var storage = privateStorage();
        when(storage.isConfigured()).thenReturn(false, true);
        assertFalse(service.attachmentsAvailable(10L, "teammate"));
        assertTrue(service.attachmentsAvailable(10L, "teammate"));
    }

    @Test
    void authorizedSessionFileIsPersistedWithVerifiedQuoteAndRealtimeAudience() {
        Profile creator = profile(1L, "creator");
        Profile teammate = profile(2L, "teammate");
        Session session = session(10L, creator);
        stubActor(session, teammate);
        when(participantRepository.existsBySessionIdAndProfileIdAndState(
                10L, 2L, ParticipantState.ACTIVE)).thenReturn(true);
        var storage = privateStorage();
        var file = new org.springframework.mock.web.MockMultipartFile(
                "file", "treino.pdf", "application/pdf", new byte[] {1, 2});
        String key = "private-chat/11111111-1111-1111-1111-111111111111.pdf";
        when(storage.store(file)).thenReturn(
                new com.example.desporto24.storage.PrivateChatFileStorage.StoredFile(
                        key, "treino.pdf", "application/pdf", 2L));
        var parent = SessionMessage.builder().id(15L).session(session).senderProfile(creator)
                .body("Traz o plano?").createdAt(Instant.now()).build();
        when(messageRepository.findById(15L)).thenReturn(Optional.of(parent));
        when(messageRepository.save(any())).thenAnswer(call -> {
            SessionMessage saved = call.getArgument(0);
            saved.setId(16L);
            return saved;
        });
        when(participantRepository.findBySessionIdAndStateOrderByJoinedAtAsc(
                10L, ParticipantState.ACTIVE)).thenReturn(List.of(
                SessionParticipant.builder().session(session).profile(teammate)
                        .state(ParticipantState.ACTIVE).build()));

        var response = service.postAttachment(10L, file, "", "teammate", 15L);
        assertEquals(16L, response.id());
        assertEquals(15L, response.replyToMessageId());
        assertEquals("Traz o plano?", response.replyToText());
        assertEquals("📎 treino.pdf", response.body());
        assertEquals("treino.pdf", response.attachmentName());
        assertEquals("application/pdf", response.attachmentMimeType());
        assertEquals(2L, response.attachmentSize());
        assertFalse(response.toString().contains(key));
        verify(messageRepository).save(argThat(saved -> key.equals(saved.getAttachmentKey())
                && saved.getSession().getId().equals(10L)));
        verify(messageRepository).flush();
        var emitted = ArgumentCaptor.forClass(SessionChatMessageCreatedEvent.class);
        verify(eventPublisher).publishEvent(emitted.capture());
        assertEquals(Set.of("creator", "teammate"), emitted.getValue().recipients());
        verify(storage, never()).delete(anyString());
    }

    @Test
    void kickoffDisablesAvailabilityUploadsAndDownloadsEvenForOrganizer() {
        Profile creator = profile(1L, "creator");
        Session session = session(10L, creator);
        session.setGameDate(Date.from(Instant.now().minusSeconds(1)));
        stubActor(session, creator);
        var storage = privateStorage();
        assertFalse(service.attachmentsAvailable(10L, "creator"));
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.postAttachment(10L,
                        new org.springframework.mock.web.MockMultipartFile("file", "x.pdf",
                                "application/pdf", new byte[]{1}),
                        "", "creator", null)).getStatusCode().value());
        assertThrows(AccessDeniedException.class,
                () -> service.downloadAttachment(10L, 42L, "creator"));
        verifyNoInteractions(storage, messageRepository);
    }

    @Test
    void delayedUploadCrossingKickoffIsDeletedWithoutPublishingMessage() {
        Profile creator = profile(1L, "creator");
        Session session = session(10L, creator);
        stubActor(session, creator);
        var storage = privateStorage();
        var file = new org.springframework.mock.web.MockMultipartFile(
                "file", "x.pdf", "application/pdf", new byte[]{1});
        String key = "private-chat/22222222-2222-2222-2222-222222222222.pdf";
        when(storage.store(file)).thenAnswer(call -> {
            session.setGameDate(Date.from(Instant.now().minusSeconds(1)));
            return new com.example.desporto24.storage.PrivateChatFileStorage.StoredFile(
                    key, "x.pdf", "application/pdf", 1L);
        });
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.postAttachment(10L, file, "", "creator", null))
                .getStatusCode().value());
        verify(storage).delete(key);
        verify(messageRepository, never()).save(any());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void foreignSessionReplyIsRejectedBeforeObjectStorage() {
        Profile creator = profile(1L, "creator");
        Session session = session(10L, creator);
        stubActor(session, creator);
        var storage = privateStorage();
        var parent = SessionMessage.builder().id(17L).session(session(99L, creator))
                .senderProfile(creator).body("Outra sessão").build();
        when(messageRepository.findById(17L)).thenReturn(Optional.of(parent));
        var file = new org.springframework.mock.web.MockMultipartFile(
                "file", "x.pdf", "application/pdf", new byte[]{1});
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.postAttachment(10L, file, "", "creator", 17L))
                .getStatusCode().value());
        verifyNoInteractions(storage);
        verify(messageRepository, never()).save(any());
    }

    @Test
    void downloadRejectsAttachmentFromOtherSessionBeforeReadingPrivateStorage() {
        Profile creator = profile(1L, "creator");
        Session session = session(10L, creator);
        stubActor(session, creator);
        var storage = privateStorage();
        String key = "private-chat/33333333-3333-3333-3333-333333333333.pdf";
        var message = SessionMessage.builder().id(18L).session(session(99L, creator))
                .senderProfile(creator).body("Anexo").attachmentKey(key)
                .attachmentName("x.pdf").attachmentMimeType("application/pdf").build();
        when(messageRepository.findById(18L)).thenReturn(Optional.of(message));
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.downloadAttachment(10L, 18L, "creator"))
                .getStatusCode().value());
        message.setSession(session);
        when(storage.read(key)).thenReturn(new byte[]{5, 7});
        var result = service.downloadAttachment(10L, 18L, "creator");
        assertArrayEquals(new byte[]{5, 7}, result.content());
        assertEquals("x.pdf", result.filename());
        verify(storage, times(1)).read(key);
    }

    @Test
    void failedSessionMessageSaveDeletesOrphanedPrivateFile() {
        Profile creator = profile(1L, "creator");
        Session session = session(10L, creator);
        stubActor(session, creator);
        var storage = privateStorage();
        var file = new org.springframework.mock.web.MockMultipartFile(
                "file", "x.pdf", "application/pdf", new byte[]{1});
        String key = "private-chat/44444444-4444-4444-4444-444444444444.pdf";
        when(storage.store(file)).thenReturn(
                new com.example.desporto24.storage.PrivateChatFileStorage.StoredFile(
                        key, "x.pdf", "application/pdf", 1L));
        when(messageRepository.save(any())).thenThrow(new IllegalStateException("DB unavailable"));
        assertThrows(IllegalStateException.class,
                () -> service.postAttachment(10L, file, "", "creator", null));
        verify(storage).delete(key);
        verifyNoInteractions(eventPublisher);
    }

    private void stubActor(Session session, Profile actor) {
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(sessionRepository.findByIdForUpdate(session.getId())).thenReturn(Optional.of(session));
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
        session.setGameDate(Date.from(Instant.now().plusSeconds(3600)));
        return session;
    }
}
