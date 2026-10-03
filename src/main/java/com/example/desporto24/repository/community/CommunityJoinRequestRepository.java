package com.example.desporto24.repository.community;

import com.example.desporto24.enumeration.CommunityInviteStatus;
import com.example.desporto24.model.community.CommunityJoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityJoinRequestRepository extends JpaRepository<CommunityJoinRequest, Long> {

    Optional<CommunityJoinRequest> findByCommunityIdAndRequesterIdAndStatus(
            Long communityId,
            Long requesterId,
            CommunityInviteStatus status
    );
}
