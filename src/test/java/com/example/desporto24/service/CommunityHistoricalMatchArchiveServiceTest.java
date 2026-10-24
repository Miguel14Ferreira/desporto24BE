package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.*;
import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityHistoricalMatchArchiveServiceTest {
    @Mock private CommunityHistoricalMatchPreviewService previewService;
    @Mock private ProfileRepository profiles;
    @Mock private CommunityMemberRepository members;
    @Mock private CommunityRepository communities;
    @Mock private CommunityHistoricalMatchArchiveRepository archives;
    @Mock private CommunityTournamentRepository tournaments;
    @Mock private CommunityHistoricalMatchArchiveRowRepository rows;
    @InjectMocks private CommunityHistoricalMatchArchiveService service;

    private final CommunityImportMappingRequest mapping = new CommunityImportMappingRequest(
            "Dados", List.of(new CommunityImportMappingRequest.ColumnMapping(0, "HOME")));
    private final MockMultipartFile file = new MockMultipartFile(
            "file", "antigos.csv", "text/csv", "data".getBytes(StandardCharsets.UTF_8));

    private void authorize() {
        Profile actor = new Profile();
        actor.setId(8L);
        when(profiles.findUserByUsernameIgnoreCase("owner"))
                .thenReturn(Optional.of(actor));
        CommunityMember member = new CommunityMember();
        member.setRole(CommunityRole.MEMBER);
        when(members.findByCommunityIdAndProfileId(7L, 8L))
                .thenReturn(Optional.of(member));
    }

    @Test
    void savesEveryMatchIncludingPossibleRepeatsWithoutCreatingLiveFixtures() {
        var source = List.of(
                new CommunityHistoricalMatchPreviewResponse.MatchRow(
                        2, "Águias", "Lobos", 2, 1, "01/10/2020", "J1", false),
                new CommunityHistoricalMatchPreviewResponse.MatchRow(
                        3, "Lobos", "Águias", 0, 0, "2020-10-01", "J1", true));
        when(previewService.preview(7L, "owner", file, mapping))
                .thenReturn(new CommunityHistoricalMatchPreviewResponse(
                        "Dados", 2, 2, 1, List.of("Possível repetição"), source));
        Community community = new Community();
        community.setId(7L);
        when(communities.findById(7L)).thenReturn(Optional.of(community));
        when(archives.save(any())).thenAnswer(invocation -> {
            CommunityHistoricalMatchArchive a = invocation.getArgument(0);
            a.setId(9L);
            return a;
        });

        var result = service.confirm(7L, "owner", file, mapping,
                new CommunityHistoricalMatchArchiveConfirmRequest("Taça 2020", "TEAM"));

        assertEquals(9L, result.id());
        assertEquals("TEAM", result.entryType());
        assertEquals(2, result.rows().size());
        assertEquals("01/10/2020", result.rows().getFirst().sourceDate());
        assertTrue(result.rows().get(1).potentialDuplicate());
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<CommunityHistoricalMatchArchiveRow>> captor =
                ArgumentCaptor.forClass(List.class);
        verify(rows).saveAll(captor.capture());
        assertEquals(2, captor.getValue().size());
        assertTrue(captor.getValue().get(1).isPotentialDuplicate());
        assertEquals("2020-10-01", captor.getValue().get(1).getSourceDate());
        verifyNoInteractions(profiles, members);
    }

    @Test
    void createsCompletedHistoricalTournamentWithoutInventingRealPlayersOrFixtures() {
        var source = List.of(new CommunityHistoricalMatchPreviewResponse.MatchRow(
                2, "CNG A", "CNG B", 3, 1, "2024-05-10", "J1", false));
        when(previewService.preview(7L, "owner", file, mapping))
                .thenReturn(new CommunityHistoricalMatchPreviewResponse(
                        "Dados", 1, 1, 0, List.of(), source));
        Community community = new Community();
        community.setId(7L);
        when(communities.findById(7L)).thenReturn(Optional.of(community));
        when(archives.save(any())).thenAnswer(inv -> {
            CommunityHistoricalMatchArchive item = inv.getArgument(0);
            item.setId(91L);
            return item;
        });
        when(tournaments.save(any())).thenAnswer(inv -> {
            CommunityTournament tournament = inv.getArgument(0);
            tournament.setId(51L);
            return tournament;
        });

        var result = service.confirm(7L, "owner", file, mapping,
                new CommunityHistoricalMatchArchiveConfirmRequest(
                        "Torneio 2024", "TEAM", true, "Futebol", "LEAGUE"));

        assertEquals(51L, result.tournamentId());
        assertEquals(1, result.rows().size());
        verify(tournaments).save(argThat(t -> t.getCommunity() == community
                && t.getHistoricalArchiveId() == 91L
                && t.getStatus() == com.example.desporto24.enumeration.TournamentStatus.COMPLETED
                && t.getEntryType() == com.example.desporto24.enumeration.TournamentEntryType.TEAM
                && "Futebol".equals(t.getSport())));
        verify(rows).saveAll(any());
        verifyNoInteractions(profiles, members);
    }

    @Test
    void invalidHistoricalTournamentSettingsNeverWriteArchiveOrTournament() {
        assertThrows(IllegalArgumentException.class, () -> service.confirm(7L, "owner",
                file, mapping, new CommunityHistoricalMatchArchiveConfirmRequest(
                        "Torneio 2024", "TEAM", true, "", "LEAGUE")));
        assertThrows(IllegalArgumentException.class, () -> service.confirm(7L, "owner",
                file, mapping, new CommunityHistoricalMatchArchiveConfirmRequest(
                        "Torneio 2024", "TEAM", true, "Futebol", "GROUP_KNOCKOUT")));
        verifyNoInteractions(previewService, archives, rows, tournaments);
    }

    @Test
    void invalidContextNeverCallsPreviewOrRepositories() {
        assertThrows(IllegalArgumentException.class, () ->
                service.confirm(7L, "owner", file, mapping,
                        new CommunityHistoricalMatchArchiveConfirmRequest("A", "TEAM")));
        assertThrows(IllegalArgumentException.class, () ->
                service.confirm(7L, "owner", file, mapping,
                        new CommunityHistoricalMatchArchiveConfirmRequest("Torneio", "UNKNOWN")));
        verifyNoInteractions(previewService, archives, rows);
    }

    @Test
    void readsOnlyAnArchiveBelongingToTheRequestedCommunity() {
        authorize();
        var archive = CommunityHistoricalMatchArchive.builder()
                .id(9L).title("Época 2020").entryType("INDIVIDUAL")
                .sourceFilename("antigos.csv")
                .createdAt(Instant.parse("2020-10-01T00:00:00Z")).build();
        when(archives.findByIdAndCommunityId(9L, 7L))
                .thenReturn(Optional.of(archive));
        when(rows.findByArchiveIdOrderBySourceRowAscIdAsc(9L)).thenReturn(List.of(
                CommunityHistoricalMatchArchiveRow.builder()
                        .sourceRow(2).home("Ana").away("João").homeScore(1)
                        .awayScore(0).sourceDate("2020-10-01")
                        .sourceRound("Final").build()));

        var result = service.detail(7L, 9L, "owner");
        assertEquals(1, result.rows().size());
        assertEquals("Final", result.rows().getFirst().sourceRound());
        assertEquals("Ana", result.rows().getFirst().home());

        assertThrows(ResponseStatusException.class,
                () -> service.detail(7L, 999L, "owner"));
        verify(archives).findByIdAndCommunityId(999L, 7L);
    }

    @Test
    void communityMemberCanListOnlyTheirOwnCommunityArchives() {
        authorize();
        var archive = CommunityHistoricalMatchArchive.builder()
                .id(9L).title("Época 2020").entryType("TEAM")
                .sourceFilename("antigos.csv").createdAt(Instant.now()).build();
        when(archives.findByCommunityIdOrderByCreatedAtDescIdDesc(eq(7L), any(Pageable.class)))
                .thenReturn(List.of(archive));
        var summaries = service.list(7L, "owner");
        assertEquals(1, summaries.size());
        assertEquals("Época 2020", summaries.getFirst().title());
    }

    @Test
    void nonMemberCannotReadAnyHistoricalMatchArchive() {
        Profile actor = new Profile();
        actor.setId(8L);
        when(profiles.findUserByUsernameIgnoreCase("owner"))
                .thenReturn(Optional.of(actor));
        assertThrows(AccessDeniedException.class, () -> service.list(7L, "owner"));
        verifyNoInteractions(archives, rows);
    }
}
