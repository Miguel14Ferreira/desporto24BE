package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityInlineSpeedDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CommunityInlineSpeedDecisionRepository extends JpaRepository<CommunityInlineSpeedDecision, Long> {
    List<CommunityInlineSpeedDecision> findByTournamentIdOrderByIdAsc(Long tournamentId);
    Optional<CommunityInlineSpeedDecision> findByTournamentIdAndSourceStage(Long tournamentId, String sourceStage);
}
