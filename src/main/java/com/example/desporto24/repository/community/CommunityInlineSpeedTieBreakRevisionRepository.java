package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityInlineSpeedTieBreakRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommunityInlineSpeedTieBreakRevisionRepository
        extends JpaRepository<CommunityInlineSpeedTieBreakRevision, Long> {
    List<CommunityInlineSpeedTieBreakRevision> findByTieBreakIdOrderByRevisionNumberAsc(Long tieBreakId);
}
