package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityDisciplinaryWarning;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommunityDisciplinaryWarningRepository
        extends JpaRepository<CommunityDisciplinaryWarning, Long> {
    List<CommunityDisciplinaryWarning> findTop100ByCommunityIdOrderByCreatedAtDescIdDesc(Long communityId);
    List<CommunityDisciplinaryWarning> findTop100ByCommunityIdAndTargetProfileIdOrderByCreatedAtDescIdDesc(
            Long communityId, Long targetProfileId);
}
