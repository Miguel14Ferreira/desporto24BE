package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

public interface CommunityPlayerRepository extends JpaRepository<CommunityPlayer, Long> {

    // Serialize first-time claims: locking the link table is not enough when no link row exists.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select player from CommunityPlayer player where player.id = :playerId")
    Optional<CommunityPlayer> findByIdForUpdate(@Param("playerId") Long playerId);

    List<CommunityPlayer> findByCommunityIdOrderByDisplayNameAsc(Long communityId);

    Optional<CommunityPlayer> findByCommunityIdAndNormalizedName(Long communityId, String normalizedName);

    Optional<CommunityPlayer> findByCommunityIdAndProfileId(Long communityId, Long profileId);

    /**
     * Only anonymous historical identities from the deleted season, no longer
     * used by any remaining season or community round. Real member profiles
     * and historical players used by other seasons must survive.
     */
    @Query("select player from CommunityPlayer player where player.community.id = :communityId " +
           "and player.id in :playerIds and player.profile is null " +
           "and not exists (select entry.id from CommunityRoundEntry entry " +
           "where entry.communityPlayer = player)")
    List<CommunityPlayer> findUnusedHistoricalPlayers(
            @Param("communityId") Long communityId, @Param("playerIds") List<Long> playerIds);

}
