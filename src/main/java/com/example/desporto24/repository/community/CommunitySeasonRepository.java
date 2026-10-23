package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunitySeason;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunitySeasonRepository extends JpaRepository<CommunitySeason, Long> {
    List<CommunitySeason> findByCommunityIdOrderByCreatedAtDesc(Long communityId);
    Optional<CommunitySeason> findByCommunityIdAndActiveTrue(Long communityId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select season from CommunitySeason season where season.id = :id and season.community.id = :communityId")
    Optional<CommunitySeason> lockByIdAndCommunityId(
            @Param("id") Long id, @Param("communityId") Long communityId);
}
