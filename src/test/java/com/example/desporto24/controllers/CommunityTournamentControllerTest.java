package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.community.CommunityTournamentPageResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentParticipantResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentParticipantsPageResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentResponse;
import com.example.desporto24.model.dto.community.CreateCommunityTournamentRequest;
import com.example.desporto24.service.CommunityTournamentService;
import com.example.desporto24.service.CommunityTournamentScheduleService;
import com.example.desporto24.model.dto.community.CommunityTournamentScheduleSummaryResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentMatchesPageResponse;
import com.example.desporto24.enumeration.TournamentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommunityTournamentControllerTest {
    private final CommunityTournamentService service = mock(CommunityTournamentService.class);
    private final CommunityTournamentScheduleService scheduleService = mock(CommunityTournamentScheduleService.class);
    private final CommunityTournamentController controller = new CommunityTournamentController(service, scheduleService);
    private final Authentication authentication = mock(Authentication.class);

    @Test
    void scheduleGenerationUsesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("owner");
        var summary = new CommunityTournamentScheduleSummaryResponse(
                9L, TournamentStatus.IN_PROGRESS, 4, 3, 6);
        when(scheduleService.generate(7L, 9L, "owner")).thenReturn(summary);
        assertSame(summary, controller.generateSchedule(7L, 9L, authentication).getBody());
        verify(scheduleService).generate(7L, 9L, "owner");
    }

    @Test
    void pagedMatchLookupDelegatesToScheduleService() {
        when(authentication.getName()).thenReturn("ana");
        var page = new CommunityTournamentMatchesPageResponse(List.of(), 2, false);
        when(scheduleService.matches(7L, 9L, "ana", 2)).thenReturn(page);
        assertSame(page, controller.matches(7L, 9L, 2, authentication).getBody());
        verify(scheduleService).matches(7L, 9L, "ana", 2);
    }

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

    @Test
    void selfEnrollmentUsesAuthenticatedProfile() {
        when(authentication.getName()).thenReturn("ana");
        var registered = new CommunityTournamentParticipantResponse(5L, "ana", "Ana", null);
        when(service.join(7L, 9L, "ana")).thenReturn(registered);
        var response = controller.join(7L, 9L, authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(registered, response.getBody());
        verify(service).join(7L, 9L, "ana");
    }

    @Test
    void confirmationUsesAuthenticatedOrganizerAndRequestedPlayer() {
        when(authentication.getName()).thenReturn("owner");
        var confirmed = new CommunityTournamentParticipantResponse(6L, "rui", "Rui", null);
        when(service.confirmParticipant(7L, 9L, 6L, "owner")).thenReturn(confirmed);
        var response = controller.confirmParticipant(7L, 9L, 6L, authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(confirmed, response.getBody());
        verify(service).confirmParticipant(7L, 9L, 6L, "owner");
    }

    @Test
    void withdrawalOnlySuppliesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("ana");
        var response = controller.leave(7L, 9L, authentication);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).leave(7L, 9L, "ana");
    }

    @Test
    void organizerRemovalDelegatesToAuthorizedService() {
        when(authentication.getName()).thenReturn("owner");
        var response = controller.removeParticipant(7L, 9L, 5L, authentication);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).removeParticipant(7L, 9L, 5L, "owner");
    }

    @Test
    void pagedRosterUsesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("ana");
        var expected = new CommunityTournamentParticipantsPageResponse(List.of(), 1, false);
        when(service.participants(7L, 9L, "ana", 1)).thenReturn(expected);
        var response = controller.participants(7L, 9L, 1, authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(expected, response.getBody());
    }
}
