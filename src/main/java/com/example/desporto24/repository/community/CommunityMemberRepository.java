package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommunityMemberRepository extends JpaRepository<CommunityMember, Long> {
    Optional<CommunityMember> findByCommunityIdAndProfileId(Long communityId, Long profileId);

    @Query("""
            select member
            from CommunityMember member
            join fetch member.community community
            join fetch community.owner
            where member.profile.id = :profileId
            order by member.joinedAt desc
            """)
    List<CommunityMember> findByProfileIdOrderByJoinedAtDesc(
            @Param("profileId") Long profileId);

    long countByProfileId(Long profileId);

    @Query("""
            select member
            from CommunityMember member
            join fetch member.profile
            where member.community.id = :communityId
            order by member.joinedAt asc
            """)
    List<CommunityMember> findByCommunityIdOrderByJoinedAtAsc(
            @Param("communityId") Long communityId);

    boolean existsByCommunityIdAndProfileId(Long communityId, Long profileId);

    long deleteByProfileId(Long profileId);
}
