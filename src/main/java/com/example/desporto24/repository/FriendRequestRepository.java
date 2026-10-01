package com.example.desporto24.repository;

import com.example.desporto24.register.FriendRequest.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findByToken(String token);

    @Query("""
            select case when count(fr) > 0 then true else false end
            from FriendRequest fr
            where fr.confirmedAt is null
              and (
                    (fr.perfil1 = :firstUsername and fr.perfil2 = :secondUsername)
                 or (fr.perfil1 = :secondUsername and fr.perfil2 = :firstUsername)
              )
            """)
    boolean existsPendingBetween(String firstUsername, String secondUsername);
    @Transactional
    @Modifying
    @Query("UPDATE FriendRequest c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token, LocalDateTime confirmedAt);

    long deleteByPerfil1OrPerfil2(String perfil1, String perfil2);
}
