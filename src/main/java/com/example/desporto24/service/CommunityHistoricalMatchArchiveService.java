package com.example.desporto24.service;

import com.example.desporto24.model.Profile;
import com.example.desporto24.enumeration.TournamentEntryType;
import com.example.desporto24.enumeration.TournamentFormat;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.community.*;
import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CommunityHistoricalMatchArchiveService {
    private final CommunityHistoricalMatchPreviewService previewService;
    private final ProfileRepository profileRepository;
    private final CommunityMemberRepository memberRepository;
    private final CommunityRepository communityRepository;
    private final CommunityHistoricalMatchArchiveRepository archiveRepository;
    private final CommunityTournamentRepository tournamentRepository;
    private final CommunityHistoricalMatchArchiveRowRepository rowRepository;

    /**
     * Separate, immutable historical source data. No live fixture, team, ranking
     * or profile is created or modified by this operation.
     */
    @Transactional
    public CommunityHistoricalMatchArchiveResponse confirm(
            Long communityId, String username, MultipartFile file,
            CommunityImportMappingRequest mapping,
            CommunityHistoricalMatchArchiveConfirmRequest context) {
        if (context == null || context.title() == null
                || context.title().trim().length() < 2
                || context.title().trim().length() > 120) {
            throw new IllegalArgumentException("Indica um título entre 2 e 120 caracteres");
        }
        String type = context.entryType() == null ? ""
                : context.entryType().trim().toUpperCase(Locale.ROOT);
        if (!List.of("INDIVIDUAL", "TEAM").contains(type)) {
            throw new IllegalArgumentException("Escolhe histórico individual ou por equipas");
        }
        boolean asTournament = Boolean.TRUE.equals(context.createTournament());
        TournamentFormat tournamentFormat = TournamentFormat.LEAGUE;
        String tournamentSport = null;
        if (asTournament) {
            if (context.format() != null && !context.format().isBlank()) {
                try {
                    tournamentFormat = TournamentFormat.valueOf(
                            context.format().trim().toUpperCase(Locale.ROOT));
                } catch (IllegalArgumentException exception) {
                    throw new IllegalArgumentException("Formato histórico inválido");
                }
            }
            if (tournamentFormat != TournamentFormat.LEAGUE
                    && tournamentFormat != TournamentFormat.KNOCKOUT) {
                throw new IllegalArgumentException("O Excel histórico suporta liga ou eliminatórias");
            }
            tournamentSport = context.sport() == null ? null : context.sport().trim();
            if (tournamentSport == null || tournamentSport.isBlank()
                    || tournamentSport.length() > 100) {
                throw new IllegalArgumentException("Indica a modalidade do torneio histórico");
            }
        }

        // This repeats OWNER/ADMIN checks and all source-data validation at
        // confirmation time; no data is trusted from a previous browser preview.
        CommunityHistoricalMatchPreviewResponse plan = previewService.preview(
                communityId, username, file, mapping);

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("Comunidade não encontrada"));
        CommunityHistoricalMatchArchive archive = archiveRepository.save(
                CommunityHistoricalMatchArchive.builder()
                        .community(community)
                        .title(context.title().trim())
                        .entryType(type)
                        .sourceFilename(file.getOriginalFilename())
                        .createdByUsername(username)
                        .createdAt(Instant.now())
                        .build());

        if (asTournament && plan.rows().isEmpty()) {
            throw new IllegalArgumentException("Não existem partidas válidas para criar um torneio");
        }

        List<CommunityHistoricalMatchArchiveRow> rows = plan.rows().stream()
                .map(row -> CommunityHistoricalMatchArchiveRow.builder()
                        .archive(archive)
                        .sourceRow(row.sourceRow())
                        .home(row.home())
                        .away(row.away())
                        .homeScore(row.homeScore())
                        .awayScore(row.awayScore())
                        .sourceDate(row.sourceDate())
                        .sourceRound(row.sourceRound())
                        .potentialDuplicate(row.potentialDuplicate())
                        .build()).toList();
        rowRepository.saveAll(rows);
        Long tournamentId = null;
        if (asTournament) {
            // A historical entry is a completed read-only edition. Source rows
            // remain owned by the archive so names never impersonate profiles.
            CommunityTournament tournament = tournamentRepository.save(
                    CommunityTournament.builder()
                            .community(community)
                            .name(context.title().trim())
                            .sport(tournamentSport)
                            .entryType(TournamentEntryType.valueOf(type))
                            .format(tournamentFormat)
                            .status(TournamentStatus.COMPLETED)
                            .winPoints(3).drawPoints(1).lossPoints(0)
                            .historicalArchiveId(archive.getId())
                            .createdAt(Instant.now())
                            .completedAt(Instant.now())
                            .build());
            tournamentId = tournament.getId();
        }
        return new CommunityHistoricalMatchArchiveResponse(
                archive.getId(), archive.getTitle(), archive.getEntryType(),
                archive.getSourceFilename(), archive.getCreatedAt(), plan.rows(), tournamentId);
    }

    @Transactional(readOnly = true)
    public List<CommunityHistoricalMatchArchiveSummaryResponse> list(
            Long communityId, String username) {
        requireMember(communityId, username);
        return archiveRepository.findByCommunityIdOrderByCreatedAtDescIdDesc(
                        communityId, PageRequest.of(0, 50))
                .stream().map(archive -> new CommunityHistoricalMatchArchiveSummaryResponse(
                        archive.getId(), archive.getTitle(), archive.getEntryType(),
                        archive.getSourceFilename(), archive.getCreatedAt()))
                .toList();
    }

    @Transactional(readOnly = true)
    public CommunityHistoricalMatchArchiveResponse detail(
            Long communityId, Long archiveId, String username) {
        requireMember(communityId, username);
        CommunityHistoricalMatchArchive archive = archiveRepository.findByIdAndCommunityId(
                        archiveId, communityId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Histórico de partidas não encontrado"));
        List<CommunityHistoricalMatchPreviewResponse.MatchRow> rows =
                rowRepository.findByArchiveIdOrderBySourceRowAscIdAsc(archiveId)
                        .stream().map(row -> new CommunityHistoricalMatchPreviewResponse.MatchRow(
                                row.getSourceRow(), row.getHome(), row.getAway(),
                                row.getHomeScore(), row.getAwayScore(),
                                row.getSourceDate(), row.getSourceRound(),
                                row.isPotentialDuplicate())).toList();
        return toResponse(archive, rows);
    }

    private CommunityHistoricalMatchArchiveResponse toResponse(
            CommunityHistoricalMatchArchive archive,
            List<CommunityHistoricalMatchPreviewResponse.MatchRow> rows) {
        return new CommunityHistoricalMatchArchiveResponse(
                archive.getId(), archive.getTitle(), archive.getEntryType(),
                archive.getSourceFilename(), archive.getCreatedAt(), rows);
    }

    private void requireMember(Long communityId, String username) {
        Profile actor = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new AccessDeniedException("Utilizador não encontrado"));
        memberRepository.findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException(
                        "Não pertences a esta comunidade"));
    }
}
