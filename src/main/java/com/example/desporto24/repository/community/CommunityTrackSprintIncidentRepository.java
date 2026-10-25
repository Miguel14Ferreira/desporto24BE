package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityTrackSprintIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommunityTrackSprintIncidentRepository
        extends JpaRepository<CommunityTrackSprintIncident, Long> {
    List<CommunityTrackSprintIncident> findByBracketIdOrderByIdAsc(Long bracketId);
    boolean existsByBracketId(Long bracketId);
    boolean existsByBracketIdAndSeriesKeyAndStatus(Long bracketId,String seriesKey,String status);
    void deleteByBracketId(Long bracketId);
}
