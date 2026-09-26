package com.example.desporto24.mapper;

import com.example.desporto24.encryption.AES;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.security.PasswordHashService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileMapperImplTest {

    private final ProfileRepository profileRepository = mock(ProfileRepository.class);
    private final PasswordHashService passwordHashService = mock(PasswordHashService.class);
    private final ProfileMapperImpl mapper = new ProfileMapperImpl(profileRepository, passwordHashService);

    @Test
    void newProfilesStoreBcryptPasswordInsteadOfLegacyAesPassword() {
        ProfileDTO dto = new ProfileDTO();
        dto.setUsername("miguel");
        dto.setPassword("plain-password");
        dto.setPhone("912345678");
        dto.setAddress("Lisboa");

        when(passwordHashService.encode("plain-password")).thenReturn("$2a$bcrypt-hash");

        Profile profile = mapper.toEntity(dto);

        assertEquals("$2a$bcrypt-hash", profile.getPassword());
        verify(passwordHashService).encode("plain-password");
    }

    @Test
    void profileDtoNeverExposesStoredPassword() {
        Profile profile = new Profile();
        profile.setUserId("1234567890");
        profile.setUsername("miguel");
        profile.setPassword("$2a$bcrypt-hash");
        profile.setPhone(AES.encryptString("912345678", profile.getUserId()));
        profile.setAddress(AES.encryptString("Lisboa", profile.getUserId()));

        ProfileDTO dto = mapper.toDto(profile);

        assertNull(dto.getPassword());
        assertEquals("912345678", dto.getPhone());
        assertEquals("Lisboa", dto.getAddress());
    }
}
