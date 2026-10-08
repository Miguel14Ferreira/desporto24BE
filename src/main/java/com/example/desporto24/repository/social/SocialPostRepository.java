package com.example.desporto24.repository.social;

import com.example.desporto24.model.social.SocialPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface SocialPostRepository extends JpaRepository<SocialPost, Long> {

    @Query("""
            select p
            from SocialPost p
            join fetch p.author
            where p.author.id in :authorIds
            order by p.createdAt desc
            """)
    List<SocialPost> findFeedPosts(
            @Param("authorIds") Collection<Long> authorIds,
            Pageable pageable);

    @Query("""
            select p
            from SocialPost p
            join fetch p.author
            order by p.createdAt desc
            """)
    List<SocialPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<SocialPost> findByAuthorId(Long authorId);
}
