package com.example.desporto24.repository.social;

import com.example.desporto24.model.social.SocialPostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialPostLikeRepository extends JpaRepository<SocialPostLike, Long> {

    boolean existsByPostIdAndProfileId(Long postId, Long profileId);

    long countByPostId(Long postId);

    long deleteByPostIdAndProfileId(Long postId, Long profileId);

    long deleteByPostId(Long postId);
}
