package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CommunityMemberRepository extends JpaRepository<CommunityMember, Long> {
    Optional<CommunityMember> findByCommunityIdAndProfileId(Long communityId, Long profileId);
    @EntityGraph(attributePaths = {"profile", "community"})
    List<CommunityMember> findByProfileIdOrderByJoinedAtDesc(Long profileId);

    @EntityGraph(attributePaths = {"profile", "community"})
    List<CommunityMember> findByProfileIdOrderByJoinedAtDesc(
            Long profileId,
            Pageable pageable);
    long countByProfileId(Long profileId);
    @EntityGraph(attributePaths = {"profile", "community"})
    List<CommunityMember> findByCommunityIdOrderByJoinedAtAsc(Long communityId);
    @EntityGraph(attributePaths = {"profile", "community"})
    List<CommunityMember> findByCommunityIdIn(Collection<Long> communityIds);
    boolean existsByCommunityIdAndProfileId(Long communityId, Long profileId);

    long deleteByProfileId(Long profileId);
}
