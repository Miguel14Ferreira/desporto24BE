package com.example.desporto24.service.impl;

import com.example.desporto24.encryption.AES;
import com.example.desporto24.mapper.ProfileMapper;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.profile.ChangePasswordRequest;
import com.example.desporto24.model.dto.profile.UpdateProfileRequest;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.security.PasswordHashService;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.service.UserBlockService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

class ProfileServiceImplSecurityTest {

    @Test
    void profileUpdateKeepsEncryptedFields() {
        ProfileRepository repository = mock(ProfileRepository.class);
        ProfileMapper mapper = mock(ProfileMapper.class);
        EmailService emailService = mock(EmailService.class);
        NotificationsRepository notificationsRepository = mock(NotificationsRepository.class);
        PasswordHashService passwordHashService = mock(PasswordHashService.class);
        UserBlockService userBlockService = mock(UserBlockService.class);

        Profile profile = new Profile();
        profile.setId(1L);
        profile.setUserId("1234567890");
        profile.setUsername("miguel");
        profile.setEmail("miguel@example.com");
        profile.setPhone(AES.encryptString("912345678", profile.getUserId()));
        profile.setAddress(AES.encryptString("Rua Antiga", profile.getUserId()));

        when(repository.findUserByUsername("miguel")).thenReturn(Optional.of(profile));
        when(repository.findAll()).thenReturn(java.util.List.of(profile));
        when(repository.save(profile)).thenReturn(profile);

        ProfileDTO mapped = new ProfileDTO();
        mapped.setUsername("miguel");
        mapped.setEmail("miguel@example.com");
        when(mapper.toDto(profile)).thenReturn(mapped);

        ProfileServiceImpl service = new ProfileServiceImpl(
                repository,
                mapper,
                emailService,
                notificationsRepository,
                passwordHashService,
                userBlockService
        );

        UpdateProfileRequest request = new UpdateProfileRequest(
                "Miguel Ferreira",
                "Miguel",
                "1995-05-30",
                "Rua Nova",
                "Portugal",
                "Lisboa",
                "1000-001",
                "351",
                "923456789",
                "masculino",
                "miguel@example.com",
                "Futebol",
                false
        );

        service.updateMyProfile("miguel", request);

        assertNotEquals("923456789", profile.getPhone());
        assertNotEquals("Rua Nova", profile.getAddress());
        assertEquals("923456789", AES.decryptString(profile.getPhone(), profile.getUserId()));
        assertEquals("Rua Nova", AES.decryptString(profile.getAddress(), profile.getUserId()));
        verifyNoInteractions(emailService);
    }

    @Test
    void emailChangeNotifiesBothAddressesWithBilingualRecoveryEmail() {
        ProfileRepository repository = mock(ProfileRepository.class);
        ProfileMapper mapper = mock(ProfileMapper.class);
        EmailService emailService = mock(EmailService.class);
        NotificationsRepository notificationsRepository = mock(NotificationsRepository.class);
        PasswordHashService passwordHashService = mock(PasswordHashService.class);
        UserBlockService userBlockService = mock(UserBlockService.class);

        Profile profile = new Profile();
        profile.setId(1L);
        profile.setUserId("1234567890");
        profile.setUsername("miguel");
        profile.setEmail("miguel@example.com");
        profile.setPhone(AES.encryptString("912345678", profile.getUserId()));
        profile.setAddress(AES.encryptString("Rua Antiga", profile.getUserId()));

        when(repository.findUserByUsername("miguel")).thenReturn(Optional.of(profile));
        when(repository.findAll()).thenReturn(java.util.List.of(profile));
        when(repository.save(profile)).thenReturn(profile);

        ProfileDTO mapped = new ProfileDTO();
        mapped.setUsername("miguel");
        mapped.setEmail("new@example.test");
        when(mapper.toDto(profile)).thenReturn(mapped);

        ProfileServiceImpl service = new ProfileServiceImpl(
                repository,
                mapper,
                emailService,
                notificationsRepository,
                passwordHashService,
                userBlockService
        );

        UpdateProfileRequest request = new UpdateProfileRequest(
                "Miguel Ferreira",
                "Miguel",
                "1995-05-30",
                "Rua Nova",
                "Portugal",
                "Lisboa",
                "1000-001",
                "351",
                "923456789",
                "masculino",
                "new@example.test",
                "Futebol",
                false
        );

        org.springframework.test.util.ReflectionTestUtils.setField(service, "uiAppUrl", "https://example.test/");
        service.updateMyProfile("miguel", request);

        org.mockito.ArgumentCaptor<String> html = org.mockito.ArgumentCaptor.forClass(String.class);
        verify(emailService).send(eq("miguel@example.com"),
                eq("Alteração de email | Email address changed — Desporto24"), html.capture());
        verify(emailService).send(eq("new@example.test"),
                eq("Alteração de email | Email address changed — Desporto24"), eq(html.getValue()));
        verifyNoMoreInteractions(emailService);
        assertEquals("miguel@example.com", mapped.getOldEmail());
        org.junit.jupiter.api.Assertions.assertTrue(html.getValue().contains("Alteração de email"));
        org.junit.jupiter.api.Assertions.assertTrue(html.getValue().contains("Email address changed"));
        org.junit.jupiter.api.Assertions.assertTrue(html.getValue().contains("Redefinir palavra-passe / Reset password"));
        org.junit.jupiter.api.Assertions.assertTrue(html.getValue().contains("href=\"https://example.test/login/resetPassword\""));
        org.junit.jupiter.api.Assertions.assertTrue(html.getValue().contains("If you made this change"));
        assertNotEquals("923456789", profile.getPhone());
        assertNotEquals("Rua Nova", profile.getAddress());
        assertEquals("923456789", AES.decryptString(profile.getPhone(), profile.getUserId()));
        assertEquals("Rua Nova", AES.decryptString(profile.getAddress(), profile.getUserId()));
    }

    @Test
    void changingPasswordInvalidatesExistingJwtVersion() {
        ProfileRepository repository =
                mock(ProfileRepository.class);
        ProfileMapper mapper =
                mock(ProfileMapper.class);
        EmailService emailService =
                mock(EmailService.class);
        NotificationsRepository notificationsRepository =
                mock(NotificationsRepository.class);
        PasswordHashService passwordHashService =
                mock(PasswordHashService.class);
        UserBlockService userBlockService =
                mock(UserBlockService.class);

        Profile profile = new Profile();
        profile.setUsername("miguel");
        profile.setEmail("fixture@example.test");
        profile.setPassword("old-hash");
        profile.setTokenVersion(8L);

        when(repository.findUserByUsername("miguel"))
                .thenReturn(Optional.of(profile));
        when(passwordHashService.matchesAndUpgrade(
                profile,
                "current-password"))
                .thenReturn(true);
        when(passwordHashService.encode("new-password"))
                .thenReturn("new-hash");

        ProfileServiceImpl service = new ProfileServiceImpl(
                repository,
                mapper,
                emailService,
                notificationsRepository,
                passwordHashService,
                userBlockService
        );

        org.springframework.test.util.ReflectionTestUtils.setField(service, "uiAppUrl", "https://example.test/");

        service.changeMyPassword(
                "miguel",
                new ChangePasswordRequest(
                        "current-password",
                        "new-password"
                )
        );

        assertEquals(9L, profile.getTokenVersion());
        assertEquals("new-hash", profile.getPassword());
        verify(repository).save(profile);
        org.mockito.ArgumentCaptor<String> html = org.mockito.ArgumentCaptor.forClass(String.class);
        verify(emailService).send(eq("fixture@example.test"),
                eq("Palavra-passe alterada | Password changed — Desporto24"), html.capture());
        org.junit.jupiter.api.Assertions.assertTrue(html.getValue().contains("Password changed"));
        org.junit.jupiter.api.Assertions.assertTrue(html.getValue().contains("Redefinir palavra-passe / Reset password"));
        org.junit.jupiter.api.Assertions.assertTrue(html.getValue().contains("href=\"https://example.test/login/resetPassword\""));
        org.junit.jupiter.api.Assertions.assertTrue(html.getValue().contains("If you made this change"));
    }

    @Test
    void publicSearchNormalizesMissingFiltersBeforeRepositoryQuery() {
        ProfileRepository repository = mock(ProfileRepository.class);
        ProfileMapper mapper = mock(ProfileMapper.class);
        EmailService emailService = mock(EmailService.class);
        NotificationsRepository notificationsRepository = mock(NotificationsRepository.class);
        PasswordHashService passwordHashService = mock(PasswordHashService.class);
        UserBlockService userBlockService = mock(UserBlockService.class);

        Profile actor = new Profile();
        actor.setUsername("miguel");

        when(repository.findUserByUsernameIgnoreCase("miguel"))
                .thenReturn(Optional.of(actor));
        when(repository.searchPublicCandidates(
                eq("miguel"),
                eq(""),
                eq(""),
                eq("lisboa"),
                eq(""),
                any(Pageable.class)
        )).thenReturn(List.of());

        ProfileServiceImpl service = new ProfileServiceImpl(
                repository,
                mapper,
                emailService,
                notificationsRepository,
                passwordHashService,
                userBlockService
        );

        service.searchPublicProfiles(
                "miguel",
                null,
                "   ",
                " Lisboa ",
                null,
                null,
                null
        );

        verify(repository).searchPublicCandidates(
                eq("miguel"),
                eq(""),
                eq(""),
                eq("lisboa"),
                eq(""),
                any(Pageable.class)
        );
    }

}
