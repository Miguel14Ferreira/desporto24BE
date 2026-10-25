package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityInlineSpeedTieBreak;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CommunityInlineSpeedTieBreakRepository extends JpaRepository<CommunityInlineSpeedTieBreak, Long> {
    List<CommunityInlineSpeedTieBreak> findByTournamentIdOrderByIdAsc(Long tournamentId);
    Optional<CommunityInlineSpeedTieBreak> findByTournamentIdAndSourceStage(Long tournamentId, String sourceStage);
}
