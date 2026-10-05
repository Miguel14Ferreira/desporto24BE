package com.example.desporto24.repository.session;

import com.example.desporto24.enumeration.DisputeStatus;
import com.example.desporto24.model.session.AttendanceDispute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface AttendanceDisputeRepository extends JpaRepository<AttendanceDispute, Long> {
    boolean existsByParticipantIdAndStatusIn(Long participantId, Collection<DisputeStatus> statuses);
    boolean existsByParticipantIdAndStatus(Long participantId, DisputeStatus status);
    Optional<AttendanceDispute> findTopByParticipantIdOrderByCreatedAtDesc(Long participantId);
    java.util.List<AttendanceDispute> findByParticipantIdInOrderByCreatedAtDesc(Collection<Long> participantIds);
    java.util.List<AttendanceDispute> findByStatusOrderByCreatedAtAsc(DisputeStatus status);
}
