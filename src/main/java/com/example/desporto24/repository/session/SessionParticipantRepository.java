package com.example.desporto24.repository.session;

import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.model.session.SessionParticipant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface SessionParticipantRepository extends JpaRepository<SessionParticipant, Long> {

    Optional<SessionParticipant> findBySessionIdAndProfileIdAndState(
            Long sessionId, Long profileId, ParticipantState state);

    Optional<SessionParticipant> findBySessionIdAndProfileId(Long sessionId, Long profileId);

    long countBySessionIdAndState(Long sessionId, ParticipantState state);


    @Query("""
            select p.session.id, count(p.id)
            from SessionParticipant p
            where p.session.id in :sessionIds
              and p.state = :state
            group by p.session.id
            """)
    List<Object[]> countBySessionIdsAndState(
            @Param("sessionIds") Collection<Long> sessionIds,
            @Param("state") ParticipantState state);

    boolean existsBySessionIdAndProfileIdAndState(
            Long sessionId, Long profileId, ParticipantState state);


    @Query("""
            select p.session.id
            from SessionParticipant p
            where p.session.id in :sessionIds
              and p.profile.id = :profileId
              and p.state = :state
            """)
    List<Long> findSessionIdsBySessionIdsAndProfileIdAndState(
            @Param("sessionIds") Collection<Long> sessionIds,
            @Param("profileId") Long profileId,
            @Param("state") ParticipantState state);

    List<SessionParticipant> findBySessionIdAndStateOrderByJoinedAtAsc(
            Long sessionId, ParticipantState state);

    List<SessionParticipant> findByProfileIdAndStateOrderByJoinedAtAsc(
            Long profileId, ParticipantState state);

    List<SessionParticipant> findByProfileIdAndAttendanceStatusInOrderByAttendanceRecordedAtDesc(
            Long profileId,
            Collection<AttendanceStatus> attendanceStatuses);

    List<SessionParticipant> findByProfileIdAndAttendanceStatusOrderByAttendanceRecordedAtDesc(
            Long profileId,
            AttendanceStatus attendanceStatus);

    long countByProfileIdAndAttendanceStatus(
            Long profileId,
            AttendanceStatus attendanceStatus);

    @Query("""
            select distinct lower(trim(p.session.sport))
            from SessionParticipant p
            where p.profile.id = :profileId
              and p.attendanceStatus = :attendanceStatus
              and p.session.sport is not null
              and trim(p.session.sport) <> ''
            """)
    List<String> findDistinctSportsByProfileIdAndAttendanceStatus(
            @Param("profileId") Long profileId,
            @Param("attendanceStatus") AttendanceStatus attendanceStatus);

    @Query("""
            select p.session.gameDate
            from SessionParticipant p
            where p.profile.id = :profileId
              and p.attendanceStatus = :attendanceStatus
              and p.session.gameDate is not null
            """)
    List<java.util.Date> findGameDatesByProfileIdAndAttendanceStatus(
            @Param("profileId") Long profileId,
            @Param("attendanceStatus") AttendanceStatus attendanceStatus);

    List<SessionParticipant> findBySessionIdAndAttendanceStatusOrderByJoinedAtAsc(
            Long sessionId,
            AttendanceStatus attendanceStatus);


    @Query("""
            select distinct p.profile.id
            from SessionParticipant p
            where p.session.id in :sessionIds
              and p.attendanceStatus = :attendanceStatus
              and p.profile.id <> :excludedProfileId
            """)
    List<Long> findDistinctProfileIdsBySessionIdsAndAttendanceStatusExcludingProfile(
            @Param("sessionIds") Collection<Long> sessionIds,
            @Param("attendanceStatus") AttendanceStatus attendanceStatus,
            @Param("excludedProfileId") Long excludedProfileId);


    @Query("""
            select count(p.id)
            from SessionParticipant p
            where p.session.id in :sessionIds
              and p.attendanceStatus = :attendanceStatus
              and p.profile.id <> :excludedProfileId
            """)
    long countBySessionIdsAndAttendanceStatusExcludingProfile(
            @Param("sessionIds") Collection<Long> sessionIds,
            @Param("attendanceStatus") AttendanceStatus attendanceStatus,
            @Param("excludedProfileId") Long excludedProfileId);


    @Query("""
            select count(p.id)
            from SessionParticipant p
            where p.session.profile.id = :creatorProfileId
              and p.attendanceStatus = :attendanceStatus
              and p.profile.id <> :creatorProfileId
            """)
    long countAttendedParticipantsForCreatedSessions(
            @Param("creatorProfileId") Long creatorProfileId,
            @Param("attendanceStatus") AttendanceStatus attendanceStatus);


    @Query("""
            select count(distinct other.profile.id)
            from SessionParticipant other
            where other.attendanceStatus = :attendanceStatus
              and other.profile.id <> :profileId
              and exists (
                  select own.id
                  from SessionParticipant own
                  where own.session.id = other.session.id
                    and own.profile.id = :profileId
                    and own.attendanceStatus = :attendanceStatus
              )
            """)
    long countDistinctProfilesAttendedWith(
            @Param("profileId") Long profileId,
            @Param("attendanceStatus") AttendanceStatus attendanceStatus);

    @Query("""
            select p
            from SessionParticipant p
            join fetch p.session s
            join fetch p.profile pr
            where pr.id in :profileIds
              and p.attendanceStatus = com.example.desporto24.enumeration.AttendanceStatus.ATTENDED
              and p.attendanceRecordedAt is not null
              and (s.privateGame = false or s.privateGame is null)
            order by p.attendanceRecordedAt desc
            """)
    List<SessionParticipant> findPublicAttendanceFeed(
            @Param("profileIds") Collection<Long> profileIds,
            Pageable pageable);

    @Query("""
            select p from SessionParticipant p
            join fetch p.session s
            where p.profile.id = :profileId
              and p.attendanceRecordedAt is not null
              and p.attendanceRecordedAt between :from and :to
              and p.attendanceStatus in (
                  com.example.desporto24.enumeration.AttendanceStatus.ATTENDED,
                  com.example.desporto24.enumeration.AttendanceStatus.NO_SHOW,
                  com.example.desporto24.enumeration.AttendanceStatus.LATE_CANCELLED
              )
            order by p.attendanceRecordedAt asc
            """)
    List<SessionParticipant> findReliabilityHistory(
            @Param("profileId") Long profileId,
            @Param("from") java.time.Instant from,
            @Param("to") java.time.Instant to);
}
