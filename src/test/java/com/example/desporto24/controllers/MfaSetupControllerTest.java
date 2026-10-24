package com.example.desporto24.controllers;

import com.example.desporto24.encryption.AES;
import com.example.desporto24.mapper.ProfileMapper;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.auth.MfaSetupConfirmRequest;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.security.TotpService;
import com.example.desporto24.security.MfaSettingsAttemptLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.example.desporto24.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MfaSetupControllerTest {

    @Mock private ProfileRepository profileRepository;
    @Mock private ProfileMapper profileMapper;
    @Mock private TotpService totpService;
    @Mock private MfaSettingsAttemptLimiter mfaSettingsAttemptLimiter;
    @Mock private EmailService emailService;
    @Mock private NotificationsRepository notificationsRepository;
    @Mock private Authentication authentication;

    @InjectMocks
    private MfaSetupController controller;

    @Test
    void disableRejectsInvalidAuthenticatorCode() {
        Profile profile = activeMfaProfile();
        when(authentication.getName()).thenReturn("miguel");
        when(profileRepository.findUserByUsername("miguel"))
                .thenReturn(Optional.of(profile));
        when(totpService.verify("TEST-SECRET", "123456"))
                .thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> controller.disable(
                        new MfaSetupConfirmRequest("123456"),
                        authentication)
        );

        assertTrue(profile.getMFA());
        assertNotNull(profile.getMfaSecret());
        verify(profileRepository, never()).save(any(Profile.class));
        verify(mfaSettingsAttemptLimiter).reserveAttempt("miguel");
        verify(mfaSettingsAttemptLimiter, never()).resetAfterSuccess(anyString());
    }

    @Test
    void disableRequiresValidAuthenticatorCode() {
        Profile profile = activeMfaProfile();
        ProfileDTO dto = new ProfileDTO();
        dto.setUsername("miguel");

        when(authentication.getName()).thenReturn("miguel");
        when(profileRepository.findUserByUsername("miguel"))
                .thenReturn(Optional.of(profile));
        when(totpService.verify("TEST-SECRET", "654321"))
                .thenReturn(true);
        when(profileRepository.save(profile)).thenReturn(profile);
        when(profileMapper.toDto(profile)).thenReturn(dto);

        var response = controller.disable(
                new MfaSetupConfirmRequest("654321"),
                authentication
        );

        assertEquals(200, response.getStatusCode().value());
        assertFalse(profile.getMFA());
        assertNull(profile.getMfaSecret());
        verify(profileRepository).save(profile);
        verify(mfaSettingsAttemptLimiter).reserveAttempt("miguel");
        verify(mfaSettingsAttemptLimiter).resetAfterSuccess("miguel");
    }


    @Test
    void activeMfaCannotBeResetWithOnlyAnAccessToken() {
        Profile profile = activeMfaProfile();
        String originalEncryptedSecret = profile.getMfaSecret();
        when(authentication.getName()).thenReturn("miguel");
        when(profileRepository.findUserByUsername("miguel"))
                .thenReturn(Optional.of(profile));

        ResponseStatusException blocked = assertThrows(ResponseStatusException.class,
                () -> controller.setup(authentication));
        assertEquals(HttpStatus.CONFLICT, blocked.getStatusCode());
        assertTrue(profile.getMFA());
        assertEquals(originalEncryptedSecret, profile.getMfaSecret());
        verify(profileRepository, never()).save(any(Profile.class));
        verifyNoInteractions(totpService, mfaSettingsAttemptLimiter);
    }

    @Test
    void firstTimeMfaSetupStillWorksForUnprotectedAccount() {
        Profile profile = new Profile();
        profile.setUsername("miguel");
        profile.setUserId("1234567890");
        profile.setMFA(false);
        when(authentication.getName()).thenReturn("miguel");
        when(profileRepository.findUserByUsername("miguel"))
                .thenReturn(Optional.of(profile));
        when(totpService.generateSecret()).thenReturn("JBSWY3DPEHPK3PXP");
        when(totpService.provisioningUri("miguel", "JBSWY3DPEHPK3PXP"))
                .thenReturn("otpauth://totp/fixture");
        when(totpService.qrCodeDataUrl("otpauth://totp/fixture"))
                .thenReturn("data:image/png;base64,fixture");

        var response = controller.setup(authentication);

        assertEquals(200, response.getStatusCode().value());
        assertFalse(profile.getMFA());
        assertNotNull(profile.getMfaSecret());
        verify(profileRepository).save(profile);
        verifyNoInteractions(mfaSettingsAttemptLimiter);
    }

    @Test
    void aBlockedMfaDisableNeverTestsTotpOrModifiesAccount() {
        Profile profile = activeMfaProfile();
        when(authentication.getName()).thenReturn("miguel");
        when(profileRepository.findUserByUsername("miguel"))
                .thenReturn(Optional.of(profile));
        doThrow(new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS))
                .when(mfaSettingsAttemptLimiter).reserveAttempt("miguel");

        var error = assertThrows(ResponseStatusException.class,
                () -> controller.disable(new MfaSetupConfirmRequest("123456"), authentication));
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, error.getStatusCode());
        assertTrue(profile.getMFA());
        verifyNoInteractions(totpService);
        verify(profileRepository, never()).save(any(Profile.class));
    }

    private Profile activeMfaProfile() {
        Profile profile = new Profile();
        profile.setUsername("miguel");
        profile.setUserId("1234567890");
        profile.setMFA(true);
        profile.setMfaSecret(
                AES.encryptString("TEST-SECRET", profile.getUserId())
        );
        return profile;
    }
}
