package com.example.desporto24.service;

import com.example.desporto24.enumeration.NotificationType;
import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.enumeration.SessionInviteStatus;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.session.SessionInvite;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionInviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SessionInviteService {
    private final SessionRepository sessions;
    private final ProfileRepository profiles;
    private final SessionInviteRepository invites;
    private final NotificationsRepository notifications;

    @Transactional
    public Long invite(Long sessionId, String organizerUsername, String recipientUsername) {
        if (recipientUsername == null || recipientUsername.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Indica um jogador.");
        }
        Session session = sessions.findByIdForUpdate(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sessão não encontrada."));
        Profile actor = profiles.findUserByUsername(organizerUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        if (!session.getProfile().getId().equals(actor.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas o organizador pode enviar convites.");
        }
        if (session.getStatus() == SessionStatus.CANCELLED
                || session.getStatus() == SessionStatus.COMPLETED
                || session.getGameDate() == null
                || !session.getGameDate().toInstant().isAfter(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A sessão já não aceita convites.");
        }
        if (session.getAudience() != SessionAudience.PUBLIC || Boolean.TRUE.equals(session.getPrivateGame())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Os convites estão disponíveis apenas para sessões públicas.");
        }
        Profile recipient = profiles.findUserByUsernameIgnoreCase(recipientUsername.trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador não encontrado."));
        if (recipient.getId().equals(actor.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não podes convidar-te a ti próprio.");
        }
        if (invites.findBySessionIdAndInviteeIdAndStatus(sessionId, recipient.getId(), SessionInviteStatus.PENDING).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um convite pendente.");
        }

        SessionInvite invite = new SessionInvite();
        invite.setSession(session);
        invite.setInviter(actor);
        invite.setInvitee(recipient);
        invite.setStatus(SessionInviteStatus.PENDING);
        invite.setCreatedAt(Instant.now());
        Long inviteId = invites.save(invite).getId();

        Notifications notification = new Notifications();
        notification.setSubject("Convite para uma sessão desportiva");
        notification.setMessage("@" + actor.getUsername() + " convidou-te para uma sessão. Abre a sessão para veres os detalhes.");
        notification.setGreeting("");
        notification.setSignature("");
        notification.setCreatedAt(Instant.now().toString());
        notification.setUsername(recipient.getUsername());
        notification.setRelatedUsername(actor.getUsername());
        notification.setSessionInvite(true);
        notification.setSessionId(sessionId);
        notification.setType(NotificationType.SESSION_INVITE);
        notification.setRead(false);
        notifications.save(notification);
        return inviteId;
    }
}
