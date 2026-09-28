package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunitySeason;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunitySeasonRepository extends JpaRepository<CommunitySeason, Long> {
    List<CommunitySeason> findByCommunityIdOrderByCreatedAtDesc(Long communityId);
    Optional<CommunitySeason> findByCommunityIdAndActiveTrue(Long communityId);
}
