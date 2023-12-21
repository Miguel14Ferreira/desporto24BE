package com.example.desporto24.Chat;

import com.example.desporto24.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Chat findByUsername1AndUsername2(String sender, String recipient);

    List<Chat> findByChatId(String id);
}
