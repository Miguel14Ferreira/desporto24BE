package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityMemberRepository extends JpaRepository<CommunityMember, Long> {
    Optional<CommunityMember> findByCommunityIdAndProfileId(Long communityId, Long profileId);
    List<CommunityMember> findByProfileIdOrderByJoinedAtDesc(Long profileId);
    long countByProfileId(Long profileId);
    List<CommunityMember> findByCommunityIdOrderByJoinedAtAsc(Long communityId);
    boolean existsByCommunityIdAndProfileId(Long communityId, Long profileId);
}
