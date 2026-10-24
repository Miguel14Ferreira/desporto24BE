package com.example.desporto24.service;

import com.example.desporto24.enumeration.TournamentEntryType;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.TournamentFormat;
import com.example.desporto24.enumeration.TournamentParticipantStatus;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityTournament;
import com.example.desporto24.model.community.CommunityTournamentMatch;
import com.example.desporto24.model.community.CommunityTournamentParticipant;
import com.example.desporto24.model.dto.community.CommunityTournamentMatchesPageResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentMatchResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentScheduleSummaryResponse;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityTournamentScheduleService {
    private static final int MAX_CONFIRMED_PLAYERS = 32;
    private static final int PAGE_SIZE = 30;

    private final ProfileRepository profileRepository;
    private final CommunityMemberRepository memberRepository;
    private final CommunityTournamentRepository tournamentRepository;
    private final CommunityTournamentParticipantRepository participantRepository;
    private final CommunityTournamentMatchRepository matchRepository;

    public CommunityTournamentScheduleSummaryResponse generate(
            Long communityId, Long tournamentId, String username) {
        Profile actor = profile(username);
        var membership = memberRepository.findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));
        if (membership.getRole() != CommunityRole.ADMIN && membership.getRole() != CommunityRole.OWNER) {
            throw new AccessDeniedException("Só Owner/Admin pode publicar o calendário");
        }

        // The same row lock is acquired by enrollment/confirmation/withdrawal operations.
        // It serializes roster writes and prevents two simultaneous schedule generations.
        CommunityTournament tournament = tournamentRepository.findByIdForUpdate(tournamentId)
                .orElseThrow(() -> notFound());
        if (!tournament.getCommunity().getId().equals(communityId)) {
            throw notFound();
        }
        if (tournament.getScoringMode() == com.example.desporto24.enumeration.TournamentScoringMode.RACE_POINTS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Karting usa corridas das Jornadas, não confrontos individuais");
        }
        if (tournament.getEntryType() == TournamentEntryType.TEAM) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "O calendário por equipas precisa de confrontos entre equipas");
        }
        if (tournament.getStatus() != TournamentStatus.DRAFT
                || tournament.getFormat() != TournamentFormat.LEAGUE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Torneio não disponível para gerar calendário");
        }
        if (matchRepository.existsByTournamentId(tournamentId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este torneio já tem confrontos");
        }

        // Bounded query: no full-roster download for abnormally large competitions.
        var slice = participantRepository.findByTournamentIdAndStatus(
                tournamentId, TournamentParticipantStatus.CONFIRMED,
                PageRequest.of(0, MAX_CONFIRMED_PLAYERS + 1, Sort.by("id")));
        if (slice.hasNext() || slice.getNumberOfElements() > MAX_CONFIRMED_PLAYERS) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Esta versão suporta no máximo 32 jogadores confirmados");
        }

        List<CommunityTournamentParticipant> confirmed = new ArrayList<>(slice.getContent());
        if (confirmed.size() < 2) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Confirma pelo menos dois jogadores antes de gerar o calendário");
        }
        for (var participant : confirmed) {
            if (!memberRepository.existsByCommunityIdAndProfileId(
                    communityId, participant.getProfile().getId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Existem jogadores confirmados que já não pertencem à comunidade");
            }
        }

        // Berger's circle method, with a bye (null seat) for odd player counts.
        List<CommunityTournamentParticipant> seats = new ArrayList<>(confirmed);
        if (seats.size() % 2 != 0) {
            seats.add(null);
        }
        int rounds = seats.size() - 1;
        List<CommunityTournamentMatch> matches = new ArrayList<>();
        for (int round = 0; round < rounds; round++) {
            for (int i = 0; i < seats.size() / 2; i++) {
                var left = seats.get(i);
                var right = seats.get(seats.size() - 1 - i);
                if (left == null || right == null) {
                    continue;
                }
                boolean reverse = (round + i) % 2 == 1;
                matches.add(CommunityTournamentMatch.builder()
                        .tournament(tournament)
                        .homeParticipant(reverse ? right : left)
                        .awayParticipant(reverse ? left : right)
                        .roundNumber(round + 1)
                        .build());
            }
            var last = seats.remove(seats.size() - 1);
            seats.add(1, last);
        }

        matchRepository.saveAll(matches);
        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        tournament.setStartedAt(Instant.now());
        tournamentRepository.save(tournament);
        return new CommunityTournamentScheduleSummaryResponse(
                tournamentId, TournamentStatus.IN_PROGRESS, confirmed.size(), rounds, matches.size());
    }

    @Transactional(readOnly = true)
    public CommunityTournamentMatchesPageResponse matches(
            Long communityId, Long tournamentId, String username, int page) {
        if (page < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Página inválida");
        }
        Profile actor = profile(username);
        if (!memberRepository.existsByCommunityIdAndProfileId(communityId, actor.getId())) {
            throw new AccessDeniedException("Não pertences a esta comunidade");
        }
        var tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> notFound());
        if (!tournament.getCommunity().getId().equals(communityId)) {
            throw notFound();
        }
        if (tournament.getScoringMode() == com.example.desporto24.enumeration.TournamentScoringMode.RACE_POINTS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Karting usa corridas das Jornadas, não confrontos individuais");
        }
        if (tournament.getEntryType() == TournamentEntryType.TEAM) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Calendário por equipas ainda não disponível");
        }
        var pageable = PageRequest.of(page, PAGE_SIZE,
                Sort.by(Sort.Direction.ASC, "roundNumber").and(Sort.by(Sort.Direction.ASC, "id")));
        var slice = matchRepository.findByTournamentId(tournamentId, pageable);
        return new CommunityTournamentMatchesPageResponse(
                slice.getContent().stream().map(match -> {
                    var home = match.getHomeParticipant().getProfile();
                    var away = match.getAwayParticipant().getProfile();
                    return new CommunityTournamentMatchResponse(
                            match.getId(), match.getRoundNumber(),
                            home.getId(), home.getUsername(),
                            away.getId(), away.getUsername(),
                            match.getStatus(), match.getHomeScore(), match.getAwayScore());
                }).toList(), page, slice.hasNext());
    }

    private Profile profile(String username) {
        return profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
    }

    private ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Torneio não encontrado");
    }
}
