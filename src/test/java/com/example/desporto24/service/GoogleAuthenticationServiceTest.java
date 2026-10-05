package com.example.desporto24.service;

import com.example.desporto24.mapper.ProfileMapper;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.auth.GoogleAuthResponse;
import com.example.desporto24.model.dto.auth.GoogleRegistrationRequest;
import com.example.desporto24.register.MFA.MFAVerificationService;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.security.GoogleIdentityService;
import com.example.desporto24.security.PasswordHashService;
import com.example.desporto24.utility.JWTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.lenient;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoogleAuthenticationServiceTest {

    @Mock private GoogleIdentityService googleIdentityService;
    @Mock private ProfileRepository profileRepository;
    @Mock private ProfileMapper profileMapper;
    @Mock private PasswordHashService passwordHashService;
    @Mock private JWTokenProvider jwtTokenProvider;
    @Mock private MFAVerificationService mfaVerificationService;
    @Mock private NotificationsRepository notificationsRepository;
    @Mock private PilotEventService pilotEventService;
    @Mock private ReferenceCatalogService referenceCatalogService;

    @Test
    void googleRegistrationPersistsLegalAcceptanceEvidence() {
        GoogleRegistrationRequest request = new GoogleRegistrationRequest(
                "google-credential",
                "fixture-user",
                "Fixture",
                "1995-05-30",
                "Portugal",
                null,
                null,
                "outro",
                true
        );
        GoogleIdentityService.GoogleIdentity identity =
                new GoogleIdentityService.GoogleIdentity(
                        "google-subject",
                        "fixture@example.test",
                        "Fixture User",
                        null
                );

        when(googleIdentityService.verify("google-credential")).thenReturn(identity);
        when(profileRepository.findByGoogleSubject("google-subject")).thenReturn(Optional.empty());
        when(profileRepository.findUserByEmail("fixture@example.test")).thenReturn(Optional.empty());
        when(profileRepository.findUserByUsernameIgnoreCase("fixture-user")).thenReturn(Optional.empty());
        when(passwordHashService.encode(anyString())).thenReturn("encoded-password");
        when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtTokenProvider.generateJwtToken(any())).thenReturn("jwt-token");
        when(profileMapper.toDto(any(Profile.class))).thenReturn(new ProfileDTO());

        GoogleAuthResponse response = service().completeRegistration(request, "pilot-google");

        assertEquals("jwt-token", response.token());
        verify(profileRepository, atLeastOnce()).save(argThat(profile ->
                profile.getTermsAcceptedAt() != null
                        && "2026-10-01".equals(profile.getTermsVersion())
                        && "2026-10-01".equals(profile.getPrivacyVersion())
        ));
    }

    private GoogleAuthenticationService service() {
        lenient().when(referenceCatalogService.isAllowedOrBlank(
                eq(ReferenceCatalogService.GENDER),
                any()
        )).thenReturn(true);

        return new GoogleAuthenticationService(
                googleIdentityService,
                profileRepository,
                profileMapper,
                passwordHashService,
                jwtTokenProvider,
                mfaVerificationService,
                notificationsRepository,
                pilotEventService,
                referenceCatalogService
        );
    }
}
