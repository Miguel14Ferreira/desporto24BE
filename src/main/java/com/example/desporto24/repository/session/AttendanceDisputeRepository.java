package com.example.desporto24.repository.session;

import com.example.desporto24.enumeration.DisputeStatus;
import com.example.desporto24.model.session.AttendanceDispute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface AttendanceDisputeRepository extends JpaRepository<AttendanceDispute, Long> {
    boolean existsByParticipantIdAndStatusIn(Long participantId, Collection<DisputeStatus> statuses);
    boolean existsByParticipantIdAndStatus(Long participantId, DisputeStatus status);

    @Query("""
            select distinct dispute.participant.id
            from AttendanceDispute dispute
            where dispute.participant.id in :participantIds
              and dispute.status in :statuses
            """)
    java.util.List<Long> findParticipantIdsByParticipantIdsAndStatusIn(
            @Param("participantIds") Collection<Long> participantIds,
            @Param("statuses") Collection<DisputeStatus> statuses);
    Optional<AttendanceDispute> findTopByParticipantIdOrderByCreatedAtDesc(Long participantId);
    java.util.List<AttendanceDispute> findByParticipantIdInOrderByCreatedAtDesc(Collection<Long> participantIds);
    java.util.List<AttendanceDispute> findByStatusOrderByCreatedAtAsc(DisputeStatus status);

    @EntityGraph(attributePaths = {
            "participant",
            "participant.session",
            "reporterProfile",
            "disputingProfile"
    })
    java.util.List<AttendanceDispute> findByStatusOrderByCreatedAtAsc(
            DisputeStatus status,
            Pageable pageable);
}
