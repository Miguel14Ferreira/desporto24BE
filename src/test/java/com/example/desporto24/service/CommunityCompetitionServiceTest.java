package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunityRoundEntry;
import com.example.desporto24.model.community.CommunityRound;
import com.example.desporto24.model.community.CommunityPlayer;
import com.example.desporto24.model.community.CommunityHistoricalPlayerLink;
import com.example.desporto24.model.community.CommunitySeason;
import com.example.desporto24.model.dto.community.CreateCommunitySeasonRequest;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityHistoricalPlayerLinkRepository;
import com.example.desporto24.repository.community.CommunityPlayerRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityRoundEntryRepository;
import com.example.desporto24.repository.community.CommunityRoundRepository;
import com.example.desporto24.repository.community.CommunitySeasonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommunityCompetitionServiceTest {

    @Mock private CommunityRepository communityRepository;
    @Mock private CommunityMemberRepository memberRepository;
    @Mock private CommunityPlayerRepository playerRepository;
    @Mock private CommunityHistoricalPlayerLinkRepository historicalPlayerLinkRepository;
    @Mock private CommunityRoundRepository roundRepository;
    @Mock private CommunityRoundEntryRepository entryRepository;
    @Mock private CommunitySeasonRepository seasonRepository;
    @Mock private ProfileRepository profileRepository;

    @InjectMocks
    private CommunityCompetitionService service;

    @Test
    void overviewCalculatesThreeOneZeroStandingsAndStatistics() {
        Profile miguel = profile(1L, "TMF14", "Miguel Ferreira");
        Profile mimocas = profile(2L, "mimocas", "Mimocas");

        Community community = Community.builder()
                .id(7L)
                .name("CNG")
                .visibility(CommunityVisibility.PRIVATE)
                .owner(miguel)
                .rankingsEnabled(true)
                .winPoints(3)
                .drawPoints(1)
                .lossPoints(0)
                .createdAt(Instant.now())
                .build();

        when(profileRepository.findUserByUsernameIgnoreCase("TMF14"))
                .thenReturn(Optional.of(miguel));
        when(communityRepository.findById(7L))
                .thenReturn(Optional.of(community));
        when(memberRepository.existsByCommunityIdAndProfileId(7L, 1L))
                .thenReturn(true);
        when(memberRepository.findByCommunityIdOrderByJoinedAtAsc(7L))
                .thenReturn(List.of(
                        membership(community, miguel, CommunityRole.OWNER),
                        membership(community, mimocas, CommunityRole.MEMBER)
                ));
        when(roundRepository.findByCommunityIdOrderByPlayedAtDescIdDesc(7L))
                .thenReturn(List.of());
        when(seasonRepository.findByCommunityIdAndActiveTrue(7L))
                .thenReturn(Optional.empty());
        when(seasonRepository.findByCommunityIdOrderByCreatedAtDesc(7L))
                .thenReturn(List.of());
        when(entryRepository.findByRoundCommunityIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(7L))
                .thenReturn(List.of(
                        entry(miguel, CommunityMatchResult.WIN, 2, 1, true, false),
                        entry(mimocas, CommunityMatchResult.LOSS, 0, 0, false, false),
                        entry(miguel, CommunityMatchResult.WIN, 1, 2, false, true),
                        entry(mimocas, CommunityMatchResult.LOSS, 1, 0, false, false)
                ));

        var overview = service.overview(7L, "TMF14");

        assertEquals(true, overview.settings().rankingsEnabled());
        assertEquals(3, overview.settings().winPoints());
        assertEquals(2, overview.standings().size());

        var leader = overview.standings().get(0);
        assertEquals("TMF14", leader.username());
        assertEquals(6, leader.points());
        assertEquals(2, leader.games());
        assertEquals(2, leader.wins());
        assertEquals(3, leader.goals());
        assertEquals(3, leader.assists());
        assertEquals(1, leader.mvps());
        assertEquals(1, leader.cleanSheets());
        assertEquals(100.0, leader.winRate());
        assertEquals("W2", leader.currentStreak());
        assertEquals(2, leader.bestWinStreak());
    }

    @Test
    void createsAnActiveSeasonWhenNoSeasonIsOpen() {
        Profile miguel = profile(1L, "TMF14", "Miguel Ferreira");
        Community community = Community.builder()
                .id(7L)
                .name("CNG")
                .visibility(CommunityVisibility.PRIVATE)
                .owner(miguel)
                .rankingsEnabled(true)
                .winPoints(3)
                .drawPoints(1)
                .lossPoints(0)
                .createdAt(Instant.now())
                .build();

        when(profileRepository.findUserByUsernameIgnoreCase("TMF14"))
                .thenReturn(Optional.of(miguel));
        when(memberRepository.findByCommunityIdAndProfileId(7L, 1L))
                .thenReturn(Optional.of(membership(
                        community, miguel, CommunityRole.OWNER)));
        when(communityRepository.findById(7L))
                .thenReturn(Optional.of(community));
        when(seasonRepository.findByCommunityIdAndActiveTrue(7L))
                .thenReturn(Optional.empty());
        when(seasonRepository.save(any(CommunitySeason.class)))
                .thenAnswer(invocation -> {
                    CommunitySeason season = invocation.getArgument(0);
                    season.setId(44L);
                    return season;
                });

        var result = service.createSeason(
                7L,
                "TMF14",
                new CreateCommunitySeasonRequest("Época 2026/27", null)
        );

        assertEquals(44L, result.id());
        assertEquals("Época 2026/27", result.name());
        assertEquals(true, result.active());
    }

    @Test
    void closingSeasonStoresChampionAndKeepsTrophyAfterScoringRulesChange() {
        Profile owner = profile(1L, "TMF14", "Miguel Ferreira");
        Profile challenger = profile(2L, "other", "Other Player");
        Community community = Community.builder()
                .id(7L).name("CNG").visibility(CommunityVisibility.PRIVATE)
                .owner(owner).rankingsEnabled(true)
                .winPoints(3).drawPoints(1).lossPoints(0)
                .createdAt(Instant.now()).build();
        CommunitySeason season = CommunitySeason.builder()
                .id(44L).community(community).name("2026/27")
                .startsAt(Instant.now()).active(true).createdAt(Instant.now()).build();

        when(profileRepository.findUserByUsernameIgnoreCase("TMF14"))
                .thenReturn(Optional.of(owner));
        when(memberRepository.findByCommunityIdAndProfileId(7L, 1L))
                .thenReturn(Optional.of(membership(community, owner, CommunityRole.OWNER)));
        when(communityRepository.findById(7L)).thenReturn(Optional.of(community));
        when(seasonRepository.findById(44L)).thenReturn(Optional.of(season));
        when(seasonRepository.save(season)).thenReturn(season);
        when(memberRepository.findByCommunityIdOrderByJoinedAtAsc(7L))
                .thenReturn(List.of(
                        membership(community, owner, CommunityRole.OWNER),
                        membership(community, challenger, CommunityRole.MEMBER)));
        when(entryRepository.findByRoundSeasonIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(44L))
                .thenReturn(List.of(
                        entry(owner, CommunityMatchResult.WIN, 2, 1, false, false),
                        entry(challenger, CommunityMatchResult.DRAW, 1, 0, false, false)));

        var closed = service.closeSeason(7L, 44L, "TMF14");
        assertEquals("TMF14", closed.championUsername());
        assertEquals("Miguel Ferreira", closed.championDisplayName());
        assertEquals(3, closed.championPoints());
        assertTrue(season.isChampionFinalized());
        assertEquals(false, season.isActive());

        // A later scoring change would make the challenger lead (10 vs. 0),
        // but the finalized season trophy must remain with its original champion.
        community.setWinPoints(0);
        community.setDrawPoints(10);
        when(seasonRepository.findByCommunityIdAndActiveTrue(7L))
                .thenReturn(Optional.empty());
        when(seasonRepository.findByCommunityIdOrderByCreatedAtDesc(7L))
                .thenReturn(List.of(season));
        when(memberRepository.existsByCommunityIdAndProfileId(7L, 1L))
                .thenReturn(true);

        var overview = service.overview(7L, "TMF14");
        assertEquals("TMF14", overview.seasons().get(0).championUsername());
        assertEquals(3, overview.seasons().get(0).championPoints());
    }

    @Test
    void closingSeasonWithoutGamesFinalizesTrophyWithNoChampion() {
        Profile owner = profile(1L, "TMF14", "Miguel Ferreira");
        Community community = Community.builder()
                .id(7L).name("CNG").visibility(CommunityVisibility.PRIVATE)
                .owner(owner).rankingsEnabled(true)
                .winPoints(3).drawPoints(1).lossPoints(0)
                .createdAt(Instant.now()).build();
        CommunitySeason season = CommunitySeason.builder()
                .id(44L).community(community).name("Empty season")
                .startsAt(Instant.now()).active(true).createdAt(Instant.now()).build();
        when(profileRepository.findUserByUsernameIgnoreCase("TMF14"))
                .thenReturn(Optional.of(owner));
        when(memberRepository.findByCommunityIdAndProfileId(7L, 1L))
                .thenReturn(Optional.of(membership(community, owner, CommunityRole.OWNER)));
        when(communityRepository.findById(7L)).thenReturn(Optional.of(community));
        when(seasonRepository.findById(44L)).thenReturn(Optional.of(season));
        when(seasonRepository.save(season)).thenReturn(season);
        when(memberRepository.findByCommunityIdOrderByJoinedAtAsc(7L))
                .thenReturn(List.of(membership(community, owner, CommunityRole.OWNER)));

        var closed = service.closeSeason(7L, 44L, "TMF14");

        assertTrue(season.isChampionFinalized());
        assertNull(closed.championUsername());
        assertNull(closed.championPoints());
    }


    @Test
    void acceptedAliasCombinesHistoricalGamesWithRealProfileWithoutChangingOriginalPlayer() {
        Profile target = profile(1L, "TMF14", "Miguel Ferreira");
        target.setNickname("Migas"); // The Excel name wins over nickname and full name.
        Community community = Community.builder().id(7L).owner(target)
                .visibility(CommunityVisibility.PRIVATE)
                .rankingsEnabled(true).winPoints(3).drawPoints(1).lossPoints(0).build();
        CommunityPlayer historical = CommunityPlayer.builder().id(10L).community(community)
                .displayName("Ferreirinha").normalizedName("ferreirinha").build();
        CommunityPlayer original = CommunityPlayer.builder().id(11L).community(community)
                .displayName("Miguel Ferreira").normalizedName("miguel ferreira").profile(target).build();
        CommunityRound round1 = CommunityRound.builder().id(21L).community(community).build();
        CommunityRound round2 = CommunityRound.builder().id(22L).community(community).build();
        CommunityRoundEntry older = CommunityRoundEntry.builder().round(round1)
                .communityPlayer(historical).result(CommunityMatchResult.WIN).build();
        CommunityRoundEntry recent = CommunityRoundEntry.builder().round(round2)
                .communityPlayer(original).profile(target).result(CommunityMatchResult.LOSS).build();
        CommunityHistoricalPlayerLink accepted = CommunityHistoricalPlayerLink.builder()
                .id(50L).community(community).player(historical).profile(target).status("ACCEPTED").build();

        when(profileRepository.findUserByUsernameIgnoreCase("TMF14")).thenReturn(Optional.of(target));
        when(communityRepository.findById(7L)).thenReturn(Optional.of(community));
        when(memberRepository.existsByCommunityIdAndProfileId(7L, 1L)).thenReturn(true);
        when(memberRepository.findByCommunityIdOrderByJoinedAtAsc(7L))
                .thenReturn(List.of(membership(community, target, CommunityRole.OWNER)));
        when(playerRepository.findByCommunityIdAndProfileId(7L, 1L)).thenReturn(Optional.of(original));
        when(historicalPlayerLinkRepository.findByCommunityIdAndStatus(7L, "ACCEPTED"))
                .thenReturn(List.of(accepted));
        when(entryRepository.findByRoundCommunityIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(7L))
                .thenReturn(List.of(older, recent));
        when(roundRepository.findByCommunityIdOrderByPlayedAtDescIdDesc(7L))
                .thenReturn(List.of(round2, round1));
        when(seasonRepository.findByCommunityIdAndActiveTrue(7L)).thenReturn(Optional.empty());
        when(seasonRepository.findByCommunityIdOrderByCreatedAtDesc(7L)).thenReturn(List.of());
        when(entryRepository.findByRoundIdOrderByIdAsc(21L)).thenReturn(List.of(older));
        when(entryRepository.findByRoundIdOrderByIdAsc(22L)).thenReturn(List.of(recent));

        var overview = service.overview(7L, "TMF14");
        assertEquals(1, overview.standings().size());
        assertEquals("TMF14", overview.standings().getFirst().username());
        assertEquals("Ferreirinha", overview.standings().getFirst().fullName());
        assertEquals(2, overview.standings().getFirst().games());
        assertEquals(3, overview.standings().getFirst().points());
        // The Excel nickname is the canonical community-facing name in all
        // screens, while @TMF14 remains the associated Jynlo identity.
        var linkedEntry = overview.rounds().get(1).entries().getFirst();
        assertEquals(overview.standings().getFirst().fullName(), linkedEntry.fullName());
        assertEquals("Ferreirinha", linkedEntry.fullName());
        assertEquals("TMF14", linkedEntry.username());
        assertEquals(1L, linkedEntry.profileId());
        var registeredEntry = overview.rounds().get(0).entries().getFirst();
        assertEquals("Ferreirinha", registeredEntry.fullName());
        assertEquals("TMF14", registeredEntry.username());
        assertEquals("Ferreirinha", historical.getDisplayName());
        assertNull(historical.getProfile());
    }

    @Test
    void directLinkedImportedNameWinsOverCurrentAccountName() {
        Profile target = profile(1L, "TMF14", "Miguel Ferreira");
        target.setNickname("Migas");
        Community community = Community.builder().id(7L).owner(target)
                .visibility(CommunityVisibility.PRIVATE).rankingsEnabled(true).build();
        CommunityPlayer fromExcel = CommunityPlayer.builder().id(11L).community(community)
                .displayName("Ferreirinha").normalizedName("ferreirinha").profile(target).build();
        CommunityRound round = CommunityRound.builder().id(21L).community(community).build();
        CommunityRoundEntry historical = CommunityRoundEntry.builder().round(round)
                .communityPlayer(fromExcel).profile(target).result(CommunityMatchResult.WIN).build();
        when(profileRepository.findUserByUsernameIgnoreCase("TMF14")).thenReturn(Optional.of(target));
        when(communityRepository.findById(7L)).thenReturn(Optional.of(community));
        when(memberRepository.existsByCommunityIdAndProfileId(7L, 1L)).thenReturn(true);
        when(memberRepository.findByCommunityIdOrderByJoinedAtAsc(7L))
                .thenReturn(List.of(membership(community, target, CommunityRole.OWNER)));
        when(playerRepository.findByCommunityIdAndProfileId(7L, 1L)).thenReturn(Optional.of(fromExcel));
        when(roundRepository.findByCommunityIdOrderByPlayedAtDescIdDesc(7L)).thenReturn(List.of(round));
        when(seasonRepository.findByCommunityIdAndActiveTrue(7L)).thenReturn(Optional.empty());
        when(seasonRepository.findByCommunityIdOrderByCreatedAtDesc(7L)).thenReturn(List.of());
        when(entryRepository.findByRoundCommunityIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(7L))
                .thenReturn(List.of(historical));
        when(entryRepository.findByRoundIdOrderByIdAsc(21L)).thenReturn(List.of(historical));

        var overview = service.overview(7L, "TMF14");
        assertEquals("Ferreirinha", overview.standings().getFirst().fullName());
        assertEquals("Ferreirinha", overview.rounds().getFirst().entries().getFirst().fullName());
        assertEquals("TMF14", overview.standings().getFirst().username());
        assertEquals("TMF14", overview.rounds().getFirst().entries().getFirst().username());
    }

    @Test
    void unclaimedImportedPlayersKeepOriginalNameWithoutInventedProfile() {
        Profile target = profile(1L, "TMF14", "Miguel Ferreira");
        Community community = Community.builder().id(7L).owner(target)
                .visibility(CommunityVisibility.PRIVATE).rankingsEnabled(true).build();
        CommunityPlayer imported = CommunityPlayer.builder().id(10L).community(community)
                .displayName("Sem Perfil").normalizedName("sem perfil").build();
        CommunityRound round = CommunityRound.builder().id(20L).community(community).build();
        CommunityRoundEntry original = CommunityRoundEntry.builder().round(round)
                .communityPlayer(imported).result(CommunityMatchResult.WIN).build();

        when(profileRepository.findUserByUsernameIgnoreCase("TMF14")).thenReturn(Optional.of(target));
        when(communityRepository.findById(7L)).thenReturn(Optional.of(community));
        when(memberRepository.existsByCommunityIdAndProfileId(7L, 1L)).thenReturn(true);
        when(roundRepository.findByCommunityIdOrderByPlayedAtDescIdDesc(7L)).thenReturn(List.of(round));
        when(seasonRepository.findByCommunityIdAndActiveTrue(7L)).thenReturn(Optional.empty());
        when(seasonRepository.findByCommunityIdOrderByCreatedAtDesc(7L)).thenReturn(List.of());
        when(entryRepository.findByRoundIdOrderByIdAsc(20L)).thenReturn(List.of(original));
        when(entryRepository.findByRoundCommunityIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(7L))
                .thenReturn(List.of(original));
        when(memberRepository.findByCommunityIdOrderByJoinedAtAsc(7L))
                .thenReturn(List.of(membership(community, target, CommunityRole.OWNER)));

        var entry = service.overview(7L, "TMF14").rounds().getFirst().entries().getFirst();
        assertEquals("Sem Perfil", entry.fullName());
        assertNull(entry.username());
        assertNull(entry.profileId());
        assertNull(imported.getProfile());
    }

    private Profile profile(Long id, String username, String fullName) {
        Profile profile = new Profile();
        profile.setId(id);
        profile.setUsername(username);
        profile.setFullName(fullName);
        return profile;
    }

    private CommunityMember membership(
            Community community,
            Profile profile,
            CommunityRole role) {
        return CommunityMember.builder()
                .community(community)
                .profile(profile)
                .role(role)
                .joinedAt(Instant.now())
                .build();
    }

    private CommunityRoundEntry entry(
            Profile profile,
            CommunityMatchResult result,
            int goals,
            int assists,
            boolean mvp,
            boolean cleanSheet) {
        return CommunityRoundEntry.builder()
                .profile(profile)
                .result(result)
                .goals(goals)
                .assists(assists)
                .mvp(mvp)
                .cleanSheet(cleanSheet)
                .build();
    }
}
