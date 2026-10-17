package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityHistoricalPlayerLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface CommunityHistoricalPlayerLinkRepository extends JpaRepository<CommunityHistoricalPlayerLink, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CommunityHistoricalPlayerLink> findByCommunityIdAndPlayerId(Long communityId, Long playerId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CommunityHistoricalPlayerLink> findByIdAndCommunityId(Long id, Long communityId);
    List<CommunityHistoricalPlayerLink> findByCommunityIdAndStatus(Long communityId, String status);
    List<CommunityHistoricalPlayerLink> findByCommunityIdAndProfileIdAndStatus(Long communityId, Long profileId, String status);
}
