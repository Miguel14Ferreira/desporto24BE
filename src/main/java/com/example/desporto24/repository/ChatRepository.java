package com.example.desporto24.repository;

import com.example.desporto24.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    void findByNomeDoChat(String nomeDoChat);
}
