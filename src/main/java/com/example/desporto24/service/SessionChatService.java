package com.example.desporto24.service;

import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.enumeration.SessionProblemType;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.example.desporto24.websocket.SessionChatMessageCreatedEvent;
import com.example.desporto24.storage.PrivateChatFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Service
@RequiredArgsConstructor
public class SessionChatService {

    private final SessionRepository sessionRepository;
    private final ProfileRepository profileRepository;
    private final SessionParticipantRepository participantRepository;
    private final SessionMessageRepository messageRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired(required = false)
    private PrivateChatFileStorage privateChatStorage;

    @Transactional(readOnly = true)
    public List<SessionMessageResponse> messages(Long sessionId, String username) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile actor = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        requireChatAccess(session, actor);
        if (!isChatOpen(session, Instant.now())) {
            throw new AccessDeniedException("Session chat is available only before the game");
        }

        return messageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SessionMessageResponse post(Long sessionId, String body, String username) {
        return post(sessionId, body, username, null);
    }

    @Transactional
    public SessionMessageResponse post(Long sessionId, String body, String username, Long replyToMessageId) {
        // Serialize with cancellation/completion so closed sessions cannot receive new messages.
        Session session = sessionRepository.findByIdForUpdate(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile actor = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        requireChatAccess(session, actor);
        if (!isChatOpen(session, Instant.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Session chat is available only before the game");
        }

        String normalized = body == null ? "" : body.trim();
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Message body is required");
        }
        if (normalized.length() > 2000) {
            throw new IllegalArgumentException("Message body must not exceed 2000 characters");
        }

        SessionMessage quoted = null;
        if (replyToMessageId != null) {
            if (replyToMessageId <= 0) {
                throw new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.BAD_REQUEST, "Invalid reply message");
            }
            quoted = messageRepository.findById(replyToMessageId)
                    .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                            org.springframework.http.HttpStatus.BAD_REQUEST, "Invalid reply message"));
            if (!sessionId.equals(quoted.getSession().getId())) {
                throw new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.BAD_REQUEST, "Invalid reply message");
            }
        }

        SessionMessage saved = messageRepository.save(SessionMessage.builder()
                .session(session)
                .senderProfile(actor)
                .body(normalized)
                .replyToMessageId(quoted == null ? null : quoted.getId())
                .replyToSender(quoted == null ? null : quoted.getSenderProfile().getUsername())
                .replyToText(quoted == null ? null : quoted.getBody())
                .createdAt(Instant.now())
                .build());

        SessionMessageResponse response = toResponse(saved);
        eventPublisher.publishEvent(new SessionChatMessageCreatedEvent(
                response,
                realtimeRecipients(session)
        ));

        return response;
    }

    /**
     * Session attachments are available only while the session is OPEN,
     * and only to its organizer and active participants.
     */
    @Transactional(readOnly = true)
    public boolean attachmentsAvailable(Long sessionId, String username) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile actor = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        requireChatAccess(session, actor);
        return isChatOpen(session, Instant.now())
                && privateChatStorage != null && privateChatStorage.isConfigured();
    }

    /**
     * Uses the same row lock as text posting and session cancellation. Recheck
     * kickoff AFTER upload: a slow object store must not reopen a closed chat.
     */
    @Transactional
    public SessionMessageResponse postAttachment(Long sessionId, MultipartFile file,
            String caption, String username, Long replyToMessageId) {
        Session session = sessionRepository.findByIdForUpdate(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile actor = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        requireChatAccess(session, actor);
        if (!isChatOpen(session, Instant.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Session chat is available only before the game");
        }
        if (privateChatStorage == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Private chat attachment storage is unavailable");
        }
        String body = caption == null ? "" : caption.trim();
        if (body.length() > 2000 || replyToMessageId != null && replyToMessageId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid attachment message");
        }
        SessionMessage quoted = null;
        if (replyToMessageId != null) {
            quoted = messageRepository.findById(replyToMessageId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Invalid reply message"));
            if (!sessionId.equals(quoted.getSession().getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reply message");
            }
        }
        var stored = privateChatStorage.store(file);
        try {
            if (!isChatOpen(session, Instant.now())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Session chat closed while uploading attachment");
            }
            SessionMessage saved = messageRepository.save(SessionMessage.builder()
                    .session(session).senderProfile(actor)
                    .body(body.isEmpty() ? "📎 " + stored.name() : body)
                    .replyToMessageId(quoted == null ? null : quoted.getId())
                    .replyToSender(quoted == null ? null : quoted.getSenderProfile().getUsername())
                    .replyToText(quoted == null ? null : quoted.getBody())
                    .attachmentKey(stored.key()).attachmentName(stored.name())
                    .attachmentMimeType(stored.mime()).attachmentSize(stored.size())
                    .createdAt(Instant.now()).build());
            messageRepository.flush();
            SessionMessageResponse response = toResponse(saved);
            eventPublisher.publishEvent(new SessionChatMessageCreatedEvent(
                    response, realtimeRecipients(session)));
            return response;
        } catch (RuntimeException failure) {
            try { privateChatStorage.delete(stored.key()); }
            catch (RuntimeException cleanup) { failure.addSuppressed(cleanup); }
            throw failure;
        }
    }

    /** Never expose attachments after kickoff, even to a former participant. */
    @Transactional(readOnly = true)
    public ChatAttachmentContent downloadAttachment(Long sessionId, Long messageId, String username) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile actor = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        requireChatAccess(session, actor);
        if (!isChatOpen(session, Instant.now())) {
            throw new AccessDeniedException("Session chat is available only before the game");
        }
        if (messageId == null || messageId <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat attachment not found");
        }
        SessionMessage message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Chat attachment not found"));
        if (!sessionId.equals(message.getSession().getId()) || message.getAttachmentKey() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat attachment not found");
        }
        if (privateChatStorage == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Private chat attachment storage is unavailable");
        }
        return new ChatAttachmentContent(privateChatStorage.read(message.getAttachmentKey()),
                message.getAttachmentMimeType(), message.getAttachmentName());
    }

    public record ChatAttachmentContent(byte[] content, String mimeType, String filename) {}

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

    static boolean isChatOpen(Session session, Instant now) {
        if (session.getStatus() == SessionStatus.COMPLETED
                || session.getStatus() == SessionStatus.CANCELLED
                || session.getProblemType() == SessionProblemType.NOT_OCCURRED
                || Boolean.FALSE.equals(session.getEnabled())) {
            return false;
        }

        // Session chat exists only before kick-off. Missing dates fail closed.
        return session.getGameDate() != null
                && session.getGameDate().toInstant().isAfter(now);
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
                message.getCreatedAt(),
                message.getReplyToMessageId(),
                message.getReplyToSender(),
                message.getReplyToText(),
                message.getAttachmentName(),
                message.getAttachmentMimeType(),
                message.getAttachmentSize()
        );
    }
}
