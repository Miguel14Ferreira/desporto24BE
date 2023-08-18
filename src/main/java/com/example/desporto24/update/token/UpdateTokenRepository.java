package com.example.desporto24.update.token;
import com.example.desporto24.registo.token.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UpdateTokenRepository extends JpaRepository<UpdateToken, Long> {

    Optional<UpdateToken> findByToken(String token);
    @Transactional
    @Modifying
    @Query("UPDATE UpdateToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token, LocalDateTime confirmedAt);
}
