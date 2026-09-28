package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.profile.UpdateProfileRequest;
import com.example.desporto24.model.dto.profile.ChangePasswordRequest;
import com.example.desporto24.model.dto.profile.PublicProfileResponse;

import java.util.List;
import com.example.desporto24.service.interfaces.ProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @Mock
    private ProfileService profileService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ProfileController controller;

    @Test
    void myProfileUsesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("miguel");
        ProfileDTO profile = new ProfileDTO();
        profile.setUsername("miguel");
        when(profileService.findUserByUsername("miguel")).thenReturn(profile);

        var response = controller.getMyProfile(authentication);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("miguel", response.getBody().getUsername());
        verify(profileService).findUserByUsername("miguel");
    }

    @Test
    void publicProfileUsesSafeResponseContract() {
        PublicProfileResponse profile = new PublicProfileResponse(
                "ana",
                "Ana Silva",
                "Aninhas",
                "Portugal",
                "Lisboa",
                "Padel",
                null,
                "2026-09-01"
        );
        when(profileService.findPublicProfile("ana")).thenReturn(profile);

        var response = controller.getUserProfile("ana");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("ana", response.getBody().username());
        assertEquals("Lisboa", response.getBody().location());
        verify(profileService).findPublicProfile("ana");
    }

    @Test
    void publicSearchUsesSafeResponseContract() {
        PublicProfileResponse profile = new PublicProfileResponse(
                "ana",
                "Ana Silva",
                "Portugal",
                "Lisboa",
                "Padel",
                null,
                "2026-09-01"
        );
        when(profileService.searchPublicProfiles("ana")).thenReturn(List.of(profile));

        var response = controller.getProfileByName("ana");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals("ana", response.getBody().get(0).username());
        verify(profileService).searchPublicProfiles("ana");
    }

    @Test
    void changeMyPasswordUsesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("miguel");
        ChangePasswordRequest request = new ChangePasswordRequest(
                "current-password",
                "new-password-123"
        );

        var response = controller.changeMyPassword(request, authentication);

        assertEquals(200, response.getStatusCode().value());
        verify(profileService).changeMyPassword("miguel", request);
    }

    @Test
    void updateMyProfileCannotChooseAnotherUsername() {
        when(authentication.getName()).thenReturn("miguel");
        UpdateProfileRequest request = new UpdateProfileRequest(
                "Miguel Ferreira",
                "Miguel",
                "1990-01-01",
                "Rua Teste",
                "Portugal",
                "Lisboa",
                "1000-000",
                "351",
                "912345678",
                "masculino",
                "miguel@example.com",
                "Padel",
                true
        );
        ProfileDTO updated = new ProfileDTO();
        updated.setUsername("miguel");
        when(profileService.updateMyProfile("miguel", request)).thenReturn(updated);

        var response = controller.updateMyProfile(request, authentication);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("miguel", response.getBody().getUsername());
        verify(profileService).updateMyProfile("miguel", request);
    }
}
