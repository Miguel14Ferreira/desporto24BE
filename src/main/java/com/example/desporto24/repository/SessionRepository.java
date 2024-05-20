package com.example.desporto24.repository;
import com.example.desporto24.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Repository
@Transactional(readOnly = true)
public interface SessionRepository extends JpaRepository<Session, Long> {
    Session findSessionByAddress(String morada);

    Session findSessionByGameDate(Date dataDeJogo);

    Session findSessionByUsername(String username);

    @Transactional
    @Modifying
    @Query("DELETE FROM Session a WHERE a.username = ?1")
    int deleteSession(String email);
}
