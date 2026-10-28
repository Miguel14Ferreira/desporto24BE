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
        verifyNoInteractions(notificationsRepository);
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
                eq("Alteração de email | Email address changed — Jynlo"), html.capture());
        verify(emailService).send(eq("new@example.test"),
                eq("Alteração de email | Email address changed — Jynlo"), eq(html.getValue()));
        verifyNoMoreInteractions(emailService);
        org.mockito.ArgumentCaptor<com.example.desporto24.register.Notifications.Notifications> notice =
                org.mockito.ArgumentCaptor.forClass(com.example.desporto24.register.Notifications.Notifications.class);
        verify(notificationsRepository).save(notice.capture());
        assertEquals("Alteração de email", notice.getValue().getSubject());
        assertEquals(true, notice.getValue().isUpdateProfile());
        assertEquals("miguel@example.com", mapped.getOldEmail());
        org.junit.jupiter.api.Assertions.assertTrue(html.getValue().contains("Alteração de email"));
        org.junit.jupiter.api.Assertions.assertTrue(html.getValue().contains("Email address changed"));
        org.junit.jupiter.api.Assertions.assertTrue(html.getValue().contains("Jynlo"));
        org.junit.jupiter.api.Assertions.assertFalse(html.getValue().contains("Desporto24"));
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
        org.mockito.ArgumentCaptor<com.example.desporto24.register.Notifications.Notifications> alert =
                org.mockito.ArgumentCaptor.forClass(com.example.desporto24.register.Notifications.Notifications.class);
        verify(notificationsRepository).save(alert.capture());
        assertEquals("Palavra-passe alterada", alert.getValue().getSubject());
        org.mockito.ArgumentCaptor<String> html = org.mockito.ArgumentCaptor.forClass(String.class);
        verify(emailService).send(eq("fixture@example.test"),
                eq("Palavra-passe alterada | Password changed — Jynlo"), html.capture());
        org.junit.jupiter.api.Assertions.assertTrue(html.getValue().contains("Password changed"));
        org.junit.jupiter.api.Assertions.assertTrue(html.getValue().contains("Redefinir palavra-passe / Reset password"));
        org.junit.jupiter.api.Assertions.assertTrue(html.getValue().contains("href=\"https://example.test/login/resetPassword\""));
        org.junit.jupiter.api.Assertions.assertTrue(html.getValue().contains("If you made this change"));
    }

    @Test
    void uploadingProfilePhotoDoesNotSendRoutineNotification() {
        ProfileRepository repository = mock(ProfileRepository.class);
        ProfileMapper mapper = mock(ProfileMapper.class);
        EmailService mail = mock(EmailService.class);
        NotificationsRepository notifications = mock(NotificationsRepository.class);
        Profile profile = new Profile();
        profile.setId(3L);
        profile.setUsername("guest");
        when(repository.findUserByUsername("guest")).thenReturn(Optional.of(profile));
        when(repository.save(profile)).thenReturn(profile);

        ProfileServiceImpl service = new ProfileServiceImpl(repository, mapper, mail, notifications,
                mock(PasswordHashService.class), mock(UserBlockService.class));
        com.example.desporto24.storage.MediaStorage storage =
                mock(com.example.desporto24.storage.MediaStorage.class);
        org.springframework.web.multipart.MultipartFile photo =
                mock(org.springframework.web.multipart.MultipartFile.class);
        when(photo.isEmpty()).thenReturn(false);
        when(storage.store(anyString(), same(photo)))
                .thenReturn(java.net.URI.create("https://example.test/profile.jpg"));
        org.springframework.test.util.ReflectionTestUtils.setField(service, "mediaStorage", storage);

        service.updateMyProfilePhoto("guest", photo);

        verify(repository).save(profile);
        verifyNoInteractions(mail, notifications);
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
