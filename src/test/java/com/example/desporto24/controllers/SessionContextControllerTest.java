package com.example.desporto24.controllers;

import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunitySeason;
import com.example.desporto24.model.session.SessionCommunityShare;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionCommunityShareRepository;
import com.example.desporto24.service.interfaces.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionContextControllerTest {
    private final SessionService service=mock(SessionService.class);
    private final SessionRepository sessions=mock(SessionRepository.class);
    private final SessionCommunityShareRepository shares=mock(SessionCommunityShareRepository.class);
    private final SessionContextController controller=new SessionContextController(service,sessions,shares);
    private final Authentication auth=mock(Authentication.class);

    @Test void showsDirectAndSharedCommunitiesWithoutDuplicatingAndMarksActualSeason() {
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("organizer");
        Session session=new Session();session.setId(18L);
        Community direct=new Community();direct.setId(3L);direct.setName("Pádel Amigos");
        Community another=new Community();another.setId(8L);another.setName("Futsal Lisboa");
        session.setCommunity(direct);
        CommunitySeason season=new CommunitySeason();season.setName("Época 2026/27");
        session.setSeasonCompetition(season);
        when(sessions.findById(18L)).thenReturn(Optional.of(session));
        when(shares.findBySession_IdOrderByIdAsc(18L)).thenReturn(List.of(
                new SessionCommunityShare(session,direct),
                new SessionCommunityShare(session,another)));
        var data=controller.context(18L,auth);
        assertEquals(List.of("Pádel Amigos","Futsal Lisboa"),
                data.communities().stream().map(SessionContextController.CommunityLabel::name).toList());
        assertTrue(data.seasonGame());
        assertEquals("Época 2026/27",data.seasonName());
        verify(service).getById(18L,"organizer");
    }

    @Test void sharedOnlyCommunityIsVisibleEvenWhenSessionHasNoDirectCommunity() {
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("member");
        Session session=new Session();session.setId(19L);
        Community group=new Community();group.setId(4L);group.setName("Amigos da Bola");
        when(sessions.findById(19L)).thenReturn(Optional.of(session));
        when(shares.findBySession_IdOrderByIdAsc(19L))
                .thenReturn(List.of(new SessionCommunityShare(session,group)));
        var data=controller.context(19L,auth);
        assertEquals("Amigos da Bola",data.communities().getFirst().name());
        assertFalse(data.seasonGame());
        assertNull(data.seasonName());
    }

    @Test void refusesUnauthorizedContextBeforeReadingSessionOrGroups() {
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("outsider");
        when(service.getById(12L,"outsider")).thenThrow(
                new org.springframework.security.access.AccessDeniedException("private"));
        assertThrows(org.springframework.security.access.AccessDeniedException.class,
                ()->controller.context(12L,auth));
        verifyNoInteractions(sessions,shares);
        assertEquals(401,assertThrows(ResponseStatusException.class,
                ()->controller.context(12L,null)).getStatusCode().value());
    }
}
