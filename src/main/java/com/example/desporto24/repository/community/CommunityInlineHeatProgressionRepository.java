package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityInlineHeatProgression;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommunityInlineHeatProgressionRepository extends JpaRepository<CommunityInlineHeatProgression, Long> {
    List<CommunityInlineHeatProgression> findByTargetRoundIdOrderBySourceRoundId(Long targetRoundId);
    void deleteByTargetRoundId(Long targetRoundId);
}
