package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.TournamentEntryType;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityInlineSpeedProgressionServiceTest {
    @Mock ProfileRepository profiles;
    @Mock CommunityMemberRepository members;
    @Mock CommunityTournamentRepository tournaments;
    @Mock CommunityRoundRepository rounds;
    @Mock CommunityRoundEntryRepository entries;
    @Mock CommunityInlineSpeedHeatRepository links;
    @Mock CommunityInlineSpeedDecisionRepository decisions;
    @Mock CommunityInlineSpeedTieBreakRepository tieBreaks;
    @Mock CommunityInlineSpeedTieBreakRevisionRepository tieBreakRevisions;
    @Mock CommunityInlineSpeedTieTrialRepository tieTrials;
    @Mock CommunityInlineSpeedTieTrialResultRepository trialResults;
    @InjectMocks CommunityInlineSpeedProgressionService service;

    private Community community;
    private CommunityTournament tournament;
    private CommunitySeason season;
    private CommunityRound heat;

    @BeforeEach
    void setup() {
        community = Community.builder().id(7L).build();
        season = CommunitySeason.builder().id(42L).community(community).active(true).build();
        tournament = CommunityTournament.builder().id(9L).community(community)
                .sport("Patinagem de Velocidade").entryType(TournamentEntryType.INDIVIDUAL)
                .status(TournamentStatus.DRAFT).build();
        heat = CommunityRound.builder().id(21L).community(community).season(season)
                .sport("Patinagem de Velocidade")
                .eventType("INLINE_TRACK_500D_FIRST_ROUND").name("Primeira fase").build();
    }

    private void admin() {
        Profile user = mock(Profile.class);
        when(user.getId()).thenReturn(100L);
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(user));
        CommunityMember member = mock(CommunityMember.class);
        when(member.getRole()).thenReturn(CommunityRole.OWNER);
        when(members.findByCommunityIdAndProfileId(7L, 100L)).thenReturn(Optional.of(member));
    }

    @Test void linksRealRoundWithoutAlteringOriginalResults() {
        admin();
        when(tournaments.findByIdForUpdate(9L)).thenReturn(Optional.of(tournament));
        when(rounds.findById(21L)).thenReturn(Optional.of(heat));
        CommunityRoundEntry athlete = CommunityRoundEntry.builder()
                .round(heat).communityPlayer(CommunityPlayer.builder().id(5L).build()).build();
        when(entries.findByRoundIdOrderByIdAsc(21L)).thenReturn(List.of(athlete));

        service.link(7L, 9L, 21L, 1, "owner");

        assertEquals(TournamentStatus.IN_PROGRESS, tournament.getStatus());
        verify(links).save(argThat(link -> link.getRound() == heat
                && link.getTournament() == tournament
                && "FIRST".equals(link.getStage()) && link.getHeatNumber() == 1));
        verify(entries, never()).save(any());
        verify(rounds, never()).save(any());
    }

    @Test void rejectsHeatFromDifferentCommunity() {
        admin();
        when(tournaments.findByIdForUpdate(9L)).thenReturn(Optional.of(tournament));
        heat.setCommunity(Community.builder().id(8L).build());
        when(rounds.findById(21L)).thenReturn(Optional.of(heat));
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.link(7L, 9L, 21L, 1, "owner"));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatusCode());
        verifyNoInteractions(links, entries);
    }

    @Test void rejectsReassigningOneHeatUnderDifferentNumber() {
        admin();
        when(tournaments.findByIdForUpdate(9L)).thenReturn(Optional.of(tournament));
        when(rounds.findById(21L)).thenReturn(Optional.of(heat));
        when(links.findByTournamentIdOrderByIdAsc(9L)).thenReturn(List.of(
                CommunityInlineSpeedHeatLink.builder().round(heat).stage("FIRST")
                        .heatNumber(1).tournament(tournament).build()));
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.link(7L, 9L, 21L, 2, "owner"));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(links, never()).save(any());
    }

    @Test void blocksHistoryBeforeLinking() {
        admin();
        tournament.setHistoricalArchiveId(12L);
        when(tournaments.findByIdForUpdate(9L)).thenReturn(Optional.of(tournament));
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.link(7L, 9L, 21L, 1, "owner"));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verifyNoInteractions(rounds, entries, links);
    }
}
