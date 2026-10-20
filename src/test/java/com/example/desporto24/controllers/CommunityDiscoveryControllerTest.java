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
    void enrichesOnlyOnePageAndKeepsLegacySportsAsFallback() {
        var multi = new com.example.desporto24.model.dto.community.CommunityDiscoveryResponse(
                2L, "Múltipla", null, "Futebol", null,
                com.example.desporto24.enumeration.CommunityVisibility.PUBLIC);
        var legacy = new com.example.desporto24.model.dto.community.CommunityDiscoveryResponse(
                4L, "Legada", null, "Padel", null,
                com.example.desporto24.enumeration.CommunityVisibility.PUBLIC);
        when(repository.discoverPublicPresentations(PageRequest.of(0, 20)))
                .thenReturn(new SliceImpl<>(List.of(multi, legacy)));
        var entity = com.example.desporto24.model.community.Community.builder()
                .id(2L).sport("Futebol")
                .sports(new java.util.ArrayList<>(List.of("Futebol", "Ténis", "Padel"))).build();
        when(repository.findAllWithSportsByIdIn(List.of(2L, 4L)))
                .thenReturn(List.of(entity));

        var response = controller.search("", "", 0);
        assertEquals(List.of("Múltipla", "Legada"),
                response.communities().stream().map(item -> item.name()).toList());
        assertEquals(List.of("Futebol", "Ténis", "Padel"),
                response.communities().get(0).sports());
        assertEquals(List.of("Padel"), response.communities().get(1).sports());
        verify(repository).findAllWithSportsByIdIn(List.of(2L, 4L));
    }

    @Test
    void presentationIncludesAllSportsWithoutExposingPrivateCommunityData() {
        var group = com.example.desporto24.model.community.Community.builder()
                .id(42L).name("Comunidade").sport("Futebol")
                .sports(new java.util.ArrayList<>(List.of("Futebol", "Padel")))
                .visibility(com.example.desporto24.enumeration.CommunityVisibility.PRIVATE).build();
        when(repository.findWithSportsById(42L)).thenReturn(java.util.Optional.of(group));

        var result = controller.presentation(42L);
        assertEquals(List.of("Futebol", "Padel"), result.sports());
        assertEquals(com.example.desporto24.enumeration.CommunityVisibility.PRIVATE, result.visibility());
        verify(repository).findWithSportsById(42L);
    }

    @Test
    void presentationWithUnknownCommunityStillReturnsNotFound() {
        when(repository.findWithSportsById(99L)).thenReturn(java.util.Optional.empty());
        var error = assertThrows(ResponseStatusException.class, () -> controller.presentation(99L));
        assertEquals(org.springframework.http.HttpStatus.NOT_FOUND, error.getStatusCode());
    }

    @Test
    void rejectsNegativePages() {
        assertThrows(ResponseStatusException.class, () -> controller.search("", "", -1));
        verifyNoInteractions(repository);
    }
}
