package com.example.desporto24.repository;
import com.example.desporto24.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
     Perfil findUserByEmail(String email);
     Perfil findUserByUsername(String username);
     Perfil findUserByPhone(String phone);
     Perfil findUserByOldEmail(String email);

     Perfil findUserByUserId(String userId);

     @Query("SELECT p FROM Perfil p WHERE p.username LIKE %?1%")
     List<Perfil> pesquisaPerfil(String keyword);
     //Perfil signUpPerfil3(Perfil perfil) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, MessagingException, NotAnImageFileException;
}