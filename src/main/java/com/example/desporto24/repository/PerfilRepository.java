package com.example.desporto24.repository;
import com.example.desporto24.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
     Perfil findUserByEmail(String email);
     Perfil findUserByUsername(String username);
     Perfil findUserByPhone(String phone);
     @Transactional
     @Modifying
     @Query("UPDATE Perfil a " +
             "SET a.enabled = TRUE WHERE a.email = ?1")
     int enablePerfil(String email);

     @Transactional
     @Modifying
     @Query("UPDATE Perfil a " +
             "SET a.enabled = FALSE WHERE a.email = ?1")
     int disablePerfil(String email);
     //Perfil signUpPerfil3(Perfil perfil) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, MessagingException, NotAnImageFileException;
}