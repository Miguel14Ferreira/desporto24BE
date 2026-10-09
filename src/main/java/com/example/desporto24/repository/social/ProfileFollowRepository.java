package com.example.desporto24.repository.social;

import com.example.desporto24.model.social.ProfileFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProfileFollowRepository extends JpaRepository<ProfileFollow, Long> {

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    long countByFollowingId(Long followingId);

    long countByFollowerId(Long followerId);

    long deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Query("""
            select follow
            from ProfileFollow follow
            join fetch follow.following
            where follow.follower.id = :followerId
            order by follow.createdAt desc
            """)
    List<ProfileFollow> findByFollowerIdOrderByCreatedAtDesc(Long followerId);

    @Query("""
            select follow
            from ProfileFollow follow
            join fetch follow.follower
            where follow.following.id = :followingId
            order by follow.createdAt desc
            """)
    List<ProfileFollow> findByFollowingIdOrderByCreatedAtDesc(Long followingId);

    long deleteByFollowerIdOrFollowingId(Long followerId, Long followingId);
}
