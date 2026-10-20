package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityStandingsArchiveRow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityStandingsArchiveRowRepository extends JpaRepository<CommunityStandingsArchiveRow, Long> {
    List<CommunityStandingsArchiveRow> findByArchiveIdOrderByPositionAscIdAsc(Long archiveId);
}
