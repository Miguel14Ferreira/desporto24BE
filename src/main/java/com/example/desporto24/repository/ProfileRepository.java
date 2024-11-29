package com.example.desporto24.repository;
import com.example.desporto24.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ProfileRepository extends JpaRepository<Profile, Long> {
     Optional<Profile> findUserByEmail(String email);
     Optional<Profile> findUserByUsername(String username);
     Optional<Profile> findUserByPhone(String phone);
     Optional<Profile> findUserByOldEmail(String email);
     Optional<Profile> findUserByUserId(String userId);

     @Query(value = "SELECT p " +
             "FROM Profile p " +
             "WHERE p.username = :username " +
             "LIMIT 10", nativeQuery = true)
     List<Profile> searchProfile(@Param("username") String username);
}