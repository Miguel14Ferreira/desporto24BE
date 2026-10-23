package com.example.desporto24.repository.session;

import com.example.desporto24.model.session.SessionMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface SessionMessageRepository extends JpaRepository<SessionMessage, Long> {
    List<SessionMessage> findBySessionIdOrderByCreatedAtAsc(Long sessionId);

    List<SessionMessage> findBySessionIdOrderByIdDesc(Long sessionId, Pageable pageable);

    @Query("""
            select count(m) from SessionMessage m
            where m.session.id = :sessionId and m.id > :lastReadId
              and m.senderProfile.id <> :readerId
            """)
    long countUnread(@Param("sessionId") Long sessionId, @Param("readerId") Long readerId,
                     @Param("lastReadId") long lastReadId);

    long deleteBySenderProfileId(Long senderProfileId);
}
