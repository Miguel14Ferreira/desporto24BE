package com.example.desporto24.repository;
import com.example.desporto24.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ProfileRepository extends JpaRepository<Profile, Long> {
     /** Exclude anonymized accounts; never count deleted profiles as registered people. */
     @Query("select count(p) from Profile p where p.email is null or lower(p.email) not like '%@deleted.invalid'")
     long countRegisteredProfiles();

     @Query("select count(p) from Profile p where p.enabled = true and (p.email is null or lower(p.email) not like '%@deleted.invalid')")
     long countEnabledRegisteredProfiles();

     @Query("select count(p) from Profile p where p.joinDate >= :since and (p.email is null or lower(p.email) not like '%@deleted.invalid')")
     long countRegistrationsSince(@Param("since") java.util.Date since);

     /** Recent logins are NOT an online-presence metric. */
     @Query("select count(p) from Profile p where p.lastLoginDate >= :since and (p.email is null or lower(p.email) not like '%@deleted.invalid')")
     long countRecentLoginsSince(@Param("since") java.util.Date since);

     Optional<Profile> findUserByEmail(String email);
     Optional<Profile> findUserByEmailIgnoreCase(String email);
     /** At most two: case-folded duplicate emails must fail login closed. */
     List<Profile> findTop2ByEmailIgnoreCase(String email);
     Optional<Profile> findUserByUsername(String username);
     Optional<Profile> findUserByUsernameIgnoreCase(String username);
     Optional<Profile> findUserByPhone(String phone);
     Optional<Profile> findUserByOldEmail(String email);
     Optional<Profile> findUserByUserId(String userId);
     @Lock(LockModeType.PESSIMISTIC_WRITE)
     @Query("SELECT p FROM Profile p WHERE p.id = :id")
     Optional<Profile> findForRoleChange(@Param("id") Long id);

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
               AND p.enabled = true
               AND (
                    :name = ''
                    OR LOWER(COALESCE(p.username, '')) LIKE CONCAT('%', :name, '%')
                    OR LOWER(COALESCE(p.fullName, '')) LIKE CONCAT('%', :name, '%')
                    OR LOWER(COALESCE(p.nickname, '')) LIKE CONCAT('%', :name, '%')
               )
               AND (
                    :sport = ''
                    OR LOWER(COALESCE(p.favoriteSports, '')) LIKE CONCAT('%', :sport, '%')
               )
               AND (
                    :location = ''
                    OR LOWER(COALESCE(p.location, '')) LIKE CONCAT('%', :location, '%')
               )
               AND (
                    :skillLevel = ''
                    OR LOWER(COALESCE(p.skillLevel, '')) = :skillLevel
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

     @Query("""
             SELECT p FROM Profile p
             WHERE (p.email IS NULL OR LOWER(p.email) NOT LIKE '%@deleted.invalid')
               AND (
                    LOWER(COALESCE(p.username, '')) LIKE CONCAT('%', :name, '%') ESCAPE '!'
                    OR LOWER(COALESCE(p.fullName, '')) LIKE CONCAT('%', :name, '%') ESCAPE '!'
                    OR LOWER(COALESCE(p.nickname, '')) LIKE CONCAT('%', :name, '%') ESCAPE '!'
               )
             ORDER BY LOWER(p.username), p.id
             """)
     Slice<Profile> searchAdminPlayers(@Param("name") String name, Pageable pageable);

     @Query("SELECT p FROM Profile p WHERE LOWER(p.username) <> LOWER(:username) AND p.enabled = true ORDER BY p.id DESC")
     List<Profile> discoverProfiles(@Param("username") String username, Pageable pageable);
}