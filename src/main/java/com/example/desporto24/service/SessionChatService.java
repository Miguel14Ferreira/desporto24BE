package com.example.desporto24.service;

import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.exception.SessionNotFound;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.session.SessionMessageResponse;
import com.example.desporto24.model.session.SessionMessage;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionMessageRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.example.desporto24.websocket.SessionChatMessageCreatedEvent;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Service
@RequiredArgsConstructor
public class SessionChatService {

    private final SessionRepository sessionRepository;
    private final ProfileRepository profileRepository;
    private final SessionParticipantRepository participantRepository;
    private final SessionMessageRepository messageRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<SessionMessageResponse> messages(Long sessionId, String username) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile actor = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        requireChatAccess(session, actor);

        List<SessionMessage> recentMessages =
                messageRepository.findBySessionIdOrderByCreatedAtDesc(
                        sessionId,
                        PageRequest.of(0, 200)
                );

        java.util.Collections.reverse(recentMessages);
        return recentMessages.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SessionMessageResponse post(Long sessionId, String body, String username) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile actor = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        requireChatAccess(session, actor);

        String normalized = body == null ? "" : body.trim();
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Message body is required");
        }
        if (normalized.length() > 2000) {
            throw new IllegalArgumentException("Message body must not exceed 2000 characters");
        }

        SessionMessage saved = messageRepository.save(SessionMessage.builder()
                .session(session)
                .senderProfile(actor)
                .body(normalized)
                .createdAt(Instant.now())
                .build());

        SessionMessageResponse response = toResponse(saved);
        eventPublisher.publishEvent(new SessionChatMessageCreatedEvent(
                response,
                realtimeRecipients(session)
        ));

        return response;
    }

    private Set<String> realtimeRecipients(Session session) {
        Set<String> recipients = new LinkedHashSet<>();

        if (session.getProfile() != null && session.getProfile().getUsername() != null) {
            recipients.add(session.getProfile().getUsername());
        }

        participantRepository
                .findBySessionIdAndStateOrderByJoinedAtAsc(session.getId(), ParticipantState.ACTIVE)
                .stream()
                .map(participant -> participant.getProfile().getUsername())
                .filter(username -> username != null && !username.isBlank())
                .forEach(recipients::add);

        return recipients;
    }

    private void requireChatAccess(Session session, Profile actor) {
        boolean creator = session.getProfile() != null
                && session.getProfile().getId().equals(actor.getId());
        boolean participant = participantRepository.existsBySessionIdAndProfileIdAndState(
                session.getId(), actor.getId(), ParticipantState.ACTIVE);

        if (!creator && !participant) {
            throw new AccessDeniedException("Session chat is limited to participants");
        }
    }

    private SessionMessageResponse toResponse(SessionMessage message) {
        return new SessionMessageResponse(
                message.getId(),
                message.getSession().getId(),
                message.getSenderProfile().getUsername(),
                message.getBody(),
                message.getCreatedAt()
        );
    }
}
