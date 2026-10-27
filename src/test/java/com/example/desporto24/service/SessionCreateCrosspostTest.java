package com.example.desporto24.service;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.dto.session.CreateSessionRequest;
import com.example.desporto24.model.dto.session.SessionDetailResponse;
import com.example.desporto24.model.session.SessionCommunityShare;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.session.SessionCommunityShareRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.service.impl.SessionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionCreateCrosspostTest {
    @Mock SessionRepository sessions;
    @Mock ProfileRepository profiles;
    @Mock CommunityRepository communities;
    @Mock CommunityMemberRepository members;
    @Mock SessionParticipantRepository participants;
    @Mock SessionCommunityShareRepository shares;
    @Mock SessionViewMapper mapper;
    @Mock PilotEventService events;
    @InjectMocks SessionServiceImpl service;

    private CreateSessionRequest request(List<Long> groups) {
        return new CreateSessionRequest(
                "Futebol", Instant.now().plusSeconds(3600), "Cacém", "Rua do Campo",
                10, BigDecimal.ZERO, false, null, null, null,
                null, null, null, null, 6, null, null, null, null, null, groups);
    }

    @Test
    void oneSessionIsCreatedAndSharedInTwoCommunitiesWithoutDuplicatingAttendance() {
        Profile creator = new Profile(); creator.setId(7L);
        when(profiles.findUserByUsername("organizer")).thenReturn(Optional.of(creator));
        Community first = Community.builder().id(11L).build();
        Community second = Community.builder().id(12L).build();
        when(communities.findAllById(List.of(11L, 12L)))
                .thenReturn(List.of(first, second));
        when(members.existsByCommunityIdAndProfileId(11L,7L)).thenReturn(true);
        when(members.existsByCommunityIdAndProfileId(12L,7L)).thenReturn(true);
        when(sessions.save(any(Session.class))).thenAnswer(inv -> {
            Session s=inv.getArgument(0);
            s.setId(42L);
            return s;
        });
        SessionDetailResponse details=mock(SessionDetailResponse.class);
        when(mapper.toDetail(any(Session.class),eq(creator))).thenReturn(details);

        assertSame(details,service.create(request(List.of(11L,12L)),"organizer"));
        verify(sessions,times(1)).save(any(Session.class));
        verify(participants,times(1)).save(any());
        verify(shares,times(2)).save(argThat(share ->
                share.getSession().getId().equals(42L)
                && (share.getCommunity().getId().equals(11L)
                    || share.getCommunity().getId().equals(12L))));
    }

    @Test
    void refusesToPostToCommunityTheCreatorHasNotJoined() {
        Profile creator = new Profile(); creator.setId(7L);
        when(profiles.findUserByUsername("organizer")).thenReturn(Optional.of(creator));
        when(communities.findAllById(List.of(11L)))
                .thenReturn(List.of(Community.builder().id(11L).build()));
        assertThrows(AccessDeniedException.class,
                () -> service.create(request(List.of(11L)), "organizer"));
        verify(sessions,never()).save(any());
        verify(shares,never()).save(any());
    }

    @Test
    void duplicatesOrMoreThanFiveTargetsCannotCreateAnySession() {
        Profile creator = new Profile();creator.setId(7L);
        when(profiles.findUserByUsername("organizer")).thenReturn(Optional.of(creator));
        assertThrows(IllegalArgumentException.class,() ->
                service.create(request(List.of(11L,11L)), "organizer"));
        assertThrows(IllegalArgumentException.class,() ->
                service.create(request(List.of(1L,2L,3L,4L,5L,6L)), "organizer"));
        verify(sessions,never()).save(any());
    }
}
