package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.community.CommunityTournamentResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentStandingRowResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityTournamentFinalizationService {
    private final ProfileRepository profileRepository;
    private final CommunityMemberRepository memberRepository;
    private final CommunityTournamentRepository tournamentRepository;
    private final CommunityTournamentStandingsService standingsService;

    /**
     * Finalization and score changes take the same pessimistic tournament lock.
     * Once finalized, the existing results endpoint refuses all changes.
     */
    public CommunityTournamentResponse complete(Long communityId, Long tournamentId, String username) {
        Profile actor = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
        var membership = memberRepository.findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));
        if (membership.getRole() != CommunityRole.OWNER && membership.getRole() != CommunityRole.ADMIN) {
            throw new AccessDeniedException("Apenas Owner/Admin pode encerrar o torneio");
        }

        var tournament = tournamentRepository.findByIdForUpdate(tournamentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Torneio não encontrado"));
        if (!tournament.getCommunity().getId().equals(communityId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Torneio não encontrado");
        }
        // Repeated requests return the immutable trophy without modifying the snapshot.
        if (tournament.getStatus() == TournamentStatus.COMPLETED) {
            return CommunityTournamentResponse.from(tournament);
        }
        if (tournament.getStatus() != TournamentStatus.IN_PROGRESS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O torneio ainda não começou");
        }

        var table = standingsService.standings(communityId, tournamentId, username);
        List<CommunityTournamentStandingRowResponse> rows = table.standings();
        int playerCount = rows.size();
        if (playerCount < 2 || playerCount > 32) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Número de jogadores inválido");
        }
        long requiredMatches = (long) playerCount * (playerCount - 1) / 2;
        // Do not finalize a draft, a partially constructed calendar, or unfinished games.
        if (table.totalMatches() != requiredMatches || table.completedMatches() != requiredMatches) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Todos os confrontos da liga precisam de estar concluídos");
        }
        for (var row : rows) {
            if (row.played() != playerCount - 1) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "A classificação tem jornadas ou participantes inconsistentes");
            }
        }

        var leader = rows.get(0);
        boolean tied = rows.size() > 1 && equalSportingRank(leader, rows.get(1));
        tournament.setChampionTied(tied);
        tournament.setChampionProfileId(tied ? null : leader.profileId());
        tournament.setChampionUsername(tied ? null : leader.username());
        tournament.setChampionDisplayName(tied ? null : leader.displayName());
        tournament.setChampionPoints(tied ? null : leader.points());
        tournament.setCompletedAt(Instant.now());
        tournament.setStatus(TournamentStatus.COMPLETED);
        return CommunityTournamentResponse.from(tournamentRepository.save(tournament));
    }

    // Profile ID orders equal rows in the standings API but is never used as a
    // sporting tie-breaker to award a sole champion.
    private boolean equalSportingRank(CommunityTournamentStandingRowResponse a,
                                      CommunityTournamentStandingRowResponse b) {
        return a.points() == b.points()
                && a.goalDifference() == b.goalDifference()
                && a.goalsFor() == b.goalsFor()
                && a.won() == b.won();
    }
}
