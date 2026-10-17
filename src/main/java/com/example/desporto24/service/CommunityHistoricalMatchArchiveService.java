package com.example.desporto24.service;

import com.example.desporto24.model.Profile;
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
        return toResponse(archive, plan.rows());
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
