package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityTrackSprintBracket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CommunityTrackSprintBracketRepository extends JpaRepository<CommunityTrackSprintBracket, Long> {
    List<CommunityTrackSprintBracket> findByCommunityIdAndSeasonIdOrderByIdAsc(Long communityId, Long seasonId);
    Optional<CommunityTrackSprintBracket> findByIdAndCommunityIdAndSeasonId(Long id, Long communityId, Long seasonId);
    void deleteBySeasonId(Long seasonId);
}
