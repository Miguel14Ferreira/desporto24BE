package com.example.desporto24.repository.social;

import com.example.desporto24.model.social.SocialPostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface SocialPostCommentRepository extends JpaRepository<SocialPostComment, Long> {

    List<SocialPostComment> findByPostIdOrderByCreatedAtAsc(Long postId);

    long countByPostId(Long postId);

    long deleteByPostId(Long postId);

    @Query("select c.post.id, count(c.id) from SocialPostComment c where c.post.id in :postIds group by c.post.id")
    List<Object[]> countByPostIds(@Param("postIds") Set<Long> postIds);

    long deleteByAuthorId(Long authorId);
}
