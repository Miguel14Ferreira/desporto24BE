package com.example.desporto24.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.desporto24.model.ChatReadCursor;
import java.util.Optional;

public interface ChatReadCursorRepository extends JpaRepository<ChatReadCursor, Long> {
    Optional<ChatReadCursor> findByProfileIdAndConversationKindAndConversationKey(
            Long profileId, String conversationKind, String conversationKey);

    @Modifying
    @Query(value = """
            INSERT INTO desporto24.chat_read_cursor
                (profile_id, conversation_kind, conversation_key, last_read_message_id)
            VALUES (:profileId, :kind, :key, :lastSeen)
            ON CONFLICT (profile_id, conversation_kind, conversation_key)
            DO UPDATE SET last_read_message_id =
                GREATEST(chat_read_cursor.last_read_message_id, EXCLUDED.last_read_message_id)
            """, nativeQuery = true)
    void advance(@Param("profileId") Long profileId, @Param("kind") String kind,
                 @Param("key") String key, @Param("lastSeen") long lastSeen);
}
