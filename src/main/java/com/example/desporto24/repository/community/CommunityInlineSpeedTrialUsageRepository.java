package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityInlineSpeedTrialUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommunityInlineSpeedTrialUsageRepository extends JpaRepository<CommunityInlineSpeedTrialUsage, Long> {
    List<CommunityInlineSpeedTrialUsage> findByRevisionIdOrderByIdAsc(Long revisionId);
}
