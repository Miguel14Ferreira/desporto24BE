package com.example.desporto24.service;

import com.example.desporto24.model.dto.venue.VenueEditSuggestionRequest;
import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.model.venue.VenueEditSuggestion;
import com.example.desporto24.repository.venue.VenueEditSuggestionRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenueEditSuggestionServiceTest {
    @Mock VenueEditSuggestionRepository submissions;
    @Mock VenueRepository venues;
    private VenueEditSuggestionService service;
    private Venue venue;

    @BeforeEach
    void init() {
        service = new VenueEditSuggestionService(submissions, venues);
        venue = Venue.builder().id(5L).name("Campo Original").city("Lisboa")
                .sports("Futebol").address("Rua Velha").createdAt(Instant.now()).build();
    }

    @Test
    void submissionSavesOnlyDiffAndNeverChangesPublishedVenue() {
        when(venues.findById(5L)).thenReturn(Optional.of(venue));
        when(submissions.save(any())).thenAnswer(inv -> {
            VenueEditSuggestion proposed = inv.getArgument(0);
            proposed.setId(44L);
            return proposed;
        });
        var receipt = service.submit(5L, new VenueEditSuggestionRequest(
                Map.of("name", "Campo Corrigido", "address", "Rua Velha"),
                "O nome correto está na placa da entrada"), "player");

        assertEquals("PENDING", receipt.status());
        assertEquals("Campo Original", receipt.before().get("name"));
        assertEquals("Campo Corrigido", receipt.proposed().get("name"));
        assertEquals(1, receipt.proposed().size());
        assertEquals("Campo Original", venue.getName());
        verify(venues, never()).save(any());
    }

    @Test
    void rejectsFieldsOutsideTheAllowlist() {
        when(venues.findById(5L)).thenReturn(Optional.of(venue));
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.submit(5L, new VenueEditSuggestionRequest(
                    Map.of("informationSource", "OFFICIAL"), "Quero alterar a origem"), "player")
        ).getStatusCode().value());
        verify(submissions, never()).save(any());
    }

    @Test
    void approvalChangesOnlyRequestedFieldsAndPreservesOtherMetadata() {
        var edit = submitted("Campo Original", "Campo Corrigido");
        when(submissions.findForReview(44L)).thenReturn(Optional.of(edit));
        when(venues.findForEditReview(5L)).thenReturn(Optional.of(venue));
        when(submissions.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = service.approve(44L, "Verificado no site da autarquia", "moderator");
        assertEquals("APPROVED", result.status());
        assertEquals("Campo Corrigido", venue.getName());
        assertEquals("Rua Velha", venue.getAddress());
        assertEquals("Futebol", venue.getSports());
        verify(venues).save(venue);
    }

    @Test
    void approvalRefusesToOverwriteNewerPublishedData() {
        var edit = submitted("Campo Original", "Campo Corrigido");
        venue.setName("Outro Nome Confirmado");
        when(submissions.findForReview(44L)).thenReturn(Optional.of(edit));
        when(venues.findForEditReview(5L)).thenReturn(Optional.of(venue));
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.approve(44L, "", "moderator")).getStatusCode().value());
        assertEquals("Outro Nome Confirmado", venue.getName());
        assertEquals(VenueEditSuggestion.Status.PENDING, edit.getStatus());
        verify(venues, never()).save(any());
    }

    @Test
    void rejectionRequiresReasonAndDoesNotChangeVenue() {
        var edit = submitted("Campo Original", "Campo Corrigido");
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.reject(44L, "x", "moderator")).getStatusCode().value());
        when(submissions.findForReview(44L)).thenReturn(Optional.of(edit));
        when(submissions.save(any())).thenAnswer(inv -> inv.getArgument(0));
        var rejected = service.reject(44L, "Dados não confirmados", "moderator");
        assertEquals("REJECTED", rejected.status());
        assertEquals("Dados não confirmados", rejected.decisionNote());
        assertEquals("Campo Original", venue.getName());
        verify(venues, never()).save(any());
    }

    @Test
    void repeatedPendingSuggestionPerUserAndVenueIsBlocked() {
        when(venues.findById(5L)).thenReturn(Optional.of(venue));
        when(submissions.existsByVenue_IdAndSubmittedByUsernameIgnoreCaseAndStatus(
                5L, "player", VenueEditSuggestion.Status.PENDING)).thenReturn(true);
        assertEquals(409, assertThrows(ResponseStatusException.class, () -> service.submit(
                5L, new VenueEditSuggestionRequest(Map.of("name", "Outro campo"),
                "A entrada tem outro nome"), "player")).getStatusCode().value());
    }

    private VenueEditSuggestion submitted(String oldName, String newName) {
        return VenueEditSuggestion.builder().id(44L).venue(venue)
                .beforeJson("{\"name\":\"" + oldName + "\"}")
                .changesJson("{\"name\":\"" + newName + "\"}")
                .reason("A placa apresenta outro nome")
                .submittedByUsername("player").submittedAt(Instant.now())
                .status(VenueEditSuggestion.Status.PENDING).build();
    }
}
