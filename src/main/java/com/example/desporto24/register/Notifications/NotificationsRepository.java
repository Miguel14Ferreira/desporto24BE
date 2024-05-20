package com.example.desporto24.register.Notifications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, Long> {

    List<Notifications> findByPerfil(String perfil);

    @Transactional
    @Modifying
    @Query("DELETE FROM Notifications n WHERE n.perfil = ?1")
    void deleteByPerfil(String perfil);
}
