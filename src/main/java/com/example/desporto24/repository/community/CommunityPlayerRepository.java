package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityPlayerRepository extends JpaRepository<CommunityPlayer, Long> {

    List<CommunityPlayer> findByCommunityIdOrderByDisplayNameAsc(Long communityId);

    Optional<CommunityPlayer> findByCommunityIdAndNormalizedName(Long communityId, String normalizedName);

    Optional<CommunityPlayer> findByCommunityIdAndProfileId(Long communityId, Long profileId);
}
