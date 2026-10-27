package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityRound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface CommunityRoundRepository extends JpaRepository<CommunityRound, Long> {
    boolean existsByCommunityIdAndSeasonId(Long communityId, Long seasonId);
    boolean existsByCommunityIdAndSourceSessionId(Long communityId, Long sourceSessionId);
    @org.springframework.data.jpa.repository.Query(
        "select r.sourceSessionId from CommunityRound r where r.community.id = :communityId and r.sourceSessionId is not null")
    List<Long> findRecordedSessionIds(@org.springframework.data.repository.query.Param("communityId") Long communityId);
    List<CommunityRound> findByCommunityIdAndSeasonIdAndSprintSeriesKeyOrderBySprintHeatNumberAsc(
            Long communityId, Long seasonId, String sprintSeriesKey);
    boolean existsByCommunityIdAndSeasonIdAndPlayedAt(Long communityId, Long seasonId, Instant playedAt);
    List<CommunityRound> findByCommunityIdAndSeasonIdAndPlayedAt(Long communityId, Long seasonId, Instant playedAt);
    List<CommunityRound> findByCommunityIdOrderByPlayedAtDescIdDesc(Long communityId);
    List<CommunityRound> findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(
            Long communityId, Long seasonId);
}
