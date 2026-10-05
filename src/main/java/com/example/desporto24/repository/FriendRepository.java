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

    @Query("""
            select friend
            from Friend friend
            where (friend.profile1.id = :firstProfileId and friend.profile2.id = :secondProfileId)
               or (friend.profile1.id = :secondProfileId and friend.profile2.id = :firstProfileId)
            """)
    Optional<Friend> findBetweenProfileIds(
            @Param("firstProfileId") Long firstProfileId,
            @Param("secondProfileId") Long secondProfileId);

    long deleteByProfile1IdOrProfile2Id(Long profile1Id, Long profile2Id);
}