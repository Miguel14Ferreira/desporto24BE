package com.example.desporto24.repository;

import com.example.desporto24.model.Friend;
import com.example.desporto24.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Integer> {

    boolean existsByPerfil1AndPerfil2(Perfil p1, Perfil p2);
    List<Friend> findByPerfil1(Perfil p);
    List<Friend> findByPerfil2(Perfil p);
}