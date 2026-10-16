package com.example.desporto24.service;

import com.example.desporto24.enumeration.TournamentMatchStatus;
import com.example.desporto24.enumeration.TournamentParticipantStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityTournamentParticipant;
import com.example.desporto24.model.dto.community.CommunityTournamentStandingRowResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentStandingsResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityTournamentMatchRepository;
import com.example.desporto24.repository.community.CommunityTournamentParticipantRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommunityTournamentStandingsService {
    private static final int MAX_PLAYERS = 32;
    private static final int MAX_MATCHES = MAX_PLAYERS * (MAX_PLAYERS - 1) / 2;

    private final ProfileRepository profileRepository;
    private final CommunityMemberRepository memberRepository;
    private final CommunityTournamentRepository tournamentRepository;
    private final CommunityTournamentParticipantRepository participantRepository;
    private final CommunityTournamentMatchRepository matchRepository;

    /**
     * Recalculate the table from the stored scores on every read. Correcting a result
     * immediately changes the table; no separate mutable standings cache is needed.
     */
    @Transactional(readOnly = true)
    public CommunityTournamentStandingsResponse standings(
            Long communityId, Long tournamentId, String username) {
        Profile actor = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
        if (!memberRepository.existsByCommunityIdAndProfileId(communityId, actor.getId())) {
            throw new AccessDeniedException("Não pertences a esta comunidade");
        }
        var tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Torneio não encontrado"));
        if (!tournament.getCommunity().getId().equals(communityId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Torneio não encontrado");
        }

        var participantSlice = participantRepository.findByTournamentIdAndStatus(
                tournamentId, TournamentParticipantStatus.CONFIRMED,
                PageRequest.of(0, MAX_PLAYERS + 1, Sort.by("id")));
        if (participantSlice.hasNext() || participantSlice.getNumberOfElements() > MAX_PLAYERS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Número de jogadores fora do limite da liga");
        }
        List<Row> rows = new ArrayList<>();
        Map<Long, Row> byEnrollmentId = new HashMap<>();
        for (CommunityTournamentParticipant participant : participantSlice.getContent()) {
            Row row = new Row(participant);
            rows.add(row);
            byEnrollmentId.put(participant.getId(), row);
        }

        var completedSlice = matchRepository.findByTournamentIdAndStatus(
                tournamentId, TournamentMatchStatus.COMPLETED,
                PageRequest.of(0, MAX_MATCHES + 1, Sort.by("id")));
        if (completedSlice.hasNext() || completedSlice.getNumberOfElements() > MAX_MATCHES) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Número de jogos fora do limite da liga");
        }
        for (var match : completedSlice.getContent()) {
            Row home = byEnrollmentId.get(match.getHomeParticipant().getId());
            Row away = byEnrollmentId.get(match.getAwayParticipant().getId());
            Integer homeScore = match.getHomeScore();
            Integer awayScore = match.getAwayScore();
            if (home == null || away == null || home == away
                    || homeScore == null || awayScore == null || homeScore < 0 || awayScore < 0) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Confronto concluído com dados inconsistentes");
            }
            home.played++;
            away.played++;
            home.goalsFor += homeScore;
            home.goalsAgainst += awayScore;
            away.goalsFor += awayScore;
            away.goalsAgainst += homeScore;
            if (homeScore > awayScore) {
                home.won++;
                away.lost++;
                home.points += tournament.getWinPoints();
                away.points += tournament.getLossPoints();
            } else if (homeScore < awayScore) {
                home.lost++;
                away.won++;
                home.points += tournament.getLossPoints();
                away.points += tournament.getWinPoints();
            } else {
                home.drawn++;
                away.drawn++;
                home.points += tournament.getDrawPoints();
                away.points += tournament.getDrawPoints();
            }
        }

        // Sporting tie-breakers: points, goal difference, goals for, wins.
        // Profile ID is only a stable last resort, not a sporting tie-breaker.
        rows.sort(Comparator.comparingLong((Row row) -> row.points).reversed()
                .thenComparing(Comparator.comparingInt(Row::goalDifference).reversed())
                .thenComparing(Comparator.comparingInt((Row row) -> row.goalsFor).reversed())
                .thenComparing(Comparator.comparingInt((Row row) -> row.won).reversed())
                .thenComparing(row -> row.profileId));

        List<CommunityTournamentStandingRowResponse> standings = new ArrayList<>();
        int position = 1;
        for (Row row : rows) {
            standings.add(new CommunityTournamentStandingRowResponse(
                    position++, row.profileId, row.username, row.displayName,
                    row.played, row.won, row.drawn, row.lost, row.goalsFor,
                    row.goalsAgainst, row.goalDifference(), row.points));
        }
        return new CommunityTournamentStandingsResponse(
                tournamentId, tournament.getStatus(), matchRepository.countByTournamentId(tournamentId),
                completedSlice.getNumberOfElements(), tournament.getWinPoints(),
                tournament.getDrawPoints(), tournament.getLossPoints(), List.copyOf(standings));
    }

    private static final class Row {
        private final Long profileId;
        private final String username;
        private final String displayName;
        private int played;
        private int won;
        private int drawn;
        private int lost;
        private int goalsFor;
        private int goalsAgainst;
        private long points;

        private Row(CommunityTournamentParticipant participant) {
            var profile = participant.getProfile();
            this.profileId = profile.getId();
            this.username = profile.getUsername();
            String nickname = profile.getNickname();
            String fullName = profile.getFullName();
            this.displayName = nickname != null && !nickname.isBlank() ? nickname
                    : fullName != null && !fullName.isBlank() ? fullName : profile.getUsername();
        }

        private int goalDifference() {
            return goalsFor - goalsAgainst;
        }
    }
}
