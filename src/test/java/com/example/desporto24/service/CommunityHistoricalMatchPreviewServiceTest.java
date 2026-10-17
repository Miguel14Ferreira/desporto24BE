package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.dto.community.CommunityImportMappingRequest;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityHistoricalMatchPreviewServiceTest {
    @Mock private ProfileRepository profiles;
    @Mock private CommunityMemberRepository members;
    @InjectMocks private CommunityHistoricalMatchPreviewService service;

    private void authorize(CommunityRole role) {
        Profile actor = new Profile();
        actor.setId(8L);
        when(profiles.findUserByUsernameIgnoreCase("organizer")).thenReturn(Optional.of(actor));
        CommunityMember member = new CommunityMember();
        member.setRole(role);
        when(members.findByCommunityIdAndProfileId(7L, 8L)).thenReturn(Optional.of(member));
    }

    private MockMultipartFile file(String contents) {
        return new MockMultipartFile("file", "jogos.csv", "text/csv",
                contents.getBytes(StandardCharsets.UTF_8));
    }

    private CommunityImportMappingRequest combinedMapping() {
        return new CommunityImportMappingRequest("Dados", List.of(
                new CommunityImportMappingRequest.ColumnMapping(0, "HOME"),
                new CommunityImportMappingRequest.ColumnMapping(1, "AWAY"),
                new CommunityImportMappingRequest.ColumnMapping(2, "SCORE"),
                new CommunityImportMappingRequest.ColumnMapping(3, "DATE"),
                new CommunityImportMappingRequest.ColumnMapping(4, "ROUND")
        ));
    }

    @Test
    void previewsOldMatchesAndWarnsAboutPotentialReplaysWithoutDroppingAny() {
        authorize(CommunityRole.OWNER);
        var result = service.preview(7L, "organizer",
                file("Casa;Fora;Resultado;Data;Jornada\n"
                   + "Águias;Lobos;2-1;2020-10-01;1\n"
                   + "Lobos;AGUIAS;0:0;2020-10-01;1\n"
                   + "Lobos;Águias;3x1;2020-10-08;2\n"),
                combinedMapping());
        assertEquals(3, result.sourceRows());
        assertEquals(3, result.acceptedRows());
        assertEquals(1, result.potentialDuplicates());
        assertEquals(3, result.rows().size());
        assertEquals(2, result.rows().getFirst().homeScore());
        assertEquals(1, result.rows().getFirst().awayScore());
        assertEquals("2020-10-08", result.rows().get(2).sourceDate());
        assertTrue(result.rows().get(1).potentialDuplicate());
        assertTrue(result.warnings().getFirst().contains("Linha 3"));
    }

    @Test
    void acceptsTwoNumericScoreColumnsAndPreservesZero() {
        authorize(CommunityRole.ADMIN);
        var mapping = new CommunityImportMappingRequest("Dados", List.of(
                new CommunityImportMappingRequest.ColumnMapping(0, "HOME"),
                new CommunityImportMappingRequest.ColumnMapping(1, "AWAY"),
                new CommunityImportMappingRequest.ColumnMapping(2, "HOME_SCORE"),
                new CommunityImportMappingRequest.ColumnMapping(3, "AWAY_SCORE")
        ));
        var parsed = service.preview(7L, "organizer",
                file("Equipa A;Equipa B;Golos A;Golos B\nLobos;Águias;0;3\n"), mapping);
        assertEquals(1, parsed.acceptedRows());
        assertEquals(0, parsed.rows().getFirst().homeScore());
        assertEquals(3, parsed.rows().getFirst().awayScore());
    }

    @Test
    void rejectsMalformedResultsWithoutWritingAnything() {
        authorize(CommunityRole.OWNER);
        var failure = assertThrows(IllegalArgumentException.class, () ->
                service.preview(7L, "organizer",
                    file("Casa;Fora;Resultado;Data;Jornada\nÁguias;Lobos;2-;2020-10-01;1\n"),
                    combinedMapping()));
        assertTrue(failure.getMessage().contains("Linha 2"));
    }

    @Test
    void rejectsMissingScoreMappingAndSameOpponent() {
        authorize(CommunityRole.OWNER);
        var incomplete = new CommunityImportMappingRequest("Dados", List.of(
                new CommunityImportMappingRequest.ColumnMapping(0, "HOME"),
                new CommunityImportMappingRequest.ColumnMapping(1, "AWAY")
        ));
        assertThrows(IllegalArgumentException.class, () ->
                service.preview(7L, "organizer",
                    file("Casa;Fora\nÁguias;Lobos\n"), incomplete));
        assertThrows(IllegalArgumentException.class, () ->
                service.preview(7L, "organizer",
                    file("Casa;Fora;Resultado;Data;Jornada\nÁguias;AGUIAS;2-0;2020-10-01;1\n"),
                    combinedMapping()));
    }

    @Test
    void ordinaryMemberCannotPreviewTournamentHistory() {
        authorize(CommunityRole.MEMBER);
        assertThrows(AccessDeniedException.class, () ->
                service.preview(7L, "organizer",
                    file("Casa;Fora;Resultado;Data;Jornada\nÁguias;Lobos;2-0;2020-10-01;1\n"),
                    combinedMapping()));
    }
}
