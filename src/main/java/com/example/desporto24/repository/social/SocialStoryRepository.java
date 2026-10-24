package com.example.desporto24.repository.social;

import com.example.desporto24.model.social.SocialStory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

public interface SocialStoryRepository extends JpaRepository<SocialStory, Long> {
    @Query("""
            select story from SocialStory story
            join fetch story.author
            left join fetch story.community
            where story.expiresAt > :now
              and ((story.community is null and story.author.id in :authorIds)
                or story.community.id in :communityIds)
            order by story.createdAt desc, story.id desc
            """)
    List<SocialStory> findActiveFeed(
            @Param("now") Instant now,
            @Param("authorIds") Collection<Long> authorIds,
            @Param("communityIds") Collection<Long> communityIds,
            Pageable page);

    // Author-scoped by database, never an unrestricted scan of personal Stories.
    List<SocialStory> findTop20ByAuthorIdAndExpiresAtAfterOrderByCreatedAtDesc(Long authorId, Instant now);

    List<SocialStory> findByExpiresAtLessThanEqualOrderByIdAsc(Instant expiresAt, Pageable page);
}
