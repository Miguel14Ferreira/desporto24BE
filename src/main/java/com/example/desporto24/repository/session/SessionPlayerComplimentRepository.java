package com.example.desporto24.repository.session;

import com.example.desporto24.model.session.SessionPlayerCompliment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionPlayerComplimentRepository extends JpaRepository<SessionPlayerCompliment, Long> {
    Optional<SessionPlayerCompliment> findBySessionIdAndSenderIdAndRecipientId(
            Long sessionId, Long senderId, Long recipientId);

    List<SessionPlayerCompliment> findBySessionIdAndSenderIdOrderByUpdatedAtDesc(
            Long sessionId, Long senderId);

    List<SessionPlayerCompliment> findBySessionIdAndRecipientIdOrderByUpdatedAtDesc(
            Long sessionId, Long recipientId);
}
