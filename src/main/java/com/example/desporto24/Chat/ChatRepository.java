package com.example.desporto24.Chat;

import com.example.desporto24.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c WHERE (c.username1 = :sender AND c.username2 = :recipient) "
            + "OR (c.username1 = :recipient AND c.username2 = :sender) ORDER BY c.date ASC")
    List<Chat> findConversation(@Param("sender") String sender, @Param("recipient") String recipient);

    long deleteByUsername1OrUsername2(String username1, String username2);
}
