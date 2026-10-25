package com.example.desporto24.Chat;

import com.example.desporto24.model.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c WHERE (c.username1 = :sender AND c.username2 = :recipient) "
            + "OR (c.username1 = :recipient AND c.username2 = :sender) ORDER BY c.date ASC")
    List<Chat> findConversation(@Param("sender") String sender, @Param("recipient") String recipient);

    @Query("SELECT c FROM Chat c WHERE (c.username1 = :sender AND c.username2 = :recipient) "
            + "OR (c.username1 = :recipient AND c.username2 = :sender)")
    Page<Chat> findConversationPage(@Param("sender") String sender,
                                    @Param("recipient") String recipient,
                                    Pageable pageable);

    @Query("SELECT c FROM Chat c WHERE ((c.username1 = :sender AND c.username2 = :recipient) "
            + "OR (c.username1 = :recipient AND c.username2 = :sender)) "
            + "AND (c.date < :beforeDate OR (c.date = :beforeDate AND c.id < :beforeId)) "
            + "ORDER BY c.date DESC, c.id DESC")
    List<Chat> findConversationBefore(@Param("sender") String sender,
                                      @Param("recipient") String recipient,
                                      @Param("beforeDate") Date beforeDate,
                                      @Param("beforeId") Long beforeId,
                                      Pageable pageable);

    @Query("""
            select count(c) from Chat c
            where ((c.username1 = :sender and c.username2 = :recipient)
                or (c.username1 = :recipient and c.username2 = :sender))
              and c.id > :lastReadId
              and c.username1 = :recipient
            """)
    long countUnread(@Param("sender") String sender,
                     @Param("recipient") String recipient,
                     @Param("lastReadId") long lastReadId);

    @Query(value = """
            select c from Chat c
            where ((lower(c.username1) = lower(:actor) and lower(c.username2) = lower(:other))
                or (lower(c.username1) = lower(:other) and lower(c.username2) = lower(:actor)))
              and lower(c.text) like concat('%', :term, '%') escape '!'
            order by c.id desc
            """,
            countQuery = """
            select count(c) from Chat c
            where ((lower(c.username1) = lower(:actor) and lower(c.username2) = lower(:other))
                or (lower(c.username1) = lower(:other) and lower(c.username2) = lower(:actor)))
              and lower(c.text) like concat('%', :term, '%') escape '!'
            """)
    Page<Chat> searchConversation(@Param("actor") String actor,
                                  @Param("other") String other,
                                  @Param("term") String escapedTerm,
                                  Pageable pageable);


    @Query("""
        select c from Chat c
        where ((lower(c.username1) = lower(:actor) and lower(c.username2) = lower(:other))
            or (lower(c.username1) = lower(:other) and lower(c.username2) = lower(:actor)))
          and c.id <= :targetId order by c.id desc
        """)
    List<Chat> findConversationContextBefore(@Param("actor") String actor,
            @Param("other") String other, @Param("targetId") Long targetId, Pageable limit);

    @Query("""
        select c from Chat c
        where ((lower(c.username1) = lower(:actor) and lower(c.username2) = lower(:other))
            or (lower(c.username1) = lower(:other) and lower(c.username2) = lower(:actor)))
          and c.id > :targetId order by c.id asc
        """)
    List<Chat> findConversationContextAfter(@Param("actor") String actor,
            @Param("other") String other, @Param("targetId") Long targetId, Pageable limit);

    @org.springframework.data.jpa.repository.Modifying
    @Query("update Chat c set c.replyToText = :replacement where c.replyToMessageId = :messageId")
    int redactRepliesToDeleted(@Param("messageId") Long messageId,
                               @Param("replacement") String replacement);

    long deleteByUsername1OrUsername2(String username1, String username2);
}
