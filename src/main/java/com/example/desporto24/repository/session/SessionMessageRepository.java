package com.example.desporto24.repository.session;

import com.example.desporto24.model.session.SessionMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionMessageRepository extends JpaRepository<SessionMessage, Long> {
}
