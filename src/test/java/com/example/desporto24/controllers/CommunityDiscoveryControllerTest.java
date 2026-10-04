package com.example.desporto24.controllers;

import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.model.dto.community.CommunityDiscoveryResponse;
import com.example.desporto24.enumeration.CommunityVisibility;
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

    @Test void blankFiltersDoNotListEveryCommunity() {
        assertTrue(controller.search(" ", "", 0).communities().isEmpty());
        verifyNoInteractions(repository);
    }

    @Test void includesPrivatePresentationsAndEscapesLiteralWildcards() {
        var preview = new CommunityDiscoveryResponse(5L, "CNG", "Futsal semanal",
                "Futebol", null, CommunityVisibility.PRIVATE);
        when(repository.searchPresentations("c!%!_!!", "futebol", PageRequest.of(0, 20)))
                .thenReturn(new SliceImpl<>(List.of(preview), PageRequest.of(0, 20), false));
        var result = controller.search(" C%_! ", " FUTEBOL ", 0);
        assertEquals(List.of(preview), result.communities());
        assertFalse(result.hasMore());
    }

    @Test void sportAloneCanSearchAndReportsNextPage() {
        when(repository.searchPresentations("", "futsal", PageRequest.of(1, 20)))
                .thenReturn(new SliceImpl<>(List.of(), PageRequest.of(1, 20), true));
        assertTrue(controller.search("", "Futsal", 1).hasMore());
    }

    @Test void rejectsNegativePageAndOversizedFilters() {
        assertThrows(ResponseStatusException.class, () -> controller.search("CNG", "", -1));
        assertThrows(ResponseStatusException.class, () -> controller.search("a".repeat(101), "", 0));
        assertThrows(ResponseStatusException.class, () -> controller.search("", "a".repeat(101), 0));
        verifyNoInteractions(repository);
    }
}
