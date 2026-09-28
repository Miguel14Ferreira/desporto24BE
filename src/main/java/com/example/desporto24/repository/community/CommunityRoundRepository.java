package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityRound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityRoundRepository extends JpaRepository<CommunityRound, Long> {
    List<CommunityRound> findByCommunityIdOrderByPlayedAtDescIdDesc(Long communityId);
}
