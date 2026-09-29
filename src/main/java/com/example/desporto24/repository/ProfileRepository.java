package com.example.desporto24.repository;
import com.example.desporto24.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ProfileRepository extends JpaRepository<Profile, Long> {
     Optional<Profile> findUserByEmail(String email);
     Optional<Profile> findUserByUsername(String username);
     Optional<Profile> findUserByUsernameIgnoreCase(String username);
     Optional<Profile> findUserByPhone(String phone);
     Optional<Profile> findUserByOldEmail(String email);
     Optional<Profile> findUserByUserId(String userId);
     Optional<Profile> findByGoogleSubject(String googleSubject);

     @Query(value = "SELECT p " +
             "FROM Profile p " +
             "WHERE p.username = :username " +
             "LIMIT 10", nativeQuery = true)
     List<Profile> searchProfile(@Param("username") String username);

     @Query("""
             SELECT p
             FROM Profile p
             WHERE LOWER(p.username) <> LOWER(:actorUsername)
               AND (
                    :name IS NULL
                    OR LOWER(COALESCE(p.username, '')) LIKE LOWER(CONCAT('%', :name, '%'))
                    OR LOWER(COALESCE(p.fullName, '')) LIKE LOWER(CONCAT('%', :name, '%'))
                    OR LOWER(COALESCE(p.nickname, '')) LIKE LOWER(CONCAT('%', :name, '%'))
               )
               AND (
                    :sport IS NULL
                    OR LOWER(COALESCE(p.favoriteSports, '')) LIKE LOWER(CONCAT('%', :sport, '%'))
               )
               AND (
                    :location IS NULL
                    OR LOWER(COALESCE(p.location, '')) LIKE LOWER(CONCAT('%', :location, '%'))
               )
               AND (
                    :skillLevel IS NULL
                    OR LOWER(COALESCE(p.skillLevel, '')) = LOWER(:skillLevel)
               )
             ORDER BY p.id DESC
             """)
     List<Profile> searchPublicCandidates(
             @Param("actorUsername") String actorUsername,
             @Param("name") String name,
             @Param("sport") String sport,
             @Param("location") String location,
             @Param("skillLevel") String skillLevel,
             Pageable pageable);

     @Query("SELECT p FROM Profile p WHERE LOWER(p.username) <> LOWER(:username) ORDER BY p.id DESC")
     List<Profile> discoverProfiles(@Param("username") String username, Pageable pageable);
}