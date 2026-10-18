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
