package com.example.desporto24.repository;

import com.example.desporto24.model.ChatMessageReaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.List;

@Repository
public interface ChatMessageReactionRepository extends JpaRepository<ChatMessageReaction, Long> {
    List<ChatMessageReaction> findByConversationKindAndMessageIdIn(String kind, Collection<Long> messageIds);

    @Modifying
    @Query(value = """
            INSERT INTO desporto24.chat_message_reaction
                (profile_id, conversation_kind, message_id, emoji)
            VALUES (:actorId, :kind, :messageId, :emoji)
            ON CONFLICT (profile_id, conversation_kind, message_id)
            DO UPDATE SET emoji = EXCLUDED.emoji
            """, nativeQuery = true)
    int upsert(@Param("actorId") Long actorId,
               @Param("kind") String kind,
               @Param("messageId") Long messageId,
               @Param("emoji") String emoji);

    long deleteByProfileIdAndConversationKindAndMessageId(Long actorId, String kind, Long messageId);
}
