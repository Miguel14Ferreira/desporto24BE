package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityStandingsArchive;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityStandingsArchiveRepository extends JpaRepository<CommunityStandingsArchive, Long> {
    List<CommunityStandingsArchive> findByCommunityIdOrderByCreatedAtDescIdDesc(Long communityId, Pageable pageable);
    Optional<CommunityStandingsArchive> findByIdAndCommunityId(Long id, Long communityId);
    boolean existsByCommunityIdAndSeasonId(Long communityId, Long seasonId);
}
