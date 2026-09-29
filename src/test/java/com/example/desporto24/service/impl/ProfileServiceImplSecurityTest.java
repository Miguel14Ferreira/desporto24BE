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
import org.junit.jupiter.api.Test;

import java.util.Optional;

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
                passwordHashService
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

        Profile profile = new Profile();
        profile.setUsername("miguel");
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
                passwordHashService
        );

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
    }
}
