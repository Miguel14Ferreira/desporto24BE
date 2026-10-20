package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityTournamentParticipant;
import com.example.desporto24.enumeration.TournamentParticipantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import java.util.Optional;

import java.util.List;

public interface CommunityTournamentParticipantRepository
        extends JpaRepository<CommunityTournamentParticipant, Long> {

    List<CommunityTournamentParticipant> findByTournamentIdOrderByIdAsc(Long tournamentId);

    boolean existsByTournamentIdAndProfileId(Long tournamentId, Long profileId);

    Optional<CommunityTournamentParticipant> findByTournamentIdAndProfileId(Long tournamentId, Long profileId);

    @EntityGraph(attributePaths = "profile")
    Slice<CommunityTournamentParticipant> findByTournamentId(Long tournamentId, Pageable pageable);

    @EntityGraph(attributePaths = "profile")
    Slice<CommunityTournamentParticipant> findByTournamentIdAndStatus(
            Long tournamentId, TournamentParticipantStatus status, Pageable pageable);
}
