package com.example.desporto24.repository;

import com.example.desporto24.model.Friend;
import com.example.desporto24.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Integer> {

    boolean existsByProfile1AndProfile2(Profile p1, Profile p2);
    List<Friend> findByProfile1(Profile p);
    List<Friend> findByProfile2(Profile p);
}