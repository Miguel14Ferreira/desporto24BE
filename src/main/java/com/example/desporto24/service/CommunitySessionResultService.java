package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.CommunityRound;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import com.example.desporto24.model.dto.community.CommunityRoundResponse;
import com.example.desporto24.model.dto.community.CommunitySessionResultRequest;
import com.example.desporto24.model.dto.community.CreateCommunityRoundRequest;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityRoundRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommunitySessionResultService {
    private final CommunityRepository communities;
    private final CommunityMemberRepository memberships;
    private final ProfileRepository profiles;
    private final SessionRepository sessions;
    private final SessionParticipantRepository participants;
    private final CommunityRoundRepository rounds;
    private final CommunityCompetitionService competition;

    /** A completed session has one independently editable, season-bound competition round.
     * Locking the session row serializes two simultaneous confirmations. The database
     * also enforces unique source_session_id in case another path is introduced later.
     */
    @Transactional
    public CommunityRoundResponse record(Long communityId, Long sessionId,
                                         String actorUsername, CommunitySessionResultRequest input) {
        var community = communities.findById(communityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comunidade inexistente"));
        Profile actor = profiles.findUserByUsernameIgnoreCase(actorUsername)
                .orElseThrow(() -> new AccessDeniedException("Utilizador desconhecido"));
        var membership = memberships.findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));
        if (membership.getRole() != CommunityRole.OWNER && membership.getRole() != CommunityRole.ADMIN) {
            throw new AccessDeniedException("Só a administração pode validar os resultados das sessões");
        }
        if (!community.isRankingsEnabled()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A classificação não está ativa nesta comunidade");
        }
        Session session = sessions.lockForCommunityResult(communityId, sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Sessão indisponível nesta comunidade"));
        if (session.getStatus() == SessionStatus.CANCELLED ||
                session.getGameDate() == null ||
                !session.getGameDate().toInstant().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Só é possível registar resultados de sessões realizadas e não canceladas");
        }
        if (rounds.existsByCommunityIdAndSourceSessionId(communityId, sessionId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Esta sessão já tem uma jornada registada");
        }
        List<SessionParticipant> eligible = participants.findBySessionIdAndStateOrderByJoinedAtAsc(
                sessionId, ParticipantState.ACTIVE);
        Map<String, SessionParticipant> eligibleByName = new HashMap<>();
        for (var participant : eligible) {
            if (participant.getAttendanceStatus() != AttendanceStatus.NO_SHOW &&
                    participant.getAttendanceStatus() != AttendanceStatus.LATE_CANCELLED) {
                eligibleByName.put(participant.getProfile().getUsername().toLowerCase(Locale.ROOT),
                        participant);
            }
        }
        if (input == null || input.outcome() == null || input.players() == null ||
                input.players().size() < 2 || input.players().size() > 100) {
            throw new IllegalArgumentException("Seleciona pelo menos dois participantes e indica o resultado");
        }
        Set<String> unique = new HashSet<>();
        int teamA = 0;
        int teamB = 0;
        var entries = new java.util.ArrayList<CommunityRoundEntryRequest>();
        for (var player : input.players()) {
            if (player == null || player.username() == null || player.team() == null)
                throw new IllegalArgumentException("Cada participante precisa de uma equipa");
            String key = player.username().trim().toLowerCase(Locale.ROOT);
            SessionParticipant registered = eligibleByName.get(key);
            if (registered == null || !unique.add(key)) {
                throw new IllegalArgumentException(
                        "Só os inscritos ativos e não repetidos podem contar para esta jornada");
            }
            if (player.team() == CommunitySessionResultRequest.Team.A) teamA++; else teamB++;
            CommunityMatchResult result = input.outcome() == CommunitySessionResultRequest.Outcome.DRAW
                    ? CommunityMatchResult.DRAW
                    : player.team().name().equals(input.outcome().name())
                        ? CommunityMatchResult.WIN : CommunityMatchResult.LOSS;
            // Goals/assists are unknown, not inferred from a quick W/D/L report.
            entries.add(new CommunityRoundEntryRequest(
                    registered.getProfile().getUsername(), player.team().name(),
                    result, 0, 0, false, false));
        }
        if (teamA == 0 || teamB == 0)
            throw new IllegalArgumentException("Seleciona jogadores para as duas equipas");

        var response = competition.createRound(communityId, actorUsername,
                new CreateCommunityRoundRequest(
                        "Sessão #" + sessionId, session.getGameDate().toInstant(),
                        entries, session.getSport()));
        CommunityRound round = rounds.findById(response.id())
                .orElseThrow(() -> new IllegalStateException("Jornada não guardada"));
        round.setSourceSessionId(sessionId);
        rounds.saveAndFlush(round);
        return response;
    }
}
