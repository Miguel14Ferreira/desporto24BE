package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityImportCsvTest {
    @Mock private ProfileRepository profileRepository;
    @Mock private CommunityMemberRepository memberRepository;
    @InjectMocks private CommunityImportService service;

    private void allowImport() {
        Profile profile = new Profile();
        profile.setId(7L);
        when(profileRepository.findUserByUsernameIgnoreCase("organizer"))
                .thenReturn(Optional.of(profile));
        CommunityMember membership = new CommunityMember();
        membership.setRole(CommunityRole.ADMIN);
        when(memberRepository.findByCommunityIdAndProfileId(9L, 7L))
                .thenReturn(Optional.of(membership));
    }

    private MockMultipartFile csv(String content) {
        return new MockMultipartFile("file", "historico.csv", "text/csv",
                content.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void previewsSemicolonSeparatedPortugueseExport() {
        allowImport();
        var preview = service.preview(9L, "organizer",
                csv("Jogador;Data;Golos;Assistências\nAna;02/10/2026;2;1\nBruno;02/10/2026;1;0"));
        assertEquals("historico.csv", preview.fileName());
        assertEquals(1, preview.sheets().size());
        var sheet = preview.sheets().getFirst();
        assertEquals("CSV", sheet.name());
        assertEquals("TABULAR", sheet.structure());
        assertEquals("Jogador", sheet.headers().getFirst());
        assertEquals("Ana", sheet.sampleRows().getFirst().getFirst());
    }

    @Test
    void previewsCommaSeparatedQuotedValuesWithoutSplittingNames() {
        allowImport();
        var preview = service.preview(9L, "organizer",
                csv("Player,Goals\n\"Silva, João\",3"));
        assertEquals("Silva, João", preview.sheets().getFirst().sampleRows().getFirst().getFirst());
    }

    @Test
    void rejectsOversizedAndUnsupportedFiles() {
        allowImport();
        var oversized = new MockMultipartFile("file", "huge.csv", "text/csv", new byte[5 * 1024 * 1024 + 1]);
        assertThrows(IllegalArgumentException.class, () -> service.preview(9L, "organizer", oversized));
        var txt = new MockMultipartFile("file", "notes.txt", "text/plain", "something".getBytes(StandardCharsets.UTF_8));
        assertThrows(IllegalArgumentException.class, () -> service.preview(9L, "organizer", txt));
    }
}
