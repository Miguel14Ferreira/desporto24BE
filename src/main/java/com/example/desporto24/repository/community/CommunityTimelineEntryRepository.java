package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityTimelineEntry;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommunityTimelineEntryRepository extends JpaRepository<CommunityTimelineEntry, Long> {
    @EntityGraph(attributePaths = "author")
    List<CommunityTimelineEntry> findByCommunityIdAndKindAndIdLessThanOrderByIdDesc(
            Long communityId, CommunityTimelineEntry.Kind kind, Long beforeId, Pageable limit);

    @EntityGraph(attributePaths = "author")
    List<CommunityTimelineEntry> findByCommunityIdAndKindOrderByIdDesc(
            Long communityId, CommunityTimelineEntry.Kind kind, Pageable limit);

    @Query("""
            select count(e) from CommunityTimelineEntry e
            where e.community.id = :communityId and e.kind = :kind
              and e.id > :lastReadId and e.author.id <> :readerId
            """)
    long countUnread(@Param("communityId") Long communityId,
                     @Param("kind") CommunityTimelineEntry.Kind kind,
                     @Param("readerId") Long readerId,
                     @Param("lastReadId") long lastReadId);

    @Query(value = """
            select e from CommunityTimelineEntry e
            join fetch e.author
            where e.community.id = :communityId and e.kind = :kind
              and lower(e.body) like concat('%', :term, '%') escape '!'
            order by e.id desc
            """,
            countQuery = """
            select count(e) from CommunityTimelineEntry e
            where e.community.id = :communityId and e.kind = :kind
              and lower(e.body) like concat('%', :term, '%') escape '!'
            """)
    org.springframework.data.domain.Page<CommunityTimelineEntry> searchConversation(
            @Param("communityId") Long communityId,
            @Param("kind") CommunityTimelineEntry.Kind kind,
            @Param("term") String escapedTerm, Pageable pageable);


    @EntityGraph(attributePaths = "author")
    List<CommunityTimelineEntry> findByCommunityIdAndKindAndIdLessThanEqualOrderByIdDesc(
            Long communityId, CommunityTimelineEntry.Kind kind, Long targetId, Pageable limit);

    @EntityGraph(attributePaths = "author")
    List<CommunityTimelineEntry> findByCommunityIdAndKindAndIdGreaterThanOrderByIdAsc(
            Long communityId, CommunityTimelineEntry.Kind kind, Long targetId, Pageable limit);

    @EntityGraph(attributePaths = "author")
    List<CommunityTimelineEntry> findTop60ByCommunityIdAndKindOrderByCreatedAtDescIdDesc(
            Long communityId, CommunityTimelineEntry.Kind kind);
    @org.springframework.data.jpa.repository.Modifying
    @Query("update CommunityTimelineEntry e set e.replyToText = :replacement " +
           "where e.community.id = :communityId and e.replyToMessageId = :messageId")
    int redactRepliesToDeleted(@Param("communityId") Long communityId,
                               @Param("messageId") Long messageId,
                               @Param("replacement") String replacement);
}
