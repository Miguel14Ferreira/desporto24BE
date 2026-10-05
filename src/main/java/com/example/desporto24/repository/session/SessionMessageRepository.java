package com.example.desporto24.repository.session;

import com.example.desporto24.model.session.SessionMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface SessionMessageRepository extends JpaRepository<SessionMessage, Long> {
    List<SessionMessage> findBySessionIdOrderByCreatedAtAsc(Long sessionId);
    List<SessionMessage> findBySessionIdOrderByCreatedAtDesc(Long sessionId, Pageable pageable);

    long deleteBySenderProfileId(Long senderProfileId);
}
