package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.TournamentEntryType;
import com.example.desporto24.enumeration.TournamentParticipantStatus;
import com.example.desporto24.model.community.CommunityTournament;
import com.example.desporto24.model.community.CommunityTournamentSuspension;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityTournamentParticipantRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import com.example.desporto24.repository.community.CommunityTournamentSuspensionRepository;
import com.example.desporto24.repository.community.CommunityTournamentTeamPlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Pre-match advisory roster check, including players enrolled before a suspension.
 * Does not alter historical results, remove registrations or assert a match lineup.
 */
@Service
@RequiredArgsConstructor
public class CommunityTournamentEligibilityService {
    private final ProfileRepository profiles;
    private final CommunityMemberRepository members;
    private final CommunityTournamentRepository tournaments;
    private final CommunityTournamentParticipantRepository individualParticipants;
    private final CommunityTournamentTeamPlayerRepository teamPlayers;
    private final CommunityTournamentSuspensionRepository suspensions;

    public record IneligibleEntry(Long profileId, Long teamId,
                                  TournamentParticipantStatus participantStatus,
                                  Instant suspendedUntil) {}

    public record EligibilityOverview(Long tournamentId, TournamentEntryType entryType,
                                      Instant checkedAt, List<IneligibleEntry> ineligibleEntries) {}

    @Transactional(readOnly = true)
    public EligibilityOverview overview(Long communityId, Long tournamentId, String username) {
        if (communityId == null || tournamentId == null || username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido inválido");
        }
        var actor = profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Conta não encontrada"));
        var member = members.findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem acesso à comunidade"));
        if (member.getRole() != CommunityRole.OWNER && member.getRole() != CommunityRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas responsáveis podem consultar elegibilidade");
        }
        CommunityTournament tournament = tournaments.findById(tournamentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Torneio não encontrado"));
        if (!communityId.equals(tournament.getCommunity().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Torneio não encontrado");
        }
        Instant now = Instant.now();
        List<IneligibleEntry> entries;
        if (tournament.getEntryType() == TournamentEntryType.TEAM) {
            entries = teamPlayers.findByTournamentIdOrderByTeamIdAscIdAsc(tournamentId)
                    .stream().map(p -> new IneligibleEntry(p.getProfile().getId(),
                            p.getTeam().getId(), null, null)).toList();
        } else {
            entries = individualParticipants.findByTournamentIdOrderByIdAsc(tournamentId)
                    .stream().map(p -> new IneligibleEntry(p.getProfile().getId(),
                            null, p.getStatus(), null)).toList();
        }
        if (entries.isEmpty()) {
            return new EligibilityOverview(tournamentId, tournament.getEntryType(), now, List.of());
        }

        List<Long> profileIds = entries.stream().map(IneligibleEntry::profileId).distinct().toList();
        Map<Long, Instant> blockedUntil = suspensions.findActiveForProfiles(communityId, profileIds, now)
                .stream().collect(Collectors.toMap(
                        CommunityTournamentSuspension::getTargetProfileId,
                        CommunityTournamentSuspension::getEndsAt,
                        (first, second) -> first.isAfter(second) ? first : second));

        var affected = entries.stream()
                .filter(p -> blockedUntil.containsKey(p.profileId()))
                .map(p -> new IneligibleEntry(p.profileId(), p.teamId(),
                        p.participantStatus(), blockedUntil.get(p.profileId())))
                .sorted(Comparator.comparing(IneligibleEntry::profileId))
                .toList();
        return new EligibilityOverview(tournamentId, tournament.getEntryType(), now, affected);
    }
}
