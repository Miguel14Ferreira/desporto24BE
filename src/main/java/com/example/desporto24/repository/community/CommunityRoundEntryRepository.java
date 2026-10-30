package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityRoundEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityRoundEntryRepository extends JpaRepository<CommunityRoundEntry, Long> {
    List<CommunityRoundEntry> findByRoundIdOrderByIdAsc(Long roundId);
    List<CommunityRoundEntry> findByRoundCommunityIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(Long communityId);
    List<CommunityRoundEntry> findByRoundSeasonIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(Long seasonId);
    // Only rounds attributed to a linked registered profile; historical records without
    // an account stay anonymous and cannot be attributed to an arbitrary player.
    @Query("""
        select entry from CommunityRoundEntry entry
        join fetch entry.communityPlayer player
        where entry.profile.id = :profileId
           or (entry.profile is null and player.profile.id = :profileId)
        """)
    List<CommunityRoundEntry> findEntriesForProfile(@Param("profileId") Long profileId);
    void deleteByRoundId(Long roundId);

    /** Player IDs linked to this exact season before its entries are deleted. */
    @Query("select distinct entry.communityPlayer.id from CommunityRoundEntry entry " +
           "where entry.round.community.id = :communityId and entry.round.season.id = :seasonId")
    List<Long> findHistoricalPlayerIdsBySeason(
            @Param("communityId") Long communityId, @Param("seasonId") Long seasonId);

    boolean existsByCommunityPlayerId(Long playerId);
}
