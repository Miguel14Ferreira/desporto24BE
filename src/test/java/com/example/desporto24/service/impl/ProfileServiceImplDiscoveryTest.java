package com.example.desporto24.service.impl;

import com.example.desporto24.mapper.ProfileMapper;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.security.PasswordHashService;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.service.UserBlockService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProfileServiceImplDiscoveryTest {

    private static Profile profile(Long id, String username) {
        Profile p = new Profile();
        p.setId(id);
        p.setUsername(username);
        return p;
    }

    @Test
    void automaticDiscoveryExcludesOnlyKnownQaUsernamesWithoutDeletingOrBlockingTheirAccounts() {
        ProfileRepository repository = mock(ProfileRepository.class);
        ProfileMapper mapper = mock(ProfileMapper.class);
        UserBlockService blocks = mock(UserBlockService.class);
        Profile actor = profile(1L, "miguel");
        Profile qaOne = profile(2L, "jynloteste01");
        Profile qaTwo = profile(3L, "JYNLOTESTE02");
        Profile realPlayer = profile(4L, "diogo");
        Profile similarRealPlayer = profile(5L, "jynloteste03");

        when(repository.findUserByUsernameIgnoreCase("miguel")).thenReturn(Optional.of(actor));
        when(blocks.blockedProfileIds(actor)).thenReturn(Set.of());
        when(repository.discoverProfiles("miguel", PageRequest.of(0, 100)))
                .thenReturn(List.of(qaOne, qaTwo, realPlayer, similarRealPlayer));
        when(mapper.toDto(any(Profile.class))).thenAnswer(invocation -> {
            Profile p = invocation.getArgument(0);
            ProfileDTO dto = new ProfileDTO();
            dto.setUsername(p.getUsername());
            return dto;
        });

        ProfileServiceImpl service = new ProfileServiceImpl(
                repository, mapper,
                mock(EmailService.class), mock(NotificationsRepository.class),
                mock(PasswordHashService.class), blocks);

        var suggested = service.discoverPublicProfiles("miguel");
        assertEquals(Set.of("diogo", "jynloteste03"),
                suggested.stream().map(item -> item.username()).collect(java.util.stream.Collectors.toSet()));
        verify(mapper, never()).toDto(qaOne);
        verify(mapper, never()).toDto(qaTwo);
        verify(repository, never()).save(any(Profile.class));
        verify(repository, never()).delete(any(Profile.class));
    }
}
