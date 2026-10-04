package com.example.desporto24.repository;

import com.example.desporto24.register.FriendRequest.FriendRequest;
import com.example.desporto24.register.Notifications.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, Long> {

    List<Notifications> findByUsername(String username);
    Optional<Notifications> findByToken(String token);

    @Transactional
    @Modifying
    @Query("DELETE FROM Notifications n WHERE n.username = ?1")
    void deleteByUsername(String username);

    long deleteByUsernameOrRelatedUsername(String username, String relatedUsername);
}
