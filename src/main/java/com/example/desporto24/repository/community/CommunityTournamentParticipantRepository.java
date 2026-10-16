package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityTournamentParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityTournamentParticipantRepository
        extends JpaRepository<CommunityTournamentParticipant, Long> {

    List<CommunityTournamentParticipant> findByTournamentIdOrderByIdAsc(Long tournamentId);

    boolean existsByTournamentIdAndProfileId(Long tournamentId, Long profileId);
}
