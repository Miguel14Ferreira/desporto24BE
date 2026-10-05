package com.example.desporto24.repository.social;

import com.example.desporto24.model.social.ProfileFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProfileFollowRepository extends JpaRepository<ProfileFollow, Long> {

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    long countByFollowingId(Long followingId);

    long countByFollowerId(Long followerId);

    long deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    List<ProfileFollow> findByFollowerIdOrderByCreatedAtDesc(Long followerId);
    List<ProfileFollow> findByFollowerIdOrderByCreatedAtDesc(Long followerId, Pageable pageable);

    List<ProfileFollow> findByFollowingIdOrderByCreatedAtDesc(Long followingId);
    List<ProfileFollow> findByFollowingIdOrderByCreatedAtDesc(Long followingId, Pageable pageable);

    long deleteByFollowerIdOrFollowingId(Long followerId, Long followingId);
}
