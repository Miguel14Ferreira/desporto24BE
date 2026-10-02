package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityRound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface CommunityRoundRepository extends JpaRepository<CommunityRound, Long> {
    boolean existsByCommunityIdAndSeasonIdAndPlayedAt(Long communityId, Long seasonId, Instant playedAt);
    List<CommunityRound> findByCommunityIdOrderByPlayedAtDescIdDesc(Long communityId);
    List<CommunityRound> findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(
            Long communityId, Long seasonId);
}
