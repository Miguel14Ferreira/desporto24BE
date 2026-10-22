package com.example.desporto24.repository;

import com.example.desporto24.model.Friend;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Integer> {

    Friend findByProfile1AndProfile2(ProfileDTO p1, ProfileDTO p2);
    List<Profile> findByProfile1(Profile profile);
    List<Friend> findByProfile1OrProfile2(Profile profile1, Profile profile2);

    // Match the ID-based friendship check used by the request endpoint.
    @Query("""
            select friend from Friend friend
            where friend.profile1.id = :profileId or friend.profile2.id = :profileId
            """)
    List<Friend> findConnectionsForProfile(@Param("profileId") Long profileId);

    @Query("""
            select friend
            from Friend friend
            where (friend.profile1.id = :firstId and friend.profile2.id = :secondId)
               or (friend.profile1.id = :secondId and friend.profile2.id = :firstId)
            """)
    Optional<Friend> findConnection(
            @Param("firstId") Long firstId,
            @Param("secondId") Long secondId);

    long deleteByProfile1IdOrProfile2Id(Long profile1Id, Long profile2Id);
}
