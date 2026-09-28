package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunityRoundEntry;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityRoundEntryRepository;
import com.example.desporto24.repository.community.CommunityRoundRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommunityCompetitionServiceTest {

    @Mock private CommunityRepository communityRepository;
    @Mock private CommunityMemberRepository memberRepository;
    @Mock private CommunityRoundRepository roundRepository;
    @Mock private CommunityRoundEntryRepository entryRepository;
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
