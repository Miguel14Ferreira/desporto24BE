package com.example.desporto24.service;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.*;
import com.example.desporto24.repository.ProfileRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
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
    @Mock CommunityMemberRepository members;
    @Mock CommunityPlayerRepository players;
    @Mock CommunityHistoricalPlayerLinkRepository links;
    @Mock CommunityRoundEntryRepository entries;
    @Mock ProfileRepository profiles;
    @Mock EntityManager entityManager;
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
    void ordinaryMemberCanClaimTheirOwnHistoricalNameWithoutManagerApproval() {
        when(profiles.findUserByUsernameIgnoreCase("TMF14")).thenReturn(Optional.of(target));
        when(members.existsByCommunityIdAndProfileId(7L, 2L)).thenReturn(true);
        when(players.findByIdForUpdate(10L)).thenReturn(Optional.of(historical));
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
        verify(entityManager).lock(target, LockModeType.PESSIMISTIC_WRITE);
        verify(players).findByIdForUpdate(10L);
        assertNull(historical.getProfile());
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
        when(players.findByIdForUpdate(10L)).thenReturn(Optional.of(historical));
        assertThrows(org.springframework.web.server.ResponseStatusException.class,
                () -> service.claimForMe(8L, "TMF14", 10L));
    }

    @Test
    void claimedHistoricalNameCannotBeTakenByAnotherMember() {
        when(profiles.findUserByUsernameIgnoreCase("TMF14")).thenReturn(Optional.of(target));
        when(members.existsByCommunityIdAndProfileId(7L, 2L)).thenReturn(true);
        when(players.findByIdForUpdate(10L)).thenReturn(Optional.of(historical));
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
        when(players.findByIdForUpdate(10L)).thenReturn(Optional.of(historical));
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
        when(players.findByIdForUpdate(10L)).thenReturn(Optional.of(historical));
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


    @Test
    void aPendingManagerSuggestionNeverReservesAnotherMembersHistoricalName() {
        when(profiles.findUserByUsernameIgnoreCase("TMF14")).thenReturn(Optional.of(target));
        when(members.existsByCommunityIdAndProfileId(7L, 2L)).thenReturn(true);
        when(players.findByIdForUpdate(10L)).thenReturn(Optional.of(historical));
        var oldSuggestion = link(50L, historical, owner, "PENDING");
        when(links.findByCommunityIdAndPlayerId(7L, 10L)).thenReturn(Optional.of(oldSuggestion));
        when(entries.findByRoundCommunityIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(7L))
                .thenReturn(List.of());
        when(links.findByCommunityIdAndStatus(7L, "ACCEPTED")).thenReturn(List.of());
        when(links.save(oldSuggestion)).thenReturn(oldSuggestion);

        var claimed = service.claimForMe(7L, "TMF14", 10L);

        assertEquals("ACCEPTED", claimed.status());
        assertEquals("TMF14", claimed.targetUsername());
        assertEquals(50L, claimed.id()); // reused row, no unique-key conflict
        assertEquals(2L, oldSuggestion.getProfile().getId());
        assertEquals("TMF14", oldSuggestion.getProposedByUsername());
        assertNotNull(oldSuggestion.getConfirmedAt());
        assertNull(historical.getProfile());
        verify(entityManager).lock(target, LockModeType.PESSIMISTIC_WRITE);
        verify(entries, never()).save(any());
    }

    @Test
    void pendingUnacceptedSuggestionsAppearAsAvailableToAllCommunityMembers() {
        when(profiles.findUserByUsernameIgnoreCase("TMF14")).thenReturn(Optional.of(target));
        when(members.existsByCommunityIdAndProfileId(7L, 2L)).thenReturn(true);
        when(players.findByCommunityIdOrderByDisplayNameAsc(7L)).thenReturn(List.of(historical));
        when(links.findByCommunityIdAndStatus(7L, "ACCEPTED")).thenReturn(List.of());

        var candidates = service.candidates(7L, "TMF14");

        assertEquals(1, candidates.size());
        assertEquals("UNLINKED", candidates.getFirst().linkStatus());
        assertNull(candidates.getFirst().linkedUsername());
        verify(links, never()).findByCommunityIdAndStatus(7L, "PENDING");
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
