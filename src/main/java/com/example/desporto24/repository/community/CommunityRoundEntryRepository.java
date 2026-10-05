package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityRoundEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface CommunityRoundEntryRepository extends JpaRepository<CommunityRoundEntry, Long> {
    @EntityGraph(attributePaths = {"round", "round.season", "profile", "communityPlayer", "communityPlayer.profile"})
    List<CommunityRoundEntry> findByRoundIdOrderByIdAsc(Long roundId);
    @EntityGraph(attributePaths = {"round", "round.season", "profile", "communityPlayer", "communityPlayer.profile"})
    List<CommunityRoundEntry> findByRoundIdInOrderByRoundIdAscIdAsc(Collection<Long> roundIds);
    @EntityGraph(attributePaths = {"round", "round.season", "profile", "communityPlayer", "communityPlayer.profile"})
    List<CommunityRoundEntry> findByRoundCommunityIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(Long communityId);
    @EntityGraph(attributePaths = {"round", "round.season", "profile", "communityPlayer", "communityPlayer.profile"})
    List<CommunityRoundEntry> findByRoundSeasonIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(Long seasonId);
    @EntityGraph(attributePaths = {"round", "round.season", "profile", "communityPlayer", "communityPlayer.profile"})
    List<CommunityRoundEntry> findByRoundSeasonIdInOrderByRoundPlayedAtAscRoundIdAscIdAsc(Collection<Long> seasonIds);
    @EntityGraph(attributePaths = {"round", "profile", "communityPlayer", "communityPlayer.profile"})
    @Query("""
            select entry
            from CommunityRoundEntry entry
            where entry.round.community.id = :communityId
              and (
                    entry.profile.id in :profileIds
                    or entry.communityPlayer.profile.id in :profileIds
              )
            order by entry.round.playedAt asc, entry.round.id asc, entry.id asc
            """)
    List<CommunityRoundEntry> findByCommunityIdAndProfileIds(
            @Param("communityId") Long communityId,
            @Param("profileIds") Collection<Long> profileIds);

    void deleteByRoundId(Long roundId);
}
