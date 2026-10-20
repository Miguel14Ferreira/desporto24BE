package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.*;
import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityStandingsArchiveServiceTest {
    @Mock private ProfileRepository profiles;
    @Mock private CommunityMemberRepository members;
    @Mock private CommunityRepository communities;
    @Mock private CommunitySeasonRepository seasons;
    @Mock private CommunityStandingsArchiveRepository archives;
    @Mock private CommunityStandingsArchiveRowRepository rows;
    @InjectMocks private CommunityStandingsArchiveService service;

    private final CommunityImportMappingRequest mapping = new CommunityImportMappingRequest(
            "Dados", List.of(
                    new CommunityImportMappingRequest.ColumnMapping(0, "POSITION"),
                    new CommunityImportMappingRequest.ColumnMapping(1, "TEAM"),
                    new CommunityImportMappingRequest.ColumnMapping(2, "PLAYED"),
                    new CommunityImportMappingRequest.ColumnMapping(3, "WINS"),
                    new CommunityImportMappingRequest.ColumnMapping(4, "POINTS")
            )
    );

    private void auth(CommunityRole role) {
        Profile actor = new Profile();
        actor.setId(8L);
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(actor));
        CommunityMember membership = new CommunityMember();
        membership.setRole(role);
        when(members.findByCommunityIdAndProfileId(7L, 8L)).thenReturn(Optional.of(membership));
    }

    private MockMultipartFile file(String text) {
        return new MockMultipartFile("file", "classificacao.csv", "text/csv",
                text.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void previewsFinalStandingsWithoutFabricatingResultsOrProfiles() {
        auth(CommunityRole.OWNER);
        var file = file("Pos;Equipa;J;V;Pts\n"
                + "1;Águias;10;8;24\n"
                + "2;Lobos;10;6;18\n"
                + "3;AGUIAS;10;1;5\n");
        var preview = service.preview(7L, "owner", file, mapping);
        assertEquals(3, preview.sourceRows());
        assertEquals(2, preview.acceptedRows());
        assertEquals(1, preview.duplicateRows());
        assertEquals(24, preview.rows().getFirst().points());
        assertEquals(8, preview.rows().getFirst().wins());
        assertEquals("Águias", preview.rows().getFirst().name());
        assertTrue(preview.warnings().getFirst().contains("primeira ocorrência"));
        verifyNoInteractions(archives, rows, communities);
    }

    @Test
    void preservesOriginalPointsAndPersistsReadOnlyArchiveWithoutMatches() {
        auth(CommunityRole.OWNER);
        var file = file("Pos;Equipa;J;V;Pts\n"
                + "1;Águias;10;8;19\n"
                + "2;Lobos;10;6;17\n");
        var community = new Community();
        community.setId(7L);
        when(communities.findById(7L)).thenReturn(Optional.of(community));
        when(archives.save(any())).thenAnswer(invocation -> {
            CommunityStandingsArchive archive = invocation.getArgument(0);
            archive.setId(42L);
            return archive;
        });

        var archive = service.confirm(7L, "owner", file, mapping,
                new CommunityStandingsArchiveConfirmRequest("Campeonato 2020", "TEAM", null));
        assertEquals(42L, archive.id());
        assertEquals("TEAM", archive.entryType());
        assertNull(archive.seasonId());
        assertEquals(List.of(19, 17), archive.rows().stream().map(
                CommunityStandingsArchivePreviewResponse.StandingRow::points).toList());

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<CommunityStandingsArchiveRow>> captor = ArgumentCaptor.forClass(List.class);
        verify(rows).saveAll(captor.capture());
        assertEquals(2, captor.getValue().size());
        assertEquals(19, captor.getValue().getFirst().getPoints());
        assertEquals("aguias", captor.getValue().getFirst().getNormalizedName());
        verifyNoInteractions(seasons);
    }

    @Test
    void rejectsMalformedTotalsWithoutWritingAnyArchive() {
        auth(CommunityRole.OWNER);
        assertThrows(IllegalArgumentException.class, () -> service.confirm(7L, "owner",
                file("Pos;Equipa;J;V;Pts\n1;Águias;10;8;20.5\n"), mapping,
                new CommunityStandingsArchiveConfirmRequest("Tabela final", "TEAM", null)));
        verifyNoInteractions(archives, rows);
    }

    @Test
    void membersCannotImportAndPrivateArchivesRequireMembershipToRead() {
        auth(CommunityRole.MEMBER);
        assertThrows(AccessDeniedException.class,
                () -> service.preview(7L, "owner", file("Pos;Equipa;J;V;Pts\n1;Lobos;4;3;10\n"), mapping));
        verifyNoInteractions(archives, rows);
        assertTrue(service.list(7L, "owner").isEmpty());
    }

    @Test
    void refusesSeasonFromAnotherCommunity() {
        auth(CommunityRole.OWNER);
        Community community = new Community();
        community.setId(7L);
        when(communities.findById(7L)).thenReturn(Optional.of(community));
        Community other = new Community();
        other.setId(12L);
        CommunitySeason season = new CommunitySeason();
        season.setId(90L);
        season.setCommunity(other);
        when(seasons.findById(90L)).thenReturn(Optional.of(season));
        assertThrows(IllegalArgumentException.class,
                () -> service.confirm(7L, "owner",
                        file("Pos;Equipa;J;V;Pts\n1;Lobos;4;3;10\n"), mapping,
                        new CommunityStandingsArchiveConfirmRequest("Época 2020", "TEAM", 90L)));
        verifyNoInteractions(archives, rows);
    }
}
