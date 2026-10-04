package com.example.desporto24.repository;

import com.example.desporto24.model.Friend;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Integer> {

    Friend findByProfile1AndProfile2(ProfileDTO p1, ProfileDTO p2);
    List<Profile> findByProfile1(Profile profile);
    List<Friend> findByProfile1OrProfile2(Profile profile1, Profile profile2);

    long deleteByProfile1IdOrProfile2Id(Long profile1Id, Long profile2Id);
}