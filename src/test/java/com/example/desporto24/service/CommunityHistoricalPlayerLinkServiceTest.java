package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.*;
import com.example.desporto24.model.dto.community.ProposeHistoricalPlayerLinkRequest;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityHistoricalPlayerLinkServiceTest {
    @Mock CommunityRepository communities;
    @Mock CommunityMemberRepository members;
    @Mock CommunityPlayerRepository players;
    @Mock CommunityHistoricalPlayerLinkRepository links;
    @Mock CommunityRoundEntryRepository entries;
    @Mock ProfileRepository profiles;
    @InjectMocks CommunityHistoricalPlayerLinkService service;

    Profile owner, target, stranger;
    Community community;
    CommunityPlayer historical;

    @BeforeEach
    void fixtures() {
        owner = profile(1L, "owner");
        target = profile(2L, "TMF14");
        stranger = profile(3L, "stranger");
        community = Community.builder().id(7L).owner(owner).build();
        historical = CommunityPlayer.builder().id(10L).community(community)
                .displayName("Ferreirinha").normalizedName("ferreirinha").build();
    }

    @Test
    void managerMayProposeButOnlyTargetMayConfirm() {
        manager();
        when(players.findById(10L)).thenReturn(Optional.of(historical));
        when(profiles.findUserByUsernameIgnoreCase("TMF14")).thenReturn(Optional.of(target));
        when(members.existsByCommunityIdAndProfileId(7L, 2L)).thenReturn(true);
        when(links.findByCommunityIdAndPlayerId(7L, 10L)).thenReturn(Optional.empty());
        when(links.save(any())).thenAnswer(invocation -> {
            CommunityHistoricalPlayerLink link = invocation.getArgument(0);
            link.setId(50L);
            return link;
        });

        var proposed = service.propose(7L, "owner", new ProposeHistoricalPlayerLinkRequest(10L, "TMF14"));
        assertEquals("PENDING", proposed.status());
        assertEquals("Ferreirinha", proposed.historicalName());
        assertEquals("TMF14", proposed.targetUsername());
        assertNull(historical.getProfile());

        CommunityHistoricalPlayerLink link = link(50L, historical, target, "PENDING");
        when(profiles.findUserByUsernameIgnoreCase("stranger")).thenReturn(Optional.of(stranger));
        when(members.existsByCommunityIdAndProfileId(7L, 3L)).thenReturn(true);
        when(links.findByIdAndCommunityId(50L, 7L)).thenReturn(Optional.of(link));
        assertThrows(AccessDeniedException.class, () -> service.decide(7L, 50L, "stranger", true));
        verify(entries, never()).save(any());

        when(profiles.findUserByUsernameIgnoreCase("TMF14")).thenReturn(Optional.of(target));
        when(links.findByCommunityIdAndStatus(7L, "ACCEPTED")).thenReturn(List.of());
        when(entries.findByRoundCommunityIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(7L)).thenReturn(List.of());
        var accepted = service.decide(7L, 50L, "TMF14", true);
        assertEquals("ACCEPTED", accepted.status());
        assertNotNull(link.getConfirmedAt());
        assertNull(historical.getProfile());
        verify(entries, never()).save(any());
    }

    @Test
    void disallowsCrossCommunityAndAlreadyBoundPlayers() {
        Community otherCommunity = Community.builder().id(8L).owner(owner).build();
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.existsByCommunityIdAndProfileId(8L, 1L)).thenReturn(true);
        when(members.findByCommunityIdAndProfileId(8L, 1L)).thenReturn(Optional.of(
                CommunityMember.builder().community(otherCommunity).profile(owner).role(CommunityRole.OWNER).build()));
        when(communities.existsById(8L)).thenReturn(true);
        when(players.findById(10L)).thenReturn(Optional.of(historical));
        assertThrows(org.springframework.web.server.ResponseStatusException.class,
                () -> service.propose(8L, "owner", new ProposeHistoricalPlayerLinkRequest(10L, "TMF14")));
    }

    @Test
    void checksForDuplicateParticipationsInSameRound() {
        when(profiles.findUserByUsernameIgnoreCase("TMF14")).thenReturn(Optional.of(target));
        when(members.existsByCommunityIdAndProfileId(7L, 2L)).thenReturn(true);
        var link = link(50L, historical, target, "PENDING");
        when(links.findByIdAndCommunityId(50L, 7L)).thenReturn(Optional.of(link));
        CommunityRound round = CommunityRound.builder().id(77L).community(community).build();
        CommunityPlayer other = CommunityPlayer.builder().id(11L).community(community).profile(target).build();
        when(entries.findByRoundCommunityIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(7L)).thenReturn(List.of(
                CommunityRoundEntry.builder().round(round).communityPlayer(historical).result(CommunityMatchResult.WIN).build(),
                CommunityRoundEntry.builder().round(round).communityPlayer(other).profile(target)
                        .result(CommunityMatchResult.LOSS).build()));
        when(links.findByCommunityIdAndStatus(7L, "ACCEPTED")).thenReturn(List.of());
        assertThrows(IllegalStateException.class, () -> service.decide(7L, 50L, "TMF14", true));
        assertEquals("PENDING", link.getStatus());
        verify(links, never()).save(any());
    }

    @Test
    void rejectionPreservesOriginalSourceAndDoesNotChangeStandings() {
        when(profiles.findUserByUsernameIgnoreCase("TMF14")).thenReturn(Optional.of(target));
        when(members.existsByCommunityIdAndProfileId(7L, 2L)).thenReturn(true);
        var link = link(50L, historical, target, "PENDING");
        when(links.findByIdAndCommunityId(50L, 7L)).thenReturn(Optional.of(link));
        when(links.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        var rejected = service.decide(7L, 50L, "TMF14", false);
        assertEquals("REJECTED", rejected.status());
        assertNull(historical.getProfile());
        verifyNoInteractions(entries);
    }


    @Test
    void ordinaryMemberCanClaimTheirOwnHistoricalNameWithoutManagerApproval() {
        when(profiles.findUserByUsernameIgnoreCase("TMF14")).thenReturn(Optional.of(target));
        when(members.existsByCommunityIdAndProfileId(7L, 2L)).thenReturn(true);
        when(players.findById(10L)).thenReturn(Optional.of(historical));
        when(links.findByCommunityIdAndPlayerId(7L, 10L)).thenReturn(Optional.empty());
        when(entries.findByRoundCommunityIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(7L)).thenReturn(List.of());
        when(links.findByCommunityIdAndStatus(7L, "ACCEPTED")).thenReturn(List.of());
        when(links.save(any())).thenAnswer(invocation -> {
            CommunityHistoricalPlayerLink link = invocation.getArgument(0);
            link.setId(90L);
            return link;
        });

        var claimed = service.claimForMe(7L, "TMF14", 10L);

        assertEquals("Ferreirinha", claimed.historicalName());
        assertEquals("TMF14", claimed.targetUsername());
        assertEquals("ACCEPTED", claimed.status());
        assertNull(historical.getProfile());
        verify(communities, never()).existsById(any());
        verify(members, never()).findByCommunityIdAndProfileId(any(), any());
        verify(entries, never()).save(any());
    }

    @Test
    void nonMembersAndOtherCommunitiesCannotClaimNames() {
        when(profiles.findUserByUsernameIgnoreCase("stranger")).thenReturn(Optional.of(stranger));
        when(members.existsByCommunityIdAndProfileId(7L, 3L)).thenReturn(false);
        assertThrows(AccessDeniedException.class, () -> service.claimForMe(7L, "stranger", 10L));
        verifyNoInteractions(players, links);

        when(profiles.findUserByUsernameIgnoreCase("TMF14")).thenReturn(Optional.of(target));
        when(members.existsByCommunityIdAndProfileId(8L, 2L)).thenReturn(true);
        when(players.findById(10L)).thenReturn(Optional.of(historical));
        assertThrows(org.springframework.web.server.ResponseStatusException.class,
                () -> service.claimForMe(8L, "TMF14", 10L));
    }

    @Test
    void claimedHistoricalNameCannotBeTakenByAnotherMember() {
        when(profiles.findUserByUsernameIgnoreCase("TMF14")).thenReturn(Optional.of(target));
        when(members.existsByCommunityIdAndProfileId(7L, 2L)).thenReturn(true);
        when(players.findById(10L)).thenReturn(Optional.of(historical));
        when(links.findByCommunityIdAndPlayerId(7L, 10L))
                .thenReturn(Optional.of(link(90L, historical, owner, "ACCEPTED")));
        assertThrows(IllegalStateException.class, () -> service.claimForMe(7L, "TMF14", 10L));
        verify(entries, never()).save(any());
        verify(links, never()).save(any());
    }

    @Test
    void claimingSameNameTwiceIsSafeAndDoesNotRewriteAnyHistory() {
        when(profiles.findUserByUsernameIgnoreCase("TMF14")).thenReturn(Optional.of(target));
        when(members.existsByCommunityIdAndProfileId(7L, 2L)).thenReturn(true);
        when(players.findById(10L)).thenReturn(Optional.of(historical));
        when(links.findByCommunityIdAndPlayerId(7L, 10L))
                .thenReturn(Optional.of(link(90L, historical, target, "ACCEPTED")));
        assertEquals("ACCEPTED", service.claimForMe(7L, "TMF14", 10L).status());
        verifyNoInteractions(entries);
        verify(links, never()).save(any());
    }

    @Test
    void selfClaimRejectsTwoParticipationsBySamePersonInOneRound() {
        when(profiles.findUserByUsernameIgnoreCase("TMF14")).thenReturn(Optional.of(target));
        when(members.existsByCommunityIdAndProfileId(7L, 2L)).thenReturn(true);
        when(players.findById(10L)).thenReturn(Optional.of(historical));
        when(links.findByCommunityIdAndPlayerId(7L, 10L)).thenReturn(Optional.empty());
        CommunityRound round = CommunityRound.builder().id(77L).community(community).build();
        CommunityPlayer existing = CommunityPlayer.builder().id(11L).community(community).profile(target).build();
        when(entries.findByRoundCommunityIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(7L)).thenReturn(List.of(
                CommunityRoundEntry.builder().round(round).communityPlayer(historical).build(),
                CommunityRoundEntry.builder().round(round).communityPlayer(existing).profile(target).build()));
        when(links.findByCommunityIdAndStatus(7L, "ACCEPTED")).thenReturn(List.of());
        assertThrows(IllegalStateException.class, () -> service.claimForMe(7L, "TMF14", 10L));
        verify(links, never()).save(any());
    }

    @Test
    void onlyClaimOwnerMayReleaseItWithoutDeletingHistoricalResults() {
        when(profiles.findUserByUsernameIgnoreCase("stranger")).thenReturn(Optional.of(stranger));
        when(members.existsByCommunityIdAndProfileId(7L, 3L)).thenReturn(true);
        var current = link(90L, historical, target, "ACCEPTED");
        when(links.findByIdAndCommunityId(90L, 7L)).thenReturn(Optional.of(current));
        assertThrows(AccessDeniedException.class, () -> service.releaseMyClaim(7L, 90L, "stranger"));

        when(profiles.findUserByUsernameIgnoreCase("TMF14")).thenReturn(Optional.of(target));
        when(members.existsByCommunityIdAndProfileId(7L, 2L)).thenReturn(true);
        service.releaseMyClaim(7L, 90L, "TMF14");
        assertEquals("REJECTED", current.getStatus());
        assertNull(current.getConfirmedAt());
        assertNull(historical.getProfile());
        verify(links).save(current);
        verifyNoInteractions(entries);
    }

    @Test
    void normalMemberMayBrowseHistoricalNamesWithoutSeeingOtherProfiles() {
        when(profiles.findUserByUsernameIgnoreCase("TMF14")).thenReturn(Optional.of(target));
        when(members.existsByCommunityIdAndProfileId(7L, 2L)).thenReturn(true);
        when(players.findByCommunityIdOrderByDisplayNameAsc(7L)).thenReturn(List.of(historical));
        when(links.findByCommunityIdAndStatus(7L, "ACCEPTED"))
                .thenReturn(List.of(link(90L, historical, owner, "ACCEPTED")));
        var candidates = service.candidates(7L, "TMF14");
        assertEquals(1, candidates.size());
        assertEquals("ACCEPTED", candidates.getFirst().linkStatus());
        assertNull(candidates.getFirst().linkedUsername());
    }

    private void manager() {
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(members.existsByCommunityIdAndProfileId(7L, 1L)).thenReturn(true);
        when(members.findByCommunityIdAndProfileId(7L, 1L)).thenReturn(Optional.of(
                CommunityMember.builder().community(community).profile(owner).role(CommunityRole.OWNER).build()));
        when(communities.existsById(7L)).thenReturn(true);
    }

    private Profile profile(Long id, String username) {
        Profile p = new Profile();
        p.setId(id);
        p.setUsername(username);
        return p;
    }

    private CommunityHistoricalPlayerLink link(Long id, CommunityPlayer player, Profile profile, String status) {
        return CommunityHistoricalPlayerLink.builder().id(id).community(community)
                .player(player).profile(profile).status(status)
                .proposedByUsername("owner").createdAt(Instant.now()).build();
    }
}
