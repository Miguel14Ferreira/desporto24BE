package com.example.desporto24.service;

import com.example.desporto24.enumeration.NotificationType;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostGameReminderServiceTest {
    @Mock private SessionRepository sessionRepository;
    @Mock private NotificationsRepository notificationsRepository;
    @InjectMocks private PostGameReminderService service;

    @Test
    void createsOneLinkedUnreadReminderAndDoesNotRepeat() {
        Session session = session(SessionStatus.OPEN, Instant.now().minusSeconds(3 * 3600));
        when(sessionRepository.findSessionsAwaitingPostGameReview(
                eq("creator"), any(Date.class), any(Date.class), any(Pageable.class)))
                .thenReturn(List.of(11L));
        when(sessionRepository.findByIdForUpdate(11L)).thenReturn(Optional.of(session));

        service.createDueReminders("creator");
        service.createDueReminders("creator");

        ArgumentCaptor<Notifications> sent = ArgumentCaptor.forClass(Notifications.class);
        verify(notificationsRepository, times(1)).save(sent.capture());
        assertEquals(NotificationType.SESSION_POST_GAME_REVIEW, sent.getValue().getType());
        assertEquals(11L, sent.getValue().getSessionId());
        assertEquals("creator", sent.getValue().getUsername());
        assertEquals("Como correu o teu jogo?", sent.getValue().getSubject());
        assertTrue(sent.getValue().getMessage().contains("Se quiseres, regista as presenças"));
        assertTrue(sent.getValue().getMessage().contains("assinala «Não aconteceu»"));
        assertFalse(sent.getValue().getMessage().contains("Confirma se o jogo aconteceu"));
        assertFalse(sent.getValue().isRead());
        assertNotNull(session.getPostGameReviewReminderSentAt());
    }

    @Test
    void neverPromptsForFutureCancelledOrCompletedGames() {
        Instant now = Instant.now();
        assertFalse(PostGameReminderService.eligible(
                session(SessionStatus.OPEN, now.minusSeconds(3600)), "creator", now));
        assertFalse(PostGameReminderService.eligible(
                session(SessionStatus.CANCELLED, now.minusSeconds(3 * 3600)), "creator", now));
        assertFalse(PostGameReminderService.eligible(
                session(SessionStatus.COMPLETED, now.minusSeconds(3 * 3600)), "creator", now));
        assertFalse(PostGameReminderService.eligible(
                session(SessionStatus.OPEN, now.minusSeconds(8 * 86400)), "creator", now));
        assertFalse(PostGameReminderService.eligible(
                session(SessionStatus.FULL, now.minusSeconds(3 * 3600)), "other", now));
    }

    @Test
    void rechecksStatusUnderLockBeforeSendingReminder() {
        Session candidate = session(SessionStatus.OPEN, Instant.now().minusSeconds(3 * 3600));
        Session cancelled = session(SessionStatus.CANCELLED, candidate.getGameDate().toInstant());
        when(sessionRepository.findSessionsAwaitingPostGameReview(
                eq("creator"), any(Date.class), any(Date.class), any(Pageable.class)))
                .thenReturn(List.of(11L));
        when(sessionRepository.findByIdForUpdate(11L)).thenReturn(Optional.of(cancelled));

        service.createDueReminders("creator");

        verify(notificationsRepository, never()).save(any());
    }

    private Session session(SessionStatus status, Instant gameDate) {
        Profile creator = new Profile();
        creator.setUsername("creator");
        Session session = new Session();
        session.setId(11L);
        session.setProfile(creator);
        session.setSport("Futebol");
        session.setGameDate(Date.from(gameDate));
        session.setStatus(status);
        return session;
    }
}
