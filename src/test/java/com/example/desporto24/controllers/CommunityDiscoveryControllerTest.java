package com.example.desporto24.controllers;

import com.example.desporto24.repository.community.CommunityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommunityDiscoveryControllerTest {
    private final CommunityRepository repository = mock(CommunityRepository.class);
    private final CommunityDiscoveryController controller = new CommunityDiscoveryController(repository);

    @Test
    void emptyFiltersBrowseOnlyThePublicProjection() {
        when(repository.discoverPublicPresentations(PageRequest.of(0, 20)))
                .thenReturn(new SliceImpl<>(List.of()));
        var result = controller.search("", "  ", 0);
        assertTrue(result.communities().isEmpty());
        verify(repository).discoverPublicPresentations(PageRequest.of(0, 20));
        verify(repository, never()).searchPresentations(anyString(), anyString(), any());
    }

    @Test
    void explicitQueryStillUsesTheExistingSearch() {
        when(repository.searchPresentations("futebol", "", PageRequest.of(0, 20)))
                .thenReturn(new SliceImpl<>(List.of()));
        controller.search(" Futebol ", "", 0);
        verify(repository).searchPresentations("futebol", "", PageRequest.of(0, 20));
        verify(repository, never()).discoverPublicPresentations(any());
    }

    @Test
    void rejectsNegativePages() {
        assertThrows(ResponseStatusException.class, () -> controller.search("", "", -1));
        verifyNoInteractions(repository);
    }
}
