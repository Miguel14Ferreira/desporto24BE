package com.example.desporto24.repository;

import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Session;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface SessionRepository extends JpaRepository<Session, Long> {

    Session findSessionByAddress(String address);
    Session findSessionByGameDate(Date gameDate);
    Session findSessionByUsername(String username);

    List<Session> findByStatusInAndGameDateAfterOrderByGameDateAsc(
            Collection<SessionStatus> statuses, Date gameDate);


    @Query("""
            select s
            from Session s
            where s.status in :statuses
              and s.gameDate > :now
              and (s.audience is null or s.audience <> com.example.desporto24.enumeration.SessionAudience.COMMUNITY_ONLY)
              and (:sport = '' or lower(s.sport) = :sport)
              and (:city = '' or lower(s.city) like concat('%', :city, '%'))
              and (:fromDate is null or s.gameDate >= :fromDate)
              and (:toDate is null or s.gameDate <= :toDate)
              and (:privateGame is null or coalesce(s.privateGame, false) = :privateGame)
              and (:freeOnly = false or coalesce(s.priceAmount, 0) = 0)
              and (:minPrice is null or coalesce(s.priceAmount, 0) >= :minPrice)
              and (:maxPrice is null or coalesce(s.priceAmount, 0) <= :maxPrice)
            order by s.gameDate asc
            """)
    List<Session> searchDiscoverable(
            @Param("statuses") Collection<SessionStatus> statuses,
            @Param("now") Date now,
            @Param("sport") String sport,
            @Param("city") String city,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("privateGame") Boolean privateGame,
            @Param("freeOnly") boolean freeOnly,
            @Param("minPrice") java.math.BigDecimal minPrice,
            @Param("maxPrice") java.math.BigDecimal maxPrice,
            Pageable pageable);

    List<Session> findByProfileIdOrderByGameDateAsc(Long profileId);
    long countByProfileId(Long profileId);

    List<Session> findByCommunityIdOrderByGameDateDesc(Long communityId);
    List<Session> findByRecurrenceSeriesIdOrderByGameDateAsc(String recurrenceSeriesId);

    @Query("""
            select s
            from Session s
            where s.profile.id in :profileIds
              and (s.privateGame = false or s.privateGame is null)
            order by s.created_at desc
            """)
    List<Session> findPublicFeedSessions(
            @Param("profileIds") Collection<Long> profileIds,
            Pageable pageable);

    @Query("""
            select s
            from Session s
            where (s.privateGame = false or s.privateGame is null)
            order by s.created_at desc
            """)
    List<Session> findRecentPublicFeedSessions(Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Session s where s.id = :id")
    java.util.Optional<Session> findByIdForUpdate(@Param("id") Long id);
}
