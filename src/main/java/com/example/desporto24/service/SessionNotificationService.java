package com.example.desporto24.service;

import com.example.desporto24.enumeration.NotificationType;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionWaitlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SessionNotificationService {

    private final NotificationsRepository notificationsRepository;
    private final SessionParticipantRepository participantRepository;
    private final SessionWaitlistRepository waitlistRepository;

    @Transactional
    public void notifyWaitlistPromoted(Session session, Profile profile) {
        save(profile, session, NotificationType.WAITLIST_PROMOTED,
                "Vaga disponível",
                "Foste promovido da lista de espera para participante.");
    }

    @Transactional
    public void notifyParticipantRemoved(Session session, Profile profile) {
        save(profile, session, NotificationType.SESSION_REMOVED,
                "Removido da sessão",
                "O criador removeu-te desta sessão.");
    }

    @Transactional
    public void notifySessionCancelled(Session session) {
        Map<String, Profile> affected = new LinkedHashMap<>();

        participantRepository.findBySessionIdAndStateOrderByJoinedAtAsc(
                        session.getId(), ParticipantState.ACTIVE)
                .stream()
                .map(p -> p.getProfile())
                .filter(p -> p != null && p.getUsername() != null)
                .forEach(p -> affected.putIfAbsent(p.getUsername(), p));

        waitlistRepository.findBySessionIdOrderByPriorityTierAscCreatedAtAsc(session.getId())
                .stream()
                .map(w -> w.getProfile())
                .filter(p -> p != null && p.getUsername() != null)
                .forEach(p -> affected.putIfAbsent(p.getUsername(), p));

        if (session.getProfile() != null && session.getProfile().getUsername() != null) {
            affected.remove(session.getProfile().getUsername());
        }

        affected.values().forEach(profile -> save(
                profile,
                session,
                NotificationType.SESSION_CANCELLED,
                "Sessão cancelada",
                "O criador cancelou esta sessão."
        ));
    }

    private void save(
            Profile profile,
            Session session,
            NotificationType type,
            String subject,
            String message) {

        Notifications notification = new Notifications();
        notification.setSubject(subject);
        notification.setMessage(message);
        notification.setGreeting("");
        notification.setSignature("");
        notification.setCreatedAt(Instant.now().toString());
        notification.setFriendRequest(false);
        notification.setUpdateProfile(false);
        notification.setSessionInvite(false);
        notification.setNormalMessage(false);
        notification.setToken(null);
        notification.setUsername(profile.getUsername());
        notification.setType(type);
        notification.setSessionId(session.getId());
        notification.setRead(false);

        notificationsRepository.save(notification);
    }
}
