package com.example.desporto24.repository.session;

import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.model.session.SessionParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionParticipantRepository extends JpaRepository<SessionParticipant, Long> {

    Optional<SessionParticipant> findBySessionIdAndProfileIdAndState(
            Long sessionId, Long profileId, ParticipantState state);

    long countBySessionIdAndState(Long sessionId, ParticipantState state);

    boolean existsBySessionIdAndProfileIdAndState(
            Long sessionId, Long profileId, ParticipantState state);

    List<SessionParticipant> findBySessionIdAndStateOrderByJoinedAtAsc(
            Long sessionId, ParticipantState state);
}
