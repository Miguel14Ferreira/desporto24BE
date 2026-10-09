package com.example.desporto24.repository.community;

import com.example.desporto24.enumeration.CommunityInviteStatus;
import com.example.desporto24.model.community.CommunityJoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommunityJoinRequestRepository extends JpaRepository<CommunityJoinRequest, Long> {

    Optional<CommunityJoinRequest> findByCommunityIdAndRequesterIdAndStatus(
            Long communityId,
            Long requesterId,
            CommunityInviteStatus status
    );

    @Query("""
            select request
            from CommunityJoinRequest request
            join fetch request.community
            join fetch request.requester
            where request.community.id = :communityId
              and request.status = :status
            order by request.createdAt asc
            """)
    List<CommunityJoinRequest> findByCommunityIdAndStatusOrderByCreatedAtAsc(
            @Param("communityId") Long communityId,
            @Param("status") CommunityInviteStatus status
    );
}
