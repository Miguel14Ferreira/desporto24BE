package com.example.desporto24.repository.community;

import com.example.desporto24.enumeration.CommunityInviteStatus;
import com.example.desporto24.model.community.CommunityInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommunityInviteRepository extends JpaRepository<CommunityInvite, Long> {
    Optional<CommunityInvite> findByCommunityIdAndInvitedProfileIdAndStatus(
            Long communityId, Long invitedProfileId, CommunityInviteStatus status);

    @Query("""
            select invite
            from CommunityInvite invite
            join fetch invite.community
            join fetch invite.invitedBy
            where invite.invitedProfile.id = :invitedProfileId
              and invite.status = :status
            order by invite.createdAt desc
            """)
    List<CommunityInvite> findByInvitedProfileIdAndStatusOrderByCreatedAtDesc(
            @Param("invitedProfileId") Long invitedProfileId,
            @Param("status") CommunityInviteStatus status);

    long countByCommunityId(Long communityId);

    long deleteByInvitedProfileIdOrInvitedById(Long invitedProfileId, Long invitedById);
}
