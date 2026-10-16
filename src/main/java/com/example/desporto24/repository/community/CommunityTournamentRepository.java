package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityTournament;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityTournamentRepository extends JpaRepository<CommunityTournament, Long> {

    List<CommunityTournament> findByCommunityIdOrderByCreatedAtDescIdDesc(Long communityId);

    Slice<CommunityTournament> findByCommunityId(Long communityId, Pageable pageable);
}
