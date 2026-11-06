package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityWarningAppealDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommunityWarningAppealDecisionRepository extends JpaRepository<CommunityWarningAppealDecision, Long> {
    boolean existsByAppealId(Long appealId);
    List<CommunityWarningAppealDecision> findTop100ByCommunityIdOrderByDecidedAtDescIdDesc(Long communityId);
    List<CommunityWarningAppealDecision> findTop100ByCommunityIdAndAppealIdInOrderByDecidedAtDescIdDesc(
            Long communityId, List<Long> appealIds);
}
