package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityHistoricalMatchArchive;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CommunityHistoricalMatchArchiveRepository
        extends JpaRepository<CommunityHistoricalMatchArchive, Long> {
    List<CommunityHistoricalMatchArchive> findByCommunityIdOrderByCreatedAtDescIdDesc(
            Long communityId, Pageable pageable);
    Optional<CommunityHistoricalMatchArchive> findByIdAndCommunityId(
            Long archiveId, Long communityId);
}
