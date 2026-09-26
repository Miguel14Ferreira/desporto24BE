package com.example.desporto24.security;

import com.example.desporto24.encryption.AES;
import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordHashServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    private BCryptPasswordEncoder encoder;
    private PasswordHashService service;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder();
        service = new PasswordHashService(profileRepository, encoder);
    }

    @Test
    void matchesBcryptWithoutRewritingIt() {
        Profile profile = new Profile();
        profile.setPassword(encoder.encode("secret"));

        assertTrue(service.matchesAndUpgrade(profile, "secret"));
        verifyNoInteractions(profileRepository);
    }

    @Test
    void rejectsWrongBcryptPassword() {
        Profile profile = new Profile();
        profile.setPassword(encoder.encode("secret"));

        assertFalse(service.matchesAndUpgrade(profile, "wrong"));
        verifyNoInteractions(profileRepository);
    }

    @Test
    void upgradesLegacyAesPasswordAfterSuccessfulLogin() {
        Profile profile = new Profile();
        profile.setUserId("1234567890");
        profile.setPassword(AES.encryptString("secret", profile.getUserId()));

        assertTrue(service.matchesAndUpgrade(profile, "secret"));

        assertTrue(profile.getPassword().startsWith("$2"));
        assertTrue(encoder.matches("secret", profile.getPassword()));
        verify(profileRepository).save(profile);
    }
}
