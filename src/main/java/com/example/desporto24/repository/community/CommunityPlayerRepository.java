package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface CommunityPlayerRepository extends JpaRepository<CommunityPlayer, Long> {

    @EntityGraph(attributePaths = {"profile"})
    List<CommunityPlayer> findByCommunityIdOrderByDisplayNameAsc(Long communityId);

    Optional<CommunityPlayer> findByCommunityIdAndNormalizedName(Long communityId, String normalizedName);

    @EntityGraph(attributePaths = {"profile"})
    Optional<CommunityPlayer> findByCommunityIdAndProfileId(Long communityId, Long profileId);
}
