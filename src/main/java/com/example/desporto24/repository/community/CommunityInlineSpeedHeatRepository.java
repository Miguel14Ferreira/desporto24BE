package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityInlineSpeedHeatLink;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CommunityInlineSpeedHeatRepository extends JpaRepository<CommunityInlineSpeedHeatLink, Long> {
    List<CommunityInlineSpeedHeatLink> findByTournamentIdOrderByIdAsc(Long tournamentId);
    Optional<CommunityInlineSpeedHeatLink> findByTournamentIdAndRoundId(Long tournamentId, Long roundId);
    boolean existsByRoundId(Long roundId);
}
