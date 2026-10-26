package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.exception.SessionNotFound;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.CommunityRoundEntry;
import com.example.desporto24.model.dto.session.SessionTeamPlayerResponse;
import com.example.desporto24.model.dto.session.SessionTeamSuggestionResponse;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityRoundEntryRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Service
@RequiredArgsConstructor
public class SessionTeamSuggestionService {

    private final SessionRepository sessionRepository;
    private final ProfileRepository profileRepository;
    private final SessionParticipantRepository participantRepository;
    private final CommunityRoundEntryRepository roundEntryRepository;
    private final CommunityExcelTeamBalancer excelBalancer;

    @Transactional
    public SessionTeamSuggestionResponse confirm(Long sessionId, String username) {
        return confirm(sessionId, username, "FORM");
    }

    @Transactional
    public SessionTeamSuggestionResponse confirm(Long sessionId, String username, String source) {
        // Serialise confirmation with joins, departures, removals and waitlist promotions.
        // Otherwise a confirmed assignment can be based on a stale participant roster.
        sessionRepository.findByIdForUpdate(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        SessionTeamSuggestionResponse suggestion = suggest(sessionId, username, source);

        List<SessionParticipant> participants = participantRepository
                .findBySessionIdAndStateOrderByJoinedAtAsc(sessionId, ParticipantState.ACTIVE);

        Map<String, String> teamByUsername = new HashMap<>();
        suggestion.teamA().forEach(player -> teamByUsername.put(player.username(), "A"));
        suggestion.teamB().forEach(player -> teamByUsername.put(player.username(), "B"));

        for (SessionParticipant participant : participants) {
            participant.setTeamCode(teamByUsername.get(participant.getProfile().getUsername()));
        }
        participantRepository.saveAll(participants);

        return suggestion;
    }

    @Transactional(readOnly = true)
    public SessionTeamSuggestionResponse suggest(Long sessionId, String username) {
        return suggest(sessionId, username, "FORM");
    }

    @Transactional(readOnly = true)
    public SessionTeamSuggestionResponse suggest(Long sessionId, String username, String source) {
        if (!"FORM".equals(source) && !"EXCEL_POINTS".equals(source)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "Origem de classificação inválida");
        }
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile actor = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        if (session.getProfile() == null || !session.getProfile().getId().equals(actor.getId())) {
            throw new AccessDeniedException("Só o criador pode gerar equipas");
        }
        if (session.getCommunity() == null) {
            throw new IllegalStateException("A sessão tem de pertencer a uma comunidade");
        }

        List<SessionParticipant> participants = participantRepository
                .findBySessionIdAndStateOrderByJoinedAtAsc(sessionId, ParticipantState.ACTIVE);

        int maxParticipants = session.getMaxParticipants() == null ? 0 : session.getMaxParticipants();
        if (maxParticipants < 2 || participants.size() < maxParticipants) {
            throw new IllegalStateException("A sessão tem de estar cheia para gerar equipas");
        }

        if ("EXCEL_POINTS".equals(source)) {
            return excelBalancer.suggest(session, participants);
        }

        Map<Long, PlayerStats> statsByProfile = new HashMap<>();
        for (CommunityRoundEntry entry : roundEntryRepository
                .findByRoundCommunityIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(
                        session.getCommunity().getId())) {
            Profile profile = entry.getProfile() != null
                    ? entry.getProfile()
                    : entry.getCommunityPlayer() == null ? null : entry.getCommunityPlayer().getProfile();
            if (profile == null) {
                continue;
            }
            statsByProfile.computeIfAbsent(profile.getId(), ignored -> new PlayerStats()).add(entry);
        }

        List<PlayerSeed> seeds = new ArrayList<>();
        double knownScoreTotal = 0.0;
        int knownCount = 0;

        for (SessionParticipant participant : participants) {
            PlayerStats stats = statsByProfile.get(participant.getProfile().getId());
            if (stats != null && stats.games > 0) {
                double score = stats.strength();
                knownScoreTotal += score;
                knownCount++;
                seeds.add(new PlayerSeed(participant, stats, score, true));
            } else {
                seeds.add(new PlayerSeed(participant, null, 0.0, false));
            }
        }

        double neutralScore = knownCount == 0 ? 0.0 : knownScoreTotal / knownCount;
        List<PlayerSeed> normalized = seeds.stream()
                .map(seed -> seed.statsAvailable
                        ? seed
                        : new PlayerSeed(seed.participant, null, neutralScore, false))
                .sorted(Comparator.comparingDouble(PlayerSeed::score).reversed())
                .toList();

        List<SessionTeamPlayerResponse> teamA = new ArrayList<>();
        List<SessionTeamPlayerResponse> teamB = new ArrayList<>();
        double scoreA = 0.0;
        double scoreB = 0.0;

        for (PlayerSeed seed : normalized) {
            boolean addToA;
            if (teamA.size() < teamB.size()) {
                addToA = true;
            } else if (teamB.size() < teamA.size()) {
                addToA = false;
            } else {
                addToA = scoreA <= scoreB;
            }

            SessionTeamPlayerResponse response = toResponse(seed);
            if (addToA) {
                teamA.add(response);
                scoreA += seed.score;
            } else {
                teamB.add(response);
                scoreB += seed.score;
            }
        }

        return new SessionTeamSuggestionResponse(
                List.copyOf(teamA),
                List.copyOf(teamB),
                round(scoreA),
                round(scoreB),
                knownCount,
                participants.size()
        );
    }

    private SessionTeamPlayerResponse toResponse(PlayerSeed seed) {
        PlayerStats stats = seed.stats;
        return new SessionTeamPlayerResponse(
                seed.participant.getProfile().getUsername(),
                round(seed.score),
                seed.statsAvailable,
                stats == null ? 0 : stats.games,
                stats == null ? 0 : stats.wins,
                stats == null ? 0 : stats.goals,
                stats == null ? 0 : stats.assists
        );
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private record PlayerSeed(
            SessionParticipant participant,
            PlayerStats stats,
            double score,
            boolean statsAvailable
    ) {
    }

    private static final class PlayerStats {
        private int games;
        private int wins;
        private int draws;
        private int goals;
        private int assists;
        private int mvps;
        private int cleanSheets;

        private void add(CommunityRoundEntry entry) {
            games++;
            if (entry.getResult() == CommunityMatchResult.WIN) {
                wins++;
            } else if (entry.getResult() == CommunityMatchResult.DRAW) {
                draws++;
            }
            goals += entry.getGoals();
            assists += entry.getAssists();
            if (entry.isMvp()) {
                mvps++;
            }
            if (entry.isCleanSheet()) {
                cleanSheets++;
            }
        }

        private double strength() {
            if (games == 0) {
                return 0.0;
            }
            double weighted = (wins * 3.0)
                    + draws
                    + (goals * 0.5)
                    + (assists * 0.35)
                    + mvps
                    + (cleanSheets * 0.25);
            return weighted / games;
        }
    }
}
