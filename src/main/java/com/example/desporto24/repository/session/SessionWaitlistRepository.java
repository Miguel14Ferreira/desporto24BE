package com.example.desporto24.repository.session;

import com.example.desporto24.model.session.SessionWaitlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionWaitlistRepository extends JpaRepository<SessionWaitlist, Long> {

    Optional<SessionWaitlist> findBySessionIdAndProfileId(Long sessionId, Long profileId);

    Optional<SessionWaitlist> findFirstBySessionIdOrderByPriorityTierAscCreatedAtAsc(Long sessionId);

    List<SessionWaitlist> findBySessionIdOrderByPriorityTierAscCreatedAtAsc(Long sessionId);

    List<SessionWaitlist> findByProfileIdOrderByCreatedAtAsc(Long profileId);
}
