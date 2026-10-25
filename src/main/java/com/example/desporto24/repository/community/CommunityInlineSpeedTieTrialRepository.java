package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityInlineSpeedTieTrial;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommunityInlineSpeedTieTrialRepository extends JpaRepository<CommunityInlineSpeedTieTrial, Long> {
    List<CommunityInlineSpeedTieTrial> findByTournamentIdOrderByIdAsc(Long tournamentId);
}
