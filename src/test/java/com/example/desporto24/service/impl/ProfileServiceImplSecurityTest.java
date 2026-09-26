package com.example.desporto24.service.impl;

import com.example.desporto24.mapper.ProfileMapper;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.profile.ChangePasswordRequest;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.security.PasswordHashService;
import com.example.desporto24.service.EmailService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProfileServiceImplSecurityTest {

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
