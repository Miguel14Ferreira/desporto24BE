package com.example.desporto24.repository.session;

import com.example.desporto24.model.session.SessionWaitlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionWaitlistRepository extends JpaRepository<SessionWaitlist, Long> {

    Optional<SessionWaitlist> findBySessionIdAndProfileId(Long sessionId, Long profileId);


    @Query("""
            select w.session.id
            from SessionWaitlist w
            where w.session.id in :sessionIds
              and w.profile.id = :profileId
            """)
    List<Long> findSessionIdsBySessionIdsAndProfileId(
            @Param("sessionIds") java.util.Collection<Long> sessionIds,
            @Param("profileId") Long profileId);

    Optional<SessionWaitlist> findFirstBySessionIdOrderByPriorityTierAscCreatedAtAsc(Long sessionId);

    List<SessionWaitlist> findBySessionIdOrderByPriorityTierAscCreatedAtAsc(Long sessionId);

    List<SessionWaitlist> findByProfileIdOrderByCreatedAtAsc(Long profileId);

    long deleteByProfileId(Long profileId);
}
