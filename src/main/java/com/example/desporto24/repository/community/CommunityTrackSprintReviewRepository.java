package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityTrackSprintReview;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommunityTrackSprintReviewRepository extends JpaRepository<CommunityTrackSprintReview,Long> {
    List<CommunityTrackSprintReview> findByIncidentIdOrderByIdAsc(Long incidentId);
}
