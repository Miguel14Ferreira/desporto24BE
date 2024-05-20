package com.example.desporto24.repository;
import com.example.desporto24.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface ProfileRepository extends JpaRepository<Profile, Long> {
     Profile findUserByEmail(String email);
     Profile findUserByUsername(String username);
     Profile findUserByPhone(String phone);
     Profile findUserByOldEmail(String email);

     Profile findUserByUserId(String userId);

     @Query("SELECT p FROM Profile p WHERE p.username LIKE %?1%")
     List<Profile> pesquisaProfile(String keyword);
     //Perfil signUpPerfil3(Perfil perfil) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, MessagingException, NotAnImageFileException;
}