package com.example.desporto24.service;

import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityRoundEntryRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

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
                new SessionTeamSuggestionService(sessions, profiles, participants, rounds);

        Session session = new Session();
        session.setId(42L);
        session.setMaxParticipants(2);
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

        when(sessions.findByIdForUpdate(42L)).thenReturn(Optional.of(session));
        when(sessions.findById(42L)).thenReturn(Optional.of(session));
        when(participants.findBySessionIdAndStateOrderByJoinedAtAsc(
                42L, ParticipantState.ACTIVE)).thenReturn(List.of(one, two));
        when(rounds.findByRoundCommunityIdOrderByRoundPlayedAtAscRoundIdAscIdAsc(11L))
                .thenReturn(List.of());

        var result = service.confirm(42L, "organizer");

        assertEquals(1, result.teamA().size());
        assertEquals(1, result.teamB().size());
        assertEquals("A", one.getTeamCode());
        assertEquals("B", two.getTeamCode());

        InOrder order = inOrder(sessions, participants);
        order.verify(sessions).findByIdForUpdate(42L);
        order.verify(sessions).findById(42L);
        order.verify(participants).findBySessionIdAndStateOrderByJoinedAtAsc(
                42L, ParticipantState.ACTIVE);
        verify(participants).saveAll(List.of(one, two));
    }

    @Test
    void missingSessionDoesNotAttemptToAssignTeams() {
        SessionRepository sessions = mock(SessionRepository.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        SessionParticipantRepository participants = mock(SessionParticipantRepository.class);
        CommunityRoundEntryRepository rounds = mock(CommunityRoundEntryRepository.class);
        when(sessions.findByIdForUpdate(42L)).thenReturn(Optional.empty());

        SessionTeamSuggestionService service =
                new SessionTeamSuggestionService(sessions, profiles, participants, rounds);

        assertThrows(com.example.desporto24.exception.SessionNotFound.class,
                () -> service.confirm(42L, "organizer"));
        verifyNoInteractions(participants, rounds, profiles);
    }
}
