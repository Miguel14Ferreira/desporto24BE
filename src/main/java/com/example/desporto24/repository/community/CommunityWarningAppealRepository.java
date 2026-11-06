package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityWarningAppeal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityWarningAppealRepository extends JpaRepository<CommunityWarningAppeal, Long> {
    boolean existsByWarningId(Long warningId);
    List<CommunityWarningAppeal> findTop100ByCommunityIdOrderByCreatedAtDescIdDesc(Long communityId);
    List<CommunityWarningAppeal> findTop100ByCommunityIdAndAuthorProfileIdOrderByCreatedAtDescIdDesc(
            Long communityId, Long authorProfileId);
}
