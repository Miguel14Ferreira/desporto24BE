package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityRoundEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

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
    void deleteByRoundId(Long roundId);
}
