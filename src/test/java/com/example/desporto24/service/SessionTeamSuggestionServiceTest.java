package com.example.desporto24.service;

import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityStandingsArchive;
import com.example.desporto24.model.community.CommunityStandingsArchiveRow;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityRoundEntryRepository;
import com.example.desporto24.repository.community.CommunityPlayerRepository;
import com.example.desporto24.repository.community.CommunityStandingsArchiveRepository;
import com.example.desporto24.repository.community.CommunityStandingsArchiveRowRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionTeamSuggestionServiceTest {

    @Test
    void confirmationLocksSessionBeforeReadingActivePlayersAndAssigningTeams() {
        SessionRepository sessions = mock(SessionRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        SessionParticipantRepository participants = mock(SessionParticipantRepository.class);
        CommunityRoundEntryRepository rounds = mock(CommunityRoundEntryRepository.class);
        SessionTeamSuggestionService service =
                new SessionTeamSuggestionService(sessions, profiles, participants, rounds,
                        mock(CommunityExcelTeamBalancer.class));

        Session session = new Session();
        session.setId(42L);
        session.setMaxParticipants(4);
        session.setSport("Futebol");
        Community community = mock(Community.class);
        when(community.getId()).thenReturn(11L);
        session.setCommunity(community);

        Profile creator = mock(Profile.class);
        when(creator.getId()).thenReturn(7L);
        session.setProfile(creator);
        when(profiles.findUserByUsername("organizer")).thenReturn(Optional.of(creator));

        Profile first = mock(Profile.class);
        Profile second = mock(Profile.class);
        when(first.getUsername()).thenReturn("first");
        when(second.getUsername()).thenReturn("second");
        SessionParticipant one = new SessionParticipant();
        one.setProfile(first);
        one.setState(ParticipantState.ACTIVE);
        SessionParticipant two = new SessionParticipant();
        two.setProfile(second);
        two.setState(ParticipantState.ACTIVE);
        Profile third = mock(Profile.class);
        Profile fourth = mock(Profile.class);
        when(third.getUsername()).thenReturn("third");
        when(fourth.getUsername()).thenReturn("fourth");
        SessionParticipant three = new SessionParticipant();
        three.setProfile(third);
        three.setState(ParticipantState.ACTIVE);
        SessionParticipant four = new SessionParticipant();
        four.setProfile(fourth);
        four.setState(ParticipantState.ACTIVE);

        when(sessions.findByIdForUpdate(42L)).thenReturn(Optional.of(session));
        when(sessions.findById(42L)).thenReturn(Optional.of(session));
        when(participants.findBySessionIdAndStateOrderByJoinedAtAsc(
                42L, ParticipantState.ACTIVE)).thenReturn(List.of(one, two, three, four));
        when(rounds.findByRoundCommunityIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(11L))
                .thenReturn(List.of());

        var result = service.confirm(42L, "organizer");

        assertEquals(2, result.teamA().size());
        assertEquals(2, result.teamB().size());
        assertEquals(2, List.of(one, two, three, four).stream().filter(p -> "A".equals(p.getTeamCode())).count());
        assertEquals(2, List.of(one, two, three, four).stream().filter(p -> "B".equals(p.getTeamCode())).count());

        InOrder order = inOrder(sessions, participants);
        order.verify(sessions).findByIdForUpdate(42L);
        order.verify(sessions).findById(42L);
        order.verify(participants, times(2)).findBySessionIdAndStateOrderByJoinedAtAsc(
                42L, ParticipantState.ACTIVE);
        verify(participants).saveAll(List.of(one, two, three, four));
    }

    @Test
    void missingSessionDoesNotAttemptToAssignTeams() {
        SessionRepository sessions = mock(SessionRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        SessionParticipantRepository participants = mock(SessionParticipantRepository.class);
        CommunityRoundEntryRepository rounds = mock(CommunityRoundEntryRepository.class);
        when(sessions.findByIdForUpdate(42L)).thenReturn(Optional.empty());

        SessionTeamSuggestionService service =
                new SessionTeamSuggestionService(sessions, profiles, participants, rounds,
                        mock(CommunityExcelTeamBalancer.class));

        assertThrows(com.example.desporto24.exception.SessionNotFound.class,
                () -> service.confirm(42L, "organizer"));
        verifyNoInteractions(participants, rounds, profiles);
    }
    @Test
    void tenActiveCommunityPlayersCanPreviewExcelFiveAgainstFiveBeforeExplicitConfirmation() {
        SessionRepository sessions = mock(SessionRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        SessionParticipantRepository participants = mock(SessionParticipantRepository.class);
        CommunityStandingsArchiveRepository archives = mock(CommunityStandingsArchiveRepository.class);
        CommunityStandingsArchiveRowRepository rows = mock(CommunityStandingsArchiveRowRepository.class);
        CommunityPlayerRepository communityPlayers = mock(CommunityPlayerRepository.class);
        CommunityRoundEntryRepository rounds = mock(CommunityRoundEntryRepository.class);
        CommunityExcelTeamBalancer balancer =
                new CommunityExcelTeamBalancer(archives, rows, communityPlayers);
        SessionTeamSuggestionService service =
                new SessionTeamSuggestionService(sessions, profiles, participants, rounds, balancer);

        Session session = new Session();
        session.setId(42L);
        session.setMaxParticipants(10);
        session.setSport("Futebol");
        Community community = mock(Community.class);
        when(community.getId()).thenReturn(11L);
        session.setCommunity(community);
        Profile creator = new Profile();
        creator.setId(7L);
        creator.setUsername("organizer");
        session.setProfile(creator);
        when(profiles.findUserByUsername("organizer")).thenReturn(Optional.of(creator));
        when(sessions.findById(42L)).thenReturn(Optional.of(session));
        when(sessions.findByIdForUpdate(42L)).thenReturn(Optional.of(session));

        List<SessionParticipant> active = new ArrayList<>();
        List<CommunityStandingsArchiveRow> standings = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Profile player = new Profile();
            player.setId(100L + i);
            player.setUsername("player" + i);
            SessionParticipant participation = new SessionParticipant();
            participation.setProfile(player);
            participation.setState(ParticipantState.ACTIVE);
            active.add(participation);

            CommunityStandingsArchiveRow ranking = new CommunityStandingsArchiveRow();
            ranking.setId((long) i);
            ranking.setNormalizedName(player.getUsername());
            ranking.setPoints(i);
            ranking.setPlayed(i);
            standings.add(ranking);
        }
        when(participants.findBySessionIdAndStateOrderByJoinedAtAsc(
                42L, ParticipantState.ACTIVE)).thenReturn(active);
        CommunityStandingsArchive archive = new CommunityStandingsArchive();
        archive.setId(90L);
        archive.setEntryType("INDIVIDUAL");
        when(archives.findFirstByCommunityIdAndEntryTypeOrderByCreatedAtDescIdDesc(
                11L, "INDIVIDUAL")).thenReturn(Optional.of(archive));
        when(rows.findByArchiveIdOrderByPositionAscIdAsc(90L)).thenReturn(standings);
        when(communityPlayers.findByCommunityIdOrderByDisplayNameAsc(11L))
                .thenReturn(List.of());

        var preview = service.suggest(42L, "organizer", "EXCEL_POINTS");
        assertEquals(5, preview.teamA().size());
        assertEquals(5, preview.teamB().size());
        assertEquals(10, preview.totalPlayers());
        assertEquals(10, preview.playersWithStats());
        assertEquals(55.0, preview.teamAScore() + preview.teamBScore(), 0.001);
        assertEquals(1.0, Math.abs(preview.teamAScore() - preview.teamBScore()), 0.001);
        assertTrue(active.stream().allMatch(p -> p.getTeamCode() == null),
                "GET preview must never assign teams");
        verify(participants, never()).saveAll(any());

        var confirmed = service.confirm(42L, "organizer", "EXCEL_POINTS");
        assertEquals(preview, confirmed, "same roster and Excel standings must be repeatable");
        assertEquals(5, active.stream().filter(p -> "A".equals(p.getTeamCode())).count());
        assertEquals(5, active.stream().filter(p -> "B".equals(p.getTeamCode())).count());
        verify(sessions).findByIdForUpdate(42L);
        verify(participants).saveAll(active);
    }

}
