package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityTournamentRaceRound;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CommunityTournamentRaceRoundRepository
        extends JpaRepository<CommunityTournamentRaceRound, Long> {
    List<CommunityTournamentRaceRound> findByTournamentIdOrderByIdAsc(Long tournamentId);
    Optional<CommunityTournamentRaceRound> findByTournamentIdAndRoundId(Long tournamentId, Long roundId);
    boolean existsByTournamentIdAndRoundId(Long tournamentId, Long roundId);
}
