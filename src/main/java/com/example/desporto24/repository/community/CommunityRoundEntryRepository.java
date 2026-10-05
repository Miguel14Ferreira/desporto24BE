package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityRoundEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CommunityRoundEntryRepository extends JpaRepository<CommunityRoundEntry, Long> {
    List<CommunityRoundEntry> findByRoundIdOrderByIdAsc(Long roundId);
    List<CommunityRoundEntry> findByRoundIdInOrderByRoundIdAscIdAsc(Collection<Long> roundIds);
    List<CommunityRoundEntry> findByRoundCommunityIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(Long communityId);
    List<CommunityRoundEntry> findByRoundSeasonIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(Long seasonId);
    List<CommunityRoundEntry> findByRoundSeasonIdInOrderByRoundPlayedAtAscRoundIdAscIdAsc(Collection<Long> seasonIds);
    void deleteByRoundId(Long roundId);
}
