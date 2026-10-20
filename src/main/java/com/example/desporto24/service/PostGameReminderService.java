package com.example.desporto24.service;

import com.example.desporto24.enumeration.NotificationType;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * Create an in-app reminder when an organiser next checks notifications.
 * A persisted marker prevents repeated prompts even if the notification is deleted.
 */
@Service
@RequiredArgsConstructor
public class PostGameReminderService {

    private static final Duration GAME_LENGTH = Duration.ofHours(2);
    private static final Duration REVIEW_WINDOW = Duration.ofDays(7);
    private static final int BATCH_SIZE = 25;

    private final SessionRepository sessionRepository;
    private final NotificationsRepository notificationsRepository;

    @Transactional
    public void createDueReminders(String username) {
        Instant now = Instant.now();
        for (Long sessionId : sessionRepository.findSessionsAwaitingPostGameReview(
                username, Date.from(now.minus(GAME_LENGTH)),
                Date.from(now.minus(REVIEW_WINDOW)), PageRequest.of(0, BATCH_SIZE))) {
            // The same row lock used by session completion/cancellation prevents a stale notice.
            Session session = sessionRepository.findByIdForUpdate(sessionId).orElse(null);
            if (!eligible(session, username, now)) {
                continue;
            }
            Profile organiser = session.getProfile();
            Notifications notification = new Notifications();
            notification.setSubject("Como correu o teu jogo?");
            notification.setMessage(
                    "O horário da tua sessão de " + session.getSport()
                    + " já passou. Se quiseres, regista as presenças. "
                    + "Se o jogo não aconteceu, assinala «Não aconteceu» na sessão.");
            notification.setGreeting("");
            notification.setSignature("");
            notification.setCreatedAt(now.toString());
            notification.setFriendRequest(false);
            notification.setUpdateProfile(false);
            notification.setSessionInvite(false);
            notification.setNormalMessage(false);
            notification.setToken(null);
            notification.setUsername(organiser.getUsername());
            notification.setType(NotificationType.SESSION_POST_GAME_REVIEW);
            notification.setSessionId(session.getId());
            notification.setRead(false);

            session.setPostGameReviewReminderSentAt(now);
            sessionRepository.save(session);
            notificationsRepository.save(notification);
        }
    }

    static boolean eligible(Session session, String username, Instant now) {
        if (session == null || session.getProfile() == null
                || !username.equals(session.getProfile().getUsername())
                || session.getPostGameReviewReminderSentAt() != null
                || session.getGameDate() == null
                || (session.getStatus() != SessionStatus.OPEN
                    && session.getStatus() != SessionStatus.FULL)) {
            return false;
        }
        Instant scheduled = session.getGameDate().toInstant();
        return !scheduled.isAfter(now.minus(GAME_LENGTH))
                && !scheduled.isBefore(now.minus(REVIEW_WINDOW));
    }
}
