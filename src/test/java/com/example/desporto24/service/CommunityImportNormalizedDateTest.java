package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunitySeason;
import com.example.desporto24.model.community.CommunityPlayer;
import com.example.desporto24.model.community.CommunityRoundEntry;
import com.example.desporto24.model.community.CommunityRound;
import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityImportContextRequest;
import com.example.desporto24.model.dto.community.CommunityImportMappingRequest;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRoundRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityPlayerRepository;
import com.example.desporto24.repository.community.CommunityRoundEntryRepository;
import com.example.desporto24.repository.community.CommunitySeasonRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
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
    @Mock private CommunityRepository communityRepository;
    @Mock private CommunityPlayerRepository communityPlayerRepository;
    @Mock private CommunityRoundEntryRepository roundEntryRepository;
    @Mock private EntityManager entityManager;
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
    void ignoresRejectedImportMetricsInBothPreviewAndSavedPlayerIdentities() {
        Profile actor = new Profile();
        actor.setId(5L);
        when(profileRepository.findUserByUsernameIgnoreCase("organizer"))
                .thenReturn(Optional.of(actor));
        CommunityMember member = new CommunityMember();
        member.setRole(CommunityRole.OWNER);
        when(memberRepository.findByCommunityIdAndProfileId(10L, 5L))
                .thenReturn(Optional.of(member));

        var file = new MockMultipartFile("file", "metricas.csv", "text/csv",
                ("Date,Player,Result,Goals,WIN,POINTS\n"
                + "01/10/2026,Ana,WIN,2.0,1,3\n"
                + "01/10/2026,Bruno,LOSS,-2,0,0\n"
                + "01/10/2026,Carlos,WIN,1.5,1,3\n"
                + "01/10/2026,Dina,WIN,NaN,1,3\n"
                + "01/10/2026,Ema,WIN,Infinity,1,3\n"
                + "01/10/2026,Filipe,WIN,2,2,3\n"
                + "01/10/2026,Grace,WIN,1000001,1,3\n"
                + "01/10/2026,Hugo,WIN,2,1,abc\n"
                + "02/10/2026,Nuno,DRAW,0,0,-1\n")
                .getBytes(StandardCharsets.UTF_8));
        var mapping = new CommunityImportMappingRequest("Dados", List.of(
                new CommunityImportMappingRequest.ColumnMapping(0, "DATE"),
                new CommunityImportMappingRequest.ColumnMapping(1, "PLAYER"),
                new CommunityImportMappingRequest.ColumnMapping(2, "RESULT"),
                new CommunityImportMappingRequest.ColumnMapping(3, "GOALS"),
                new CommunityImportMappingRequest.ColumnMapping(4, "WIN"),
                new CommunityImportMappingRequest.ColumnMapping(5, "POINTS")));

        var validation = service.validateMapping(10L, "organizer", file, mapping);
        assertTrue(validation.valid());
        assertEquals(2, validation.validRows());
        assertEquals(7, validation.warnings().size());
        var preview = service.normalizedPreview(10L, "organizer", file, mapping);
        assertEquals(2, preview.acceptedRows());
        assertEquals(7, preview.skippedRows());
        assertTrue(preview.skipped().stream()
                .allMatch(row -> row.reason().equals("INVALID_NUMERIC_VALUE")));

        var plan = service.importPlan(10L, "organizer", file, mapping);
        assertEquals(2, plan.acceptedRows());
        assertEquals(2, plan.distinctPlayers());
        assertEquals(List.of("Ana", "Nuno"),
                plan.players().stream().map(player -> player.displayName()).toList());

        Community community = new Community();
        community.setId(10L);
        when(communityRepository.findById(10L)).thenReturn(Optional.of(community));
        var saved = service.confirmPlayers(10L, "organizer", file, mapping);
        assertEquals(2, saved.createdPlayers());
        var captor = org.mockito.ArgumentCaptor.forClass(CommunityPlayer.class);
        verify(communityPlayerRepository, times(2)).save(captor.capture());
        assertEquals(List.of("Ana", "Nuno"),
                captor.getAllValues().stream().map(CommunityPlayer::getDisplayName).toList());
    }

    @Test
    void rejectsContradictoryResultFlagsInsteadOfSilentlyChoosingAWinner() {
        Profile actor = new Profile();
        actor.setId(5L);
        when(profileRepository.findUserByUsernameIgnoreCase("organizer"))
                .thenReturn(Optional.of(actor));
        CommunityMember member = new CommunityMember();
        member.setRole(CommunityRole.OWNER);
        when(memberRepository.findByCommunityIdAndProfileId(10L, 5L))
                .thenReturn(Optional.of(member));

        var file = new MockMultipartFile("file", "resultados.csv", "text/csv",
                ("Date,Player,Result,WIN,DRAW,LOSS\n"
                + "01/10/2026,Ana,WIN,1,0,0\n"
                + "01/10/2026,Bruno,LOSS,1,0,1\n"
                + "01/10/2026,Carla,DRAW,1,0,0\n"
                + "01/10/2026,Dinis,DRAW,0,1,0\n")
                .getBytes(StandardCharsets.UTF_8));
        var mapping = new CommunityImportMappingRequest("Dados", List.of(
                new CommunityImportMappingRequest.ColumnMapping(0, "DATE"),
                new CommunityImportMappingRequest.ColumnMapping(1, "PLAYER"),
                new CommunityImportMappingRequest.ColumnMapping(2, "RESULT"),
                new CommunityImportMappingRequest.ColumnMapping(3, "WIN"),
                new CommunityImportMappingRequest.ColumnMapping(4, "DRAW"),
                new CommunityImportMappingRequest.ColumnMapping(5, "LOSS")));

        var validation = service.validateMapping(10L, "organizer", file, mapping);
        assertEquals(2, validation.validRows());
        assertTrue(validation.warnings().stream()
                .anyMatch(warning -> warning.contains("contraditórios")));
        var preview = service.normalizedPreview(10L, "organizer", file, mapping);
        assertEquals(2, preview.acceptedRows());
        assertEquals(2, preview.skippedRows());
        assertTrue(preview.skipped().stream()
                .allMatch(row -> row.reason().equals("INVALID_NUMERIC_VALUE")));

        var plan = service.importPlan(10L, "organizer", file, mapping);
        assertEquals(List.of("Ana", "Dinis"),
                plan.players().stream().map(player -> player.displayName()).toList());
    }

    @Test
    void mergesEquivalentDateFormatsAndRetainsEveryDistinctPlayer() {
        authorize();
        var file = new MockMultipartFile("file", "jornadas.csv", "text/csv",
                ("Date,Player,Result\n"
                + "01/10/2026,Ana,WIN\n"
                + "2026-10-01,ÁNA,LOSS\n"
                + "2026-10-01,Bruno,LOSS\n"
                + "02/10/2026,Carlos,DRAW\n").getBytes(StandardCharsets.UTF_8));
        var mapping = new CommunityImportMappingRequest("Dados", List.of(
                new CommunityImportMappingRequest.ColumnMapping(0, "DATE"),
                new CommunityImportMappingRequest.ColumnMapping(1, "PLAYER"),
                new CommunityImportMappingRequest.ColumnMapping(2, "RESULT")));
        var context = new CommunityImportContextRequest(30L, null);

        var validation = service.validateMapping(10L, "organizer", file, mapping);
        assertEquals(3, validation.validRows());
        assertTrue(validation.warnings().stream().anyMatch(w -> w.contains("Linha 3")));
        var preview = service.normalizedPreview(10L, "organizer", file, mapping);
        assertEquals(3, preview.acceptedRows());
        assertEquals("PLAYER_DUPLICATE_IN_ROUND", preview.skipped().getFirst().reason());

        var plan = service.finalRoundsPlan(10L, "organizer", file, mapping, context);
        assertEquals(2, plan.distinctRounds());
        assertEquals(0, plan.duplicateRounds());
        assertEquals(3, plan.acceptedRows());
        assertEquals("2026-10-01", plan.rounds().getFirst().parsedDate());
        assertEquals(2, plan.rounds().getFirst().players());
        assertEquals(1, plan.rounds().getFirst().wins());
        assertEquals(1, plan.rounds().getFirst().losses());

        Community community = new Community();
        community.setId(10L);
        when(communityRepository.findById(10L)).thenReturn(Optional.of(community));
        when(communityPlayerRepository.findByCommunityIdAndNormalizedName(eq(10L), anyString()))
                .thenAnswer(invocation -> {
                    String normalized = invocation.getArgument(1);
                    CommunityPlayer player = new CommunityPlayer();
                    player.setNormalizedName(normalized);
                    player.setDisplayName(normalized);
                    return Optional.of(player);
                });
        when(roundRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.confirmRounds(10L, "organizer", file, mapping, context);
        assertEquals(2, result.createdRounds());
        assertEquals(3, result.createdEntries());
        verify(roundRepository, times(2)).save(any());
        var entryCaptor = org.mockito.ArgumentCaptor.forClass(CommunityRoundEntry.class);
        verify(roundEntryRepository, times(3)).save(entryCaptor.capture());
        assertEquals(List.of(CommunityMatchResult.WIN, CommunityMatchResult.LOSS, CommunityMatchResult.DRAW),
                entryCaptor.getAllValues().stream().map(CommunityRoundEntry::getResult).toList());
    }

    @Test
    void mergesYearlessDatesWithDatedRowsWhenDefaultYearIsChosen() {
        authorize();
        var file = new MockMultipartFile("file", "jornadas.csv", "text/csv",
                ("Date,Player,Result\n"
                + "01/10,Ana,WIN\n"
                + "2026-10-01,Bruno,LOSS\n"
                + "01.10,ÁNA,DRAW\n").getBytes(StandardCharsets.UTF_8));
        var mapping = new CommunityImportMappingRequest("Dados", List.of(
                new CommunityImportMappingRequest.ColumnMapping(0, "DATE"),
                new CommunityImportMappingRequest.ColumnMapping(1, "PLAYER"),
                new CommunityImportMappingRequest.ColumnMapping(2, "RESULT")));
        var plan = service.finalRoundsPlan(10L, "organizer", file, mapping,
                new CommunityImportContextRequest(30L, 2026));
        assertEquals(1, plan.distinctRounds());
        assertEquals(2, plan.acceptedRows());
        assertEquals(1, plan.rounds().getFirst().wins());
        assertEquals(1, plan.rounds().getFirst().losses());
        assertEquals("2026-10-01", plan.rounds().getFirst().parsedDate());
    }

    @Test
    void repeatedPlayerIsDeduplicatedAcrossPreviewPlanAndPersistence() {
        authorize();
        var file = new MockMultipartFile("file", "duplicados.csv", "text/csv",
                ("Date,Player,Result\n"
                + "01/10/2026,Ana,WIN\n"
                + "01/10/2026,ÁNA,LOSS\n").getBytes(StandardCharsets.UTF_8));
        var mapping = new CommunityImportMappingRequest("Dados", List.of(
                new CommunityImportMappingRequest.ColumnMapping(0, "DATE"),
                new CommunityImportMappingRequest.ColumnMapping(1, "PLAYER"),
                new CommunityImportMappingRequest.ColumnMapping(2, "RESULT")));

        var validation = service.validateMapping(10L, "organizer", file, mapping);
        assertTrue(validation.valid());
        assertEquals(2, validation.dataRows());
        assertEquals(1, validation.validRows());
        assertTrue(validation.warnings().stream()
                .anyMatch(w -> w.contains("Linha 3") && w.contains("primeira ocorrência")));

        var preview = service.normalizedPreview(10L, "organizer", file, mapping);
        assertEquals(1, preview.acceptedRows());
        assertEquals(1, preview.skippedRows());
        assertEquals("Ana", preview.rows().getFirst().player());
        assertEquals("PLAYER_DUPLICATE_IN_ROUND", preview.skipped().getFirst().reason());

        var context = new CommunityImportContextRequest(30L, null);
        var plan = service.finalRoundsPlan(10L, "organizer", file, mapping, context);
        assertEquals(1, plan.rounds().getFirst().players());
        assertEquals(1, plan.rounds().getFirst().wins());
        assertEquals(0, plan.rounds().getFirst().losses());

        Community community = new Community();
        community.setId(10L);
        when(communityRepository.findById(10L)).thenReturn(Optional.of(community));
        CommunityPlayer player = new CommunityPlayer();
        player.setDisplayName("Ana");
        player.setNormalizedName("ana");
        when(communityPlayerRepository.findByCommunityIdAndNormalizedName(10L, "ana"))
                .thenReturn(Optional.of(player));
        when(roundRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.confirmRounds(10L, "organizer", file, mapping, context);
        assertEquals(1, result.createdRounds());
        assertEquals(1, result.createdEntries());
        verify(roundEntryRepository, times(1)).save(any());
        verify(communityPlayerRepository, never()).findByCommunityIdAndNormalizedName(10L, "ána");
    }

    @Test
    void samePlayerInDifferentRoundsMustStillBeImportedTwice() {
        authorize();
        var file = new MockMultipartFile("file", "two-rounds.csv", "text/csv",
                ("Date,Player,Result\n"
                + "01/10/2026,Ana,WIN\n"
                + "02/10/2026,ÁNA,LOSS\n").getBytes(StandardCharsets.UTF_8));
        var mapping = new CommunityImportMappingRequest("Dados", List.of(
                new CommunityImportMappingRequest.ColumnMapping(0, "DATE"),
                new CommunityImportMappingRequest.ColumnMapping(1, "PLAYER"),
                new CommunityImportMappingRequest.ColumnMapping(2, "RESULT")));
        var context = new CommunityImportContextRequest(30L, null);

        var plan = service.finalRoundsPlan(10L, "organizer", file, mapping, context);
        assertEquals(2, plan.distinctRounds());
        assertEquals(1, plan.rounds().get(0).players());
        assertEquals(1, plan.rounds().get(1).players());
        assertEquals(1, plan.rounds().get(0).wins());
        assertEquals(1, plan.rounds().get(1).losses());
    }

    @Test
    void skipsARoundCreatedByAnotherImportAfterTheInitialPlan() {
        authorize();
        var file = singleRound("Ana", "WIN");
        var mapping = roundMapping();
        Community community = new Community();
        community.setId(10L);
        when(communityRepository.findById(10L)).thenReturn(Optional.of(community));
        Instant playedAt = Instant.parse("2026-10-01T00:00:00Z");
        CommunityRound existing = storedRound(71L, "Import 2026-10-01");
        when(roundRepository.findByCommunityIdAndSeasonIdAndPlayedAt(10L, 30L, playedAt))
                .thenReturn(List.of(), List.of(existing));
        when(roundEntryRepository.findByRoundIdOrderByIdAsc(71L))
                .thenReturn(List.of(storedEntry("ana", CommunityMatchResult.WIN)));

        var result = service.confirmRounds(10L, "organizer", file, mapping,
                new CommunityImportContextRequest(30L, null));

        assertEquals(0, result.createdRounds());
        assertEquals(1, result.skippedDuplicateRounds());
        assertEquals(0, result.createdEntries());
        verify(entityManager).lock(any(CommunitySeason.class),
                eq(LockModeType.PESSIMISTIC_WRITE));
        verify(roundRepository, times(2))
                .findByCommunityIdAndSeasonIdAndPlayedAt(10L, 30L, playedAt);
        verify(roundRepository, never()).save(any());
        verify(roundEntryRepository, never()).save(any());
        verifyNoInteractions(communityPlayerRepository);
    }

    @Test
    void reimportingExistingRoundIsIdempotentWithoutRecreatingPlayers() {
        authorize();
        Community community = new Community();
        community.setId(10L);
        when(communityRepository.findById(10L)).thenReturn(Optional.of(community));
        CommunityRound existing = storedRound(71L, "Import 2026-10-01");
        when(roundRepository.findByCommunityIdAndSeasonIdAndPlayedAt(
                eq(10L), eq(30L), any()))
                .thenReturn(List.of(existing));
        when(roundEntryRepository.findByRoundIdOrderByIdAsc(71L))
                .thenReturn(List.of(storedEntry("ana", CommunityMatchResult.WIN)));

        var result = service.confirmRounds(10L, "organizer",
                singleRound("Ana", "WIN"), roundMapping(),
                new CommunityImportContextRequest(30L, null));

        assertEquals(0, result.createdRounds());
        assertEquals(1, result.skippedDuplicateRounds());
        assertEquals(0, result.createdEntries());
        verify(entityManager).lock(any(CommunitySeason.class),
                eq(LockModeType.PESSIMISTIC_WRITE));
        verify(roundRepository, never()).save(any());
        verify(roundEntryRepository, never()).save(any());
        verifyNoInteractions(communityPlayerRepository);
    }

    @Test
    void existingImportedRoundIsRecognizedByItsContentsNotOnlyTheDate() {
        authorize();
        when(roundRepository.findByCommunityIdAndSeasonIdAndPlayedAt(
                eq(10L), eq(30L), any()))
                .thenReturn(List.of(storedRound(71L, "Import 2026-10-01")));
        when(roundEntryRepository.findByRoundIdOrderByIdAsc(71L))
                .thenReturn(List.of(storedEntry("ana", CommunityMatchResult.WIN)));

        var plan = service.finalRoundsPlan(10L, "organizer",
                singleRound("ÁNA", "WIN"), roundMapping(),
                new CommunityImportContextRequest(30L, null));

        assertEquals(1, plan.duplicateRounds());
        assertTrue(plan.rounds().getFirst().duplicate());
    }

    @Test
    void manuallyEnteredRoundOnSameDayDoesNotBlockHistoricalImport() {
        authorize();
        when(roundRepository.findByCommunityIdAndSeasonIdAndPlayedAt(
                eq(10L), eq(30L), any()))
                .thenReturn(List.of(storedRound(70L, "Amigável")));

        var plan = service.finalRoundsPlan(10L, "organizer",
                singleRound("Ana", "WIN"), roundMapping(),
                new CommunityImportContextRequest(30L, null));

        assertEquals(0, plan.duplicateRounds());
        assertFalse(plan.rounds().getFirst().duplicate());
        verifyNoInteractions(roundEntryRepository);
    }

    @Test
    void differentStatisticsOnSameDateAreNotMistakenForAnIdenticalImport() {
        authorize();
        when(roundRepository.findByCommunityIdAndSeasonIdAndPlayedAt(
                eq(10L), eq(30L), any()))
                .thenReturn(List.of(storedRound(71L, "Import 2026-10-01")));
        when(roundEntryRepository.findByRoundIdOrderByIdAsc(71L))
                .thenReturn(List.of(storedEntry("ana", CommunityMatchResult.WIN)));
        var file = new MockMultipartFile("file", "nova-jornada.csv", "text/csv",
                ("Date,Player,Result,Goals\n01/10/2026,Ana,WIN,2\n")
                        .getBytes(StandardCharsets.UTF_8));
        var mapping = new CommunityImportMappingRequest("Dados", List.of(
                new CommunityImportMappingRequest.ColumnMapping(0, "DATE"),
                new CommunityImportMappingRequest.ColumnMapping(1, "PLAYER"),
                new CommunityImportMappingRequest.ColumnMapping(2, "RESULT"),
                new CommunityImportMappingRequest.ColumnMapping(3, "GOALS")));

        var plan = service.finalRoundsPlan(10L, "organizer", file, mapping,
                new CommunityImportContextRequest(30L, null));

        assertEquals(0, plan.duplicateRounds());
        assertFalse(plan.rounds().getFirst().duplicate());
    }

    @Test
    void differentImportedGameSameDayCanBeRecordedWithoutDroppingResults() {
        authorize();
        Community community = new Community();
        community.setId(10L);
        when(communityRepository.findById(10L)).thenReturn(Optional.of(community));
        CommunityRound firstGame = storedRound(71L, "Import 2026-10-01");
        when(roundRepository.findByCommunityIdAndSeasonIdAndPlayedAt(
                eq(10L), eq(30L), any())).thenReturn(List.of(firstGame));
        when(roundEntryRepository.findByRoundIdOrderByIdAsc(71L))
                .thenReturn(List.of(storedEntry("ana", CommunityMatchResult.WIN)));
        when(roundRepository.save(any(CommunityRound.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        CommunityPlayer player = new CommunityPlayer();
        player.setNormalizedName("ana");
        player.setDisplayName("Ana");
        when(communityPlayerRepository.findByCommunityIdAndNormalizedName(10L, "ana"))
                .thenReturn(Optional.of(player));

        // Same source player and date, but this is a distinct game with a LOSS.
        var result = service.confirmRounds(10L, "organizer",
                singleRound("Ana", "LOSS"), roundMapping(),
                new CommunityImportContextRequest(30L, null));

        assertEquals(1, result.createdRounds());
        assertEquals(0, result.skippedDuplicateRounds());
        assertEquals(1, result.createdEntries());
        verify(roundRepository, times(1)).save(any());
        verify(roundEntryRepository, times(1)).save(any());
        verify(entityManager).lock(any(CommunitySeason.class),
                eq(LockModeType.PESSIMISTIC_WRITE));
    }

    private MockMultipartFile singleRound(String player, String result) {
        return new MockMultipartFile("file", "jornada.csv", "text/csv",
                ("Date,Player,Result\n01/10/2026," + player + "," + result + "\n")
                        .getBytes(StandardCharsets.UTF_8));
    }

    private CommunityImportMappingRequest roundMapping() {
        return new CommunityImportMappingRequest("Dados", List.of(
                new CommunityImportMappingRequest.ColumnMapping(0, "DATE"),
                new CommunityImportMappingRequest.ColumnMapping(1, "PLAYER"),
                new CommunityImportMappingRequest.ColumnMapping(2, "RESULT")));
    }

    private CommunityRound storedRound(Long id, String name) {
        return CommunityRound.builder().id(id).name(name).build();
    }

    private CommunityRoundEntry storedEntry(String normalizedName,
            CommunityMatchResult result) {
        CommunityPlayer player = new CommunityPlayer();
        player.setNormalizedName(normalizedName);
        return CommunityRoundEntry.builder().communityPlayer(player).result(result).build();
    }

}
