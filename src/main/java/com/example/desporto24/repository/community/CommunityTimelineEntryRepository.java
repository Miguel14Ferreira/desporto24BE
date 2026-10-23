package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityTimelineEntry;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CommunityTimelineEntryRepository extends JpaRepository<CommunityTimelineEntry, Long> {
    @EntityGraph(attributePaths = "author")
    List<CommunityTimelineEntry> findByCommunityIdAndKindAndIdLessThanOrderByIdDesc(
            Long communityId, CommunityTimelineEntry.Kind kind, Long beforeId, Pageable limit);

    @EntityGraph(attributePaths = "author")
    List<CommunityTimelineEntry> findTop60ByCommunityIdAndKindOrderByCreatedAtDescIdDesc(
            Long communityId, CommunityTimelineEntry.Kind kind);
}
