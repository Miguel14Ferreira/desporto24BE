package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityTournamentMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityTournamentMatchRepository extends JpaRepository<CommunityTournamentMatch, Long> {

    List<CommunityTournamentMatch> findByTournamentIdOrderByRoundNumberAscIdAsc(Long tournamentId);
}
