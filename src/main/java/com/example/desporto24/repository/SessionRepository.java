package com.example.desporto24.repository;

import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Session;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface SessionRepository extends JpaRepository<Session, Long> {

    Session findSessionByAddress(String address);
    Session findSessionByGameDate(Date gameDate);
    Session findSessionByUsername(String username);

    List<Session> findByStatusInAndGameDateAfterOrderByGameDateAsc(
            Collection<SessionStatus> statuses, Date gameDate);

    List<Session> findByProfileIdOrderByGameDateAsc(Long profileId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Session s where s.id = :id")
    java.util.Optional<Session> findByIdForUpdate(@Param("id") Long id);
}
