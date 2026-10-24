package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityInlineHeatProgression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CommunityInlineHeatProgressionRepository extends JpaRepository<CommunityInlineHeatProgression, Long> {
    List<CommunityInlineHeatProgression> findByTargetRoundIdOrderBySourceRoundId(Long targetRoundId);
    void deleteByTargetRoundId(Long targetRoundId);
    @Query("select link from CommunityInlineHeatProgression link " +
            "where link.targetRound.community.id = :communityId " +
            "order by link.targetRound.id, link.sourceRound.id")
    List<CommunityInlineHeatProgression> findByCommunityId(
            @Param("communityId") Long communityId);
}
