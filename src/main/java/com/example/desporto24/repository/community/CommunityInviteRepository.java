package com.example.desporto24.repository.community;

import com.example.desporto24.enumeration.CommunityInviteStatus;
import com.example.desporto24.model.community.CommunityInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface CommunityInviteRepository extends JpaRepository<CommunityInvite, Long> {
    Optional<CommunityInvite> findByCommunityIdAndInvitedProfileIdAndStatus(
            Long communityId, Long invitedProfileId, CommunityInviteStatus status);
    @EntityGraph(attributePaths = {"community", "invitedProfile", "invitedBy"})
    List<CommunityInvite> findByInvitedProfileIdAndStatusOrderByCreatedAtDesc(
            Long invitedProfileId,
            CommunityInviteStatus status,
            Pageable pageable);

    long deleteByInvitedProfileIdOrInvitedById(Long invitedProfileId, Long invitedById);
}
