package com.example.desporto24.repository.session;

import com.example.desporto24.enumeration.SessionInviteStatus;
import com.example.desporto24.model.session.SessionInvite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionInviteRepository extends JpaRepository<SessionInvite, Long> {
    Optional<SessionInvite> findBySessionIdAndInviteeIdAndStatus(
            Long sessionId, Long inviteeId, SessionInviteStatus status);

    List<SessionInvite> findByInviteeIdAndStatusOrderByCreatedAtDesc(
            Long inviteeId, SessionInviteStatus status);
}
