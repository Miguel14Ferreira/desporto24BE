package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunitySeason;
import com.example.desporto24.model.dto.community.CommunityImportContextRequest;
import com.example.desporto24.model.dto.community.CommunityImportMappingRequest;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRoundRepository;
import com.example.desporto24.repository.community.CommunitySeasonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityImportNormalizedDateTest {
    @Mock private ProfileRepository profileRepository;
    @Mock private CommunityMemberRepository memberRepository;
    @Mock private CommunitySeasonRepository seasonRepository;
    @Mock private CommunityRoundRepository roundRepository;
    @InjectMocks private CommunityImportService service;

    private void authorize() {
        Profile actor = new Profile();
        actor.setId(5L);
        when(profileRepository.findUserByUsernameIgnoreCase("organizer")).thenReturn(Optional.of(actor));
        CommunityMember member = new CommunityMember();
        member.setRole(CommunityRole.OWNER);
        when(memberRepository.findByCommunityIdAndProfileId(10L, 5L)).thenReturn(Optional.of(member));

        Community community = new Community();
        community.setId(10L);
        CommunitySeason season = new CommunitySeason();
        season.setId(30L);
        season.setCommunity(community);
        when(seasonRepository.findById(30L)).thenReturn(Optional.of(season));
    }

    @Test
    void blocksUndatedPlayersBeforeAnyHistoricalRoundCanBeSaved() {
        authorize();
        var file = new MockMultipartFile("file", "historic.csv", "text/csv",
                ("Date,Player,Result\n"
                + "01/10/2026,Ana,WIN\n"
                + ",Bruno,LOSS\n").getBytes(StandardCharsets.UTF_8));
        var mapping = new CommunityImportMappingRequest("Dados", List.of(
                new CommunityImportMappingRequest.ColumnMapping(0, "DATE"),
                new CommunityImportMappingRequest.ColumnMapping(1, "PLAYER"),
                new CommunityImportMappingRequest.ColumnMapping(2, "RESULT")));
        var context = new CommunityImportContextRequest(30L, null);

        var error = assertThrows(IllegalArgumentException.class,
                () -> service.confirmRounds(10L, "organizer", file, mapping, context));

        assertTrue(error.getMessage().contains("1 participações sem data"));
        verifyNoInteractions(roundRepository);
    }

    @Test
    void finalPlanDoesNotTreatTwoFormatsOfSameDateAsDifferentRounds() {
        authorize();
        var file = new MockMultipartFile("file", "jornadas.csv", "text/csv",
                ("Date,Player,Result\n"
                + "01/10/2026,Ana,WIN\n"
                + "2026-10-01,Bruno,LOSS\n"
                + "02/10/2026,Carlos,DRAW\n").getBytes(StandardCharsets.UTF_8));
        var mapping = new CommunityImportMappingRequest("Dados", List.of(
                new CommunityImportMappingRequest.ColumnMapping(0, "DATE"),
                new CommunityImportMappingRequest.ColumnMapping(1, "PLAYER"),
                new CommunityImportMappingRequest.ColumnMapping(2, "RESULT")));
        var context = new CommunityImportContextRequest(30L, null);

        var plan = service.finalRoundsPlan(10L, "organizer", file, mapping, context);

        assertEquals(3, plan.distinctRounds());
        assertEquals(1, plan.duplicateRounds());
        assertEquals(List.of(false, true, false),
                plan.rounds().stream().map(r -> r.duplicate()).toList());
        assertEquals("2026-10-01", plan.rounds().get(0).parsedDate());
        assertEquals("2026-10-01", plan.rounds().get(1).parsedDate());
        verify(roundRepository, times(1)).existsByCommunityIdAndSeasonIdAndPlayedAt(
                10L, 30L, Instant.parse("2026-10-01T00:00:00Z"));
    }
}
