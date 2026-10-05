package com.example.desporto24.Chat;

import com.example.desporto24.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByUsername1AndUsername2(String sender, String recipient);

    @Query("""
            select c
            from Chat c
            where (c.username1 = :first and c.username2 = :second)
               or (c.username1 = :second and c.username2 = :first)
            order by c.date desc
            """)
    List<Chat> findConversation(
            @Param("first") String first,
            @Param("second") String second,
            Pageable pageable);

    long deleteByUsername1OrUsername2(String username1, String username2);
}
