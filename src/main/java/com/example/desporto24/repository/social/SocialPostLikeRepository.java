package com.example.desporto24.repository.social;

import com.example.desporto24.model.social.SocialPostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface SocialPostLikeRepository extends JpaRepository<SocialPostLike, Long> {

    boolean existsByPostIdAndProfileId(Long postId, Long profileId);

    long countByPostId(Long postId);


    @Query("""
            select l.post.id, count(l.id)
            from SocialPostLike l
            where l.post.id in :postIds
            group by l.post.id
            """)
    List<Object[]> countByPostIds(@Param("postIds") Collection<Long> postIds);

    long deleteByPostIdAndProfileId(Long postId, Long profileId);

    long deleteByPostId(Long postId);

    long deleteByProfileId(Long profileId);
}
