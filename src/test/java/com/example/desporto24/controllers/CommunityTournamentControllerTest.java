package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.community.CommunityTournamentPageResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentResponse;
import com.example.desporto24.model.dto.community.CreateCommunityTournamentRequest;
import com.example.desporto24.service.CommunityTournamentService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommunityTournamentControllerTest {
    private final CommunityTournamentService service = mock(CommunityTournamentService.class);
    private final CommunityTournamentController controller = new CommunityTournamentController(service);
    private final Authentication authentication = mock(Authentication.class);

    @Test
    void creatingUsesAuthenticatedIdentityAndReturns201() {
        var request = new CreateCommunityTournamentRequest("Liga", null, null, null);
        when(authentication.getName()).thenReturn("owner");
        when(service.create(7L, "owner", request)).thenReturn(
                new CommunityTournamentResponse(19L, "Liga", null, null, 3, 1, 0, null, null, null));
        var response = controller.create(7L, request, authentication);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(19L, response.getBody().id());
        verify(service).create(7L, "owner", request);
    }

    @Test
    void listingUsesPageAndAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("owner");
        var expected = new CommunityTournamentPageResponse(List.of(), 2, false);
        when(service.list(7L, "owner", 2)).thenReturn(expected);
        assertSame(expected, controller.list(7L, 2, authentication).getBody());
        verify(service).list(7L, "owner", 2);
    }
}
