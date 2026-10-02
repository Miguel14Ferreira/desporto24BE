package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunityPlayer;
import com.example.desporto24.model.community.CommunityRound;
import com.example.desporto24.model.community.CommunityRoundEntry;
import com.example.desporto24.model.community.CommunitySeason;
import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityPlayerRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityRoundEntryRepository;
import com.example.desporto24.repository.community.CommunityRoundRepository;
import com.example.desporto24.repository.community.CommunitySeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityCompetitionService {

    private final CommunityRepository communityRepository;
    private final CommunityMemberRepository memberRepository;
    private final CommunityPlayerRepository playerRepository;
    private final CommunityRoundRepository roundRepository;
    private final CommunityRoundEntryRepository entryRepository;
    private final CommunitySeasonRepository seasonRepository;
    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public CommunityCompetitionOverviewResponse overview(Long communityId, String username) {
        return overview(communityId, username, null);
    }

    @Transactional(readOnly = true)
    public CommunityCompetitionOverviewResponse overview(
            Long communityId,
            String username,
            Long seasonId) {
        Profile actor = profile(username);
        Community community = community(communityId);
        requireAccess(community, actor);

        if (seasonId != null) {
            CommunitySeason selected = seasonRepository.findById(seasonId)
                    .orElseThrow(() -> new IllegalArgumentException("Época não encontrada"));
            if (!selected.getCommunity().getId().equals(communityId)) {
                throw new IllegalArgumentException("A época não pertence a esta comunidade");
            }
        }

        Long activeSeasonId = seasonRepository.findByCommunityIdAndActiveTrue(communityId)
                .map(CommunitySeason::getId)
                .orElse(null);

        List<CommunityRound> rounds = seasonId == null
                ? roundRepository.findByCommunityIdOrderByPlayedAtDescIdDesc(communityId)
                : roundRepository.findByCommunityIdAndSeasonIdOrderByPlayedAtDescIdDesc(
                        communityId, seasonId);

        return new CommunityCompetitionOverviewResponse(
                settings(community),
                seasonRepository.findByCommunityIdOrderByCreatedAtDesc(communityId)
                        .stream()
                        .map(season -> seasonResponse(community, season))
                        .toList(),
                seasonId,
                activeSeasonId,
                rounds.stream().map(this::roundResponse).toList(),
                standings(community, seasonId)
        );
    }

    public CommunitySeasonResponse createSeason(
            Long communityId,
            String username,
            CreateCommunitySeasonRequest request) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);
        Community community = community(communityId);

        seasonRepository.findByCommunityIdAndActiveTrue(communityId)
                .ifPresent(existing -> {
                    throw new IllegalStateException(
                            "Encerra a época ativa antes de criares outra"
                    );
                });

        CommunitySeason season = seasonRepository.save(CommunitySeason.builder()
                .community(community)
                .name(request.name().trim())
                .startsAt(request.startsAt() == null ? Instant.now() : request.startsAt())
                .active(true)
                .createdAt(Instant.now())
                .build());

        return seasonResponse(community, season);
    }

    public CommunitySeasonResponse closeSeason(
            Long communityId,
            Long seasonId,
            String username) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);
        Community community = community(communityId);

        CommunitySeason season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new IllegalArgumentException("Época não encontrada"));
        if (!season.getCommunity().getId().equals(communityId)) {
            throw new IllegalArgumentException("A época não pertence a esta comunidade");
        }
        if (!season.isActive()) {
            throw new IllegalStateException("Esta época já está encerrada");
        }

        season.setActive(false);
        season.setEndedAt(Instant.now());
        CommunitySeason saved = seasonRepository.save(season);
        return seasonResponse(community, saved);
    }

    public CommunityCompetitionSettingsResponse updateSettings(
            Long communityId,
            String username,
            UpdateCommunityCompetitionSettingsRequest request) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);
        Community community = community(communityId);

        if (request.rankingsEnabled() != null) {
            community.setRankingsEnabled(request.rankingsEnabled());
        }
        if (request.winPoints() != null) {
            community.setWinPoints(request.winPoints());
        }
        if (request.drawPoints() != null) {
            community.setDrawPoints(request.drawPoints());
        }
        if (request.lossPoints() != null) {
            community.setLossPoints(request.lossPoints());
        }

        return settings(communityRepository.save(community));
    }

    public CommunityRoundResponse createRound(
            Long communityId,
            String username,
            CreateCommunityRoundRequest request) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);
        Community community = community(communityId);

        if (!community.isRankingsEnabled()) {
            throw new IllegalStateException("Ativa primeiro o módulo de classificação desta comunidade");
        }

        Set<String> usernames = new HashSet<>();
        for (CommunityRoundEntryRequest entry : request.entries()) {
            String normalized = entry.username().trim().toLowerCase(Locale.ROOT);
            if (!usernames.add(normalized)) {
                throw new IllegalArgumentException("Um jogador só pode aparecer uma vez por jornada");
            }
        }

        CommunitySeason activeSeason = seasonRepository
                .findByCommunityIdAndActiveTrue(communityId)
                .orElse(null);

        CommunityRound round = roundRepository.save(CommunityRound.builder()
                .community(community)
                .season(activeSeason)
                .name(request.name().trim())
                .playedAt(request.playedAt())
                .createdAt(Instant.now())
                .build());

        List<CommunityRoundEntry> entries = new ArrayList<>();
        for (CommunityRoundEntryRequest requestEntry : request.entries()) {
            Profile player = profile(requestEntry.username().trim());

            if (!memberRepository.existsByCommunityIdAndProfileId(communityId, player.getId())) {
                throw new IllegalArgumentException(
                        "O jogador @" + player.getUsername() + " não pertence à comunidade"
                );
            }

            CommunityPlayer communityPlayer = playerRepository
                    .findByCommunityIdAndProfileId(communityId, player.getId())
                    .orElseGet(() -> playerRepository.save(CommunityPlayer.builder()
                            .community(community)
                            .displayName(playerDisplayName(player))
                            .normalizedName(normalizePlayerName(playerDisplayName(player)))
                            .profile(player)
                            .createdAt(Instant.now())
                            .build()));

            entries.add(CommunityRoundEntry.builder()
                    .round(round)
                    .profile(player)
                    .communityPlayer(communityPlayer)
                    .team(trimToNull(requestEntry.team()))
                    .result(requestEntry.result())
                    .goals(valueOrZero(requestEntry.goals()))
                    .assists(valueOrZero(requestEntry.assists()))
                    .mvp(requestEntry.mvp())
                    .cleanSheet(requestEntry.cleanSheet())
                    .build());
        }

        entryRepository.saveAll(entries);
        return roundResponse(round);
    }

    public void deleteRound(Long communityId, Long roundId, String username) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);

        CommunityRound round = roundRepository.findById(roundId)
                .orElseThrow(() -> new IllegalArgumentException("Jornada não encontrada"));

        if (!round.getCommunity().getId().equals(communityId)) {
            throw new IllegalArgumentException("A jornada não pertence a esta comunidade");
        }
        if (round.getSeason() != null && !round.getSeason().isActive()) {
            throw new IllegalStateException("Não podes apagar jornadas de uma época encerrada");
        }

        entryRepository.deleteByRoundId(roundId);
        roundRepository.delete(round);
    }

    private List<CommunityStandingResponse> standings(Community community, Long seasonId) {
        Map<String, StandingAccumulator> table = new LinkedHashMap<>();

        for (CommunityMember member : memberRepository.findByCommunityIdOrderByJoinedAtAsc(community.getId())) {
            Profile profile = member.getProfile();
            CommunityPlayer communityPlayer = playerRepository
                    .findByCommunityIdAndProfileId(community.getId(), profile.getId())
                    .orElse(null);
            table.putIfAbsent(standingKey(communityPlayer, profile), new StandingAccumulator(communityPlayer, profile));
        }

        List<CommunityRoundEntry> sourceEntries = seasonId == null
                ? entryRepository.findByRoundCommunityIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(community.getId())
                : entryRepository.findByRoundSeasonIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(seasonId);

        for (CommunityRoundEntry entry : sourceEntries) {
            CommunityPlayer communityPlayer = entry.getCommunityPlayer();
            Profile profile = entry.getProfile() != null
                    ? entry.getProfile()
                    : communityPlayer == null ? null : communityPlayer.getProfile();
            StandingAccumulator row = table.computeIfAbsent(
                    standingKey(communityPlayer, profile),
                    ignored -> new StandingAccumulator(communityPlayer, profile)
            );
            row.add(entry);
        }

        int winPoints = pointsOrDefault(community.getWinPoints(), 3);
        int drawPoints = pointsOrDefault(community.getDrawPoints(), 1);
        int lossPoints = pointsOrDefault(community.getLossPoints(), 0);

        List<StandingAccumulator> ordered = table.values().stream()
                .sorted(
                        Comparator.comparingInt((StandingAccumulator row) -> row.points(winPoints, drawPoints, lossPoints))
                                .reversed()
                                .thenComparing(Comparator.comparingInt(StandingAccumulator::wins).reversed())
                                .thenComparing(Comparator.comparingInt(StandingAccumulator::goals).reversed())
                                .thenComparing(StandingAccumulator::displayName, String.CASE_INSENSITIVE_ORDER)
                )
                .toList();

        List<CommunityStandingResponse> response = new ArrayList<>();
        for (int index = 0; index < ordered.size(); index++) {
            StandingAccumulator row = ordered.get(index);
            int games = row.games();
            response.add(new CommunityStandingResponse(
                    index + 1,
                    row.profileId(),
                    row.username(),
                    row.displayName(),
                    row.profilePicUrl(),
                    games,
                    row.wins(),
                    row.draws(),
                    row.losses(),
                    row.points(winPoints, drawPoints, lossPoints),
                    row.goals(),
                    row.assists(),
                    row.mvps(),
                    row.cleanSheets(),
                    games == 0 ? 0.0 : Math.round((row.wins() * 10000.0 / games)) / 100.0,
                    row.currentStreak(),
                    row.bestWinStreak()
            ));
        }
        return response;
    }

    private String standingKey(CommunityPlayer communityPlayer, Profile profile) {
        if (communityPlayer != null) return "player:" + communityPlayer.getId();
        if (profile != null) return "profile:" + profile.getId();
        throw new IllegalStateException("A estatística não tem jogador associado");
    }

    private CommunityRoundResponse roundResponse(CommunityRound round) {
        List<CommunityRoundEntryResponse> entries = entryRepository
                .findByRoundIdOrderByIdAsc(round.getId())
                .stream()
                .map(entry -> new CommunityRoundEntryResponse(
                        entry.getProfile().getId(),
                        entry.getProfile().getUsername(),
                        entry.getProfile().getFullName(),
                        entry.getProfile().getProfilePicUrl(),
                        entry.getTeam(),
                        entry.getResult(),
                        entry.getGoals(),
                        entry.getAssists(),
                        entry.isMvp(),
                        entry.isCleanSheet()
                ))
                .toList();

        return new CommunityRoundResponse(
                round.getId(),
                round.getName(),
                round.getPlayedAt(),
                entries
        );
    }

    private CommunitySeasonResponse seasonResponse(
            Community community,
            CommunitySeason season) {
        String championUsername = null;
        String championDisplayName = null;
        Integer championPoints = null;

        if (!season.isActive()) {
            List<CommunityStandingResponse> seasonStandings = standings(
                    community, season.getId());
            if (!seasonStandings.isEmpty() && seasonStandings.get(0).games() > 0) {
                CommunityStandingResponse champion = seasonStandings.get(0);
                championUsername = champion.username();
                championDisplayName = champion.fullName();
                championPoints = champion.points();
            }
        }

        return new CommunitySeasonResponse(
                season.getId(),
                season.getName(),
                season.getStartsAt(),
                season.getEndedAt(),
                season.isActive(),
                championUsername,
                championDisplayName,
                championPoints
        );
    }

    private CommunityCompetitionSettingsResponse settings(Community community) {
        return new CommunityCompetitionSettingsResponse(
                community.isRankingsEnabled(),
                pointsOrDefault(community.getWinPoints(), 3),
                pointsOrDefault(community.getDrawPoints(), 1),
                pointsOrDefault(community.getLossPoints(), 0)
        );
    }

    private void requireAccess(Community community, Profile actor) {
        boolean member = memberRepository.existsByCommunityIdAndProfileId(
                community.getId(), actor.getId());

        if (!member && community.getVisibility() == CommunityVisibility.PRIVATE) {
            throw new AccessDeniedException("Não tens acesso a esta comunidade");
        }
    }

    private void requireAdmin(Long communityId, Profile actor) {
        CommunityMember membership = memberRepository
                .findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));

        if (membership.getRole() != CommunityRole.OWNER
                && membership.getRole() != CommunityRole.ADMIN) {
            throw new AccessDeniedException("Apenas Owners e Admins podem gerir a competição");
        }
    }

    private Profile profile(String username) {
        return profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
    }

    private Community community(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comunidade não encontrada"));
    }

    private int pointsOrDefault(Integer value, int fallback) {
        return value == null ? fallback : value;
    }

    private String playerDisplayName(Profile profile) {
        String nickname = trimToNull(profile.getNickname());
        if (nickname != null) {
            return nickname;
        }
        String fullName = trimToNull(profile.getFullName());
        return fullName != null ? fullName : profile.getUsername();
    }

    private String normalizePlayerName(String value) {
        return value.trim().toLowerCase(Locale.ROOT);
    }

    private int valueOrZero(Integer value) {
        return value == null ? 0 : value;
    }

    private String trimToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static final class StandingAccumulator {
        private final CommunityPlayer communityPlayer;
        private final Profile profile;
        private int games;
        private int wins;
        private int draws;
        private int losses;
        private int goals;
        private int assists;
        private int mvps;
        private int cleanSheets;
        private CommunityMatchResult lastResult;
        private int currentStreakCount;
        private int currentWinStreak;
        private int bestWinStreak;

        private StandingAccumulator(CommunityPlayer communityPlayer, Profile profile) {
            this.communityPlayer = communityPlayer;
            this.profile = profile;
        }

        private void add(CommunityRoundEntry entry) {
            games++;
            goals += entry.getGoals();
            assists += entry.getAssists();
            if (entry.isMvp()) mvps++;
            if (entry.isCleanSheet()) cleanSheets++;

            switch (entry.getResult()) {
                case WIN -> {
                    wins++;
                    currentWinStreak++;
                    bestWinStreak = Math.max(bestWinStreak, currentWinStreak);
                }
                case DRAW -> {
                    draws++;
                    currentWinStreak = 0;
                }
                case LOSS -> {
                    losses++;
                    currentWinStreak = 0;
                }
            }

            if (entry.getResult() == lastResult) {
                currentStreakCount++;
            } else {
                lastResult = entry.getResult();
                currentStreakCount = 1;
            }
        }

        private int points(int winPoints, int drawPoints, int lossPoints) {
            return wins * winPoints + draws * drawPoints + losses * lossPoints;
        }

        private String currentStreak() {
            if (lastResult == null || currentStreakCount == 0) {
                return "-";
            }
            String code = switch (lastResult) {
                case WIN -> "W";
                case DRAW -> "D";
                case LOSS -> "L";
            };
            return code + currentStreakCount;
        }

        private Long profileId() { return profile == null ? null : profile.getId(); }
        private String username() { return profile == null ? null : profile.getUsername(); }
        private String displayName() {
            if (communityPlayer != null) return communityPlayer.getDisplayName();
            if (profile == null) return "Jogador";
            String fullName = profile.getFullName();
            return fullName == null || fullName.isBlank() ? profile.getUsername() : fullName;
        }
        private String profilePicUrl() { return profile == null ? null : profile.getProfilePicUrl(); }
        private int games() { return games; }
        private int wins() { return wins; }
        private int draws() { return draws; }
        private int losses() { return losses; }
        private int goals() { return goals; }
        private int assists() { return assists; }
        private int mvps() { return mvps; }
        private int cleanSheets() { return cleanSheets; }
        private int bestWinStreak() { return bestWinStreak; }
    }
}
