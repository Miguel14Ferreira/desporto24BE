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
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenueEditSuggestionServiceTest {
    @Mock VenueEditSuggestionRepository submissions;
    @Mock VenueRepository venues;
    @Mock VenueEditDecisionNotificationService decisionNotifications;
    private VenueEditSuggestionService service;
    private Venue venue;

    @BeforeEach
    void init() {
        service = new VenueEditSuggestionService(submissions, venues, decisionNotifications);
        venue = Venue.builder().id(5L).name("Campo Original").city("Lisboa")
                .sports("Futebol").address("Rua Velha").createdAt(Instant.now()).build();
    }


    @Test
    void decisionHistoryReturnsOnlyCompletedModerationRecordsAndAttestation() {
        var approved = submitted("Campo Original", "Campo Corrigido");
        approved.setStatus(VenueEditSuggestion.Status.APPROVED);
        approved.setSourceUrl("https://www.agoraporto.pt/piscinas-municipais/piscina-municipal-da-constituicao");
        approved.setSourceReviewConfirmedAt(Instant.parse("2026-10-25T19:00:00Z"));
        approved.setReviewedAt(approved.getSourceReviewConfirmedAt());
        approved.setReviewedByUsername("moderator");
        var rejected = submitted("Campo Original", "Campo Errado");
        rejected.setId(43L);
        rejected.setStatus(VenueEditSuggestion.Status.REJECTED);
        rejected.setSourceReviewConfirmedAt(null);
        rejected.setReviewedAt(Instant.parse("2026-10-25T18:00:00Z"));
        rejected.setReviewedByUsername("moderator");
        when(submissions.findByStatusInOrderByReviewedAtDescIdDesc(
                eq(Set.of(VenueEditSuggestion.Status.APPROVED, VenueEditSuggestion.Status.REJECTED)),
                eq(PageRequest.of(0, 20))))
            .thenReturn(new SliceImpl<>(List.of(approved, rejected), PageRequest.of(0, 20), false));

        var result = service.history(0);
        assertEquals(0, result.page());
        assertFalse(result.hasMore());
        assertEquals(List.of("APPROVED", "REJECTED"),
                result.suggestions().stream().map(x -> x.status()).toList());
        assertEquals("moderator", result.suggestions().getFirst().reviewedByUsername());
        assertEquals(approved.getSourceReviewConfirmedAt(),
                result.suggestions().getFirst().sourceReviewConfirmedAt());
        assertNull(result.suggestions().get(1).sourceReviewConfirmedAt());
        verify(submissions, never()).save(any());
        verify(venues, never()).save(any());
    }

    @Test
    void decisionHistoryRejectsInvalidPageAndDoesNotQueryRepository() {
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.history(-1)).getStatusCode().value());
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.history(1001)).getStatusCode().value());
        verifyNoInteractions(submissions);
    }

    @Test
    void decisionHistorySupportsPaginationWithoutAnyMutation() {
        when(submissions.findByStatusInOrderByReviewedAtDescIdDesc(
                eq(Set.of(VenueEditSuggestion.Status.APPROVED, VenueEditSuggestion.Status.REJECTED)),
                eq(PageRequest.of(1, 20))))
            .thenReturn(new SliceImpl<>(List.of(), PageRequest.of(1, 20), true));
        var result = service.history(1);
        assertEquals(1, result.page());
        assertTrue(result.hasMore());
        assertTrue(result.suggestions().isEmpty());
        verify(submissions, never()).save(any());
    }

    @Test
    void submissionSavesOnlyDiffAndNeverChangesPublishedVenue() {
        when(venues.findForEditReview(5L)).thenReturn(Optional.of(venue));
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
        when(venues.findForEditReview(5L)).thenReturn(Optional.of(venue));
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
        assertNull(result.sourceReviewConfirmedAt());
        assertNull(edit.getSourceReviewConfirmedAt());
        verify(venues).save(venue);
        verify(decisionNotifications).decision(44L, 5L, "Campo Corrigido", "player", true, "Verificado no site da autarquia");
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
        verify(decisionNotifications).decision(44L, 5L, "Campo Original", "player", false, "Dados não confirmados");
    }

    @Test
    void repeatedPendingSuggestionPerUserAndVenueIsBlocked() {
        when(venues.findForEditReview(5L)).thenReturn(Optional.of(venue));
        when(submissions.existsByVenue_IdAndSubmittedByUsernameIgnoreCaseAndStatus(
                5L, "player", VenueEditSuggestion.Status.PENDING)).thenReturn(true);
        assertEquals(409, assertThrows(ResponseStatusException.class, () -> service.submit(
                5L, new VenueEditSuggestionRequest(Map.of("name", "Outro campo"),
                "A entrada tem outro nome"), "player")).getStatusCode().value());
    }



    @Test
    void gpsSuggestionRequiresPairedValidCoordinatesAndHttpsEvidence() {
        when(venues.findForEditReview(5L)).thenReturn(Optional.of(venue));
        var base = Map.of("latitude", "38.72", "longitude", "-9.14");
        for (var changes : List.of(
                Map.of("latitude", "38.72"),
                Map.of("longitude", "-9.14"),
                Map.of("latitude", "NaN", "longitude", "-9.14"),
                Map.of("latitude", "38,72", "longitude", "-9.14"),
                Map.of("latitude", "38.72", "longitude", "-32.1"),
                Map.of("latitude", "0", "longitude", "0"),
                Map.of("latitude", "38.72000000", "longitude", "-9.14"))) {
            assertEquals(400, assertThrows(ResponseStatusException.class,
                    () -> service.submit(5L, new VenueEditSuggestionRequest(
                            changes, "Coordenadas verificadas na ficha oficial",
                            "https://example.org/venue"), "player")).getStatusCode().value());
        }
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.submit(5L, new VenueEditSuggestionRequest(
                        base, "Coordenadas verificadas na ficha oficial"), "player")).getStatusCode().value());
        verify(submissions, never()).save(any());
        verify(venues, never()).save(any());
    }

    @Test
    void gpsSubmissionIsPendingOnlyAndAuditsBothCoordinatesWithTheirSource() {
        when(venues.findForEditReview(5L)).thenReturn(Optional.of(venue));
        when(submissions.save(any())).thenAnswer(inv -> {
            var value = (VenueEditSuggestion) inv.getArgument(0);
            value.setId(70L);
            return value;
        });
        var source = "https://example.org/venue";
        var result = service.submit(5L, new VenueEditSuggestionRequest(
                Map.of("latitude", "38.720", "longitude", "-9.140"),
                "Coordenadas da entrada na ficha oficial", source), "player");
        assertEquals("PENDING", result.status());
        assertEquals("38.72", result.proposed().get("latitude"));
        assertEquals("-9.14", result.proposed().get("longitude"));
        assertTrue(result.before().containsKey("latitude"));
        assertTrue(result.before().containsKey("longitude"));
        assertNull(venue.getLatitude());
        assertNull(venue.getLongitude());
        assertEquals(source, result.sourceUrl());
        verify(venues, never()).save(any());
    }

    @Test
    void gpsApprovalRequiresModeratorSourceAttestationBeforePublishing() {
        venue.setLatitude(38.71);
        venue.setLongitude(-9.13);
        var prior = submitted("Campo Original", "Campo Corrigido");
        prior.setBeforeJson("{\"latitude\":\"38.71\",\"longitude\":\"-9.13\"}");
        prior.setChangesJson("{\"latitude\":\"38.72\",\"longitude\":\"-9.14\"}");
        prior.setSourceUrl("https://example.org/venue");
        when(submissions.findForReview(44L)).thenReturn(Optional.of(prior));
        when(venues.findForEditReview(5L)).thenReturn(Optional.of(venue));
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.approve(44L, "Verificado o ponto", "admin", false)).getStatusCode().value());
        assertEquals(38.71, venue.getLatitude());
        assertEquals(-9.13, venue.getLongitude());
        when(submissions.save(any())).thenAnswer(inv -> inv.getArgument(0));
        var approved = service.approve(44L, "Posição exata confirmada", "admin", true);
        assertEquals("APPROVED", approved.status());
        assertEquals(38.72, venue.getLatitude());
        assertEquals(-9.14, venue.getLongitude());
        assertNotNull(approved.sourceReviewConfirmedAt());
        verify(venues).save(venue);
    }

    @Test
    void gpsApprovalDoesNotOverwriteMoreRecentCoordinatesOrPartialProposals() {
        venue.setLatitude(38.73);
        venue.setLongitude(-9.13);
        var proposal = submitted("Campo Original", "Campo Corrigido");
        proposal.setBeforeJson("{\"latitude\":\"38.71\",\"longitude\":\"-9.13\"}");
        proposal.setChangesJson("{\"latitude\":\"38.72\",\"longitude\":\"-9.14\"}");
        proposal.setSourceUrl("https://example.org/venue");
        when(submissions.findForReview(44L)).thenReturn(Optional.of(proposal));
        when(venues.findForEditReview(5L)).thenReturn(Optional.of(venue));
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.approve(44L, "Verificado", "admin", true)).getStatusCode().value());
        verify(venues, never()).save(any());
    }

    @Test
    void contactAddRequiresExactHttpsPublicSourceBeforeAnySave() {
        when(venues.findForEditReview(5L)).thenReturn(Optional.of(venue));
        var noSource = new VenueEditSuggestionRequest(
                Map.of("contact", "225 073 300"), "Telefone na página da piscina");
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.submit(5L, noSource, "player")).getStatusCode().value());
        for (String invalid : java.util.List.of(
                "http://www.agoraporto.pt/venue", "javascript:alert(1)",
                "https://user:pass@www.agoraporto.pt/venue",
                "https://127.0.0.1/source", "https://localhost/source",
                "https://www.agoraporto.pt:444/source",
                "https://www.agoraporto.pt/source#tracking",
                "https://www.agoraporto.pt.evil.com/" + "x".repeat(510))) {
            var input = new VenueEditSuggestionRequest(Map.of("contact", "225 073 300"),
                    "Telefone na página da piscina", invalid);
            assertEquals(400, assertThrows(ResponseStatusException.class,
                    () -> service.submit(5L, input, "player")).getStatusCode().value(), invalid);
        }
        verify(submissions, never()).save(any());
        verify(venues, never()).save(any());
    }

    @Test
    void acceptsContactEvidenceAsPendingButNeverPublishesWithoutApproval() {
        when(venues.findForEditReview(5L)).thenReturn(Optional.of(venue));
        when(submissions.save(any())).thenAnswer(inv -> {
            VenueEditSuggestion suggestion = inv.getArgument(0);
            suggestion.setId(45L);
            return suggestion;
        });
        var source = "https://www.agoraporto.pt/piscinas-municipais/piscina-municipal-da-constituicao";
        var result = service.submit(5L, new VenueEditSuggestionRequest(
                Map.of("contact", "225 073 300"), "Telefone da receção indicado na fonte", source),
                "player");
        assertEquals("PENDING", result.status());
        assertEquals(source, result.sourceUrl());
        assertEquals("225 073 300", result.proposed().get("contact"));
        assertNull(venue.getContact());
        verify(venues, never()).save(any());
    }

    @Test
    void contactRemovalAndUnrelatedUpdatesStayBackwardsCompatible() {
        venue.setContact("225000000");
        when(venues.findForEditReview(5L)).thenReturn(Optional.of(venue));
        when(submissions.save(any())).thenAnswer(inv -> inv.getArgument(0));
        var result = service.submit(5L, new VenueEditSuggestionRequest(
                Map.of("contact", ""), "Contacto publicado incorretamente"), "player");
        assertNull(result.sourceUrl());
        assertEquals("", result.proposed().get("contact"));
        assertEquals("225000000", venue.getContact());
    }

    @Test
    void oldPendingContactWithoutEvidenceCannotBeApproved() {
        var prior = submitted("Campo Original", "Campo Corrigido");
        prior.setBeforeJson("{\"contact\":null}");
        prior.setChangesJson("{\"contact\":\"225073300\"}");
        when(submissions.findForReview(44L)).thenReturn(Optional.of(prior));
        when(venues.findForEditReview(5L)).thenReturn(Optional.of(venue));
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.approve(44L, "Confirmado", "moderator")).getStatusCode().value());
        assertNull(venue.getContact());
        verify(venues, never()).save(any());
    }

    @Test
    void reviewedContactCanBeApprovedWithoutChangingItsSource() {
        var prior = submitted("Campo Original", "Campo Corrigido");
        prior.setBeforeJson("{\"contact\":null}");
        prior.setChangesJson("{\"contact\":\"225073300\"}");
        prior.setSourceUrl("https://www.agoraporto.pt/piscinas-municipais/piscina-municipal-da-constituicao");
        when(submissions.findForReview(44L)).thenReturn(Optional.of(prior));
        when(venues.findForEditReview(5L)).thenReturn(Optional.of(venue));
        when(submissions.save(any())).thenAnswer(inv -> inv.getArgument(0));
        var result = service.approve(44L, "Confirmado no recinto e na fonte oficial", "moderator", true);
        assertEquals("APPROVED", result.status());
        assertEquals("225073300", venue.getContact());
        assertEquals(prior.getSourceUrl(), result.sourceUrl());
        assertNotNull(result.sourceReviewConfirmedAt());
        assertEquals(result.reviewedAt(), result.sourceReviewConfirmedAt());
        assertEquals(prior.getSourceReviewConfirmedAt(), result.sourceReviewConfirmedAt());
        assertEquals("moderator", result.reviewedByUsername());
    }


    @Test
    void directApiStyleApprovalMustNotBypassManualSourceReview() {
        var prior = submitted("Campo Original", "Campo Corrigido");
        prior.setBeforeJson("{\"contact\":null}");
        prior.setChangesJson("{\"contact\":\"225073300\"}");
        prior.setSourceUrl("https://www.agoraporto.pt/piscinas-municipais/piscina-municipal-da-constituicao");
        when(submissions.findForReview(44L)).thenReturn(Optional.of(prior));
        when(venues.findForEditReview(5L)).thenReturn(Optional.of(venue));

        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.approve(44L, "Fonte revista", "moderator")
        ).getStatusCode().value());
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.approve(44L, "Fonte revista", "moderator", false)
        ).getStatusCode().value());
        assertEquals(VenueEditSuggestion.Status.PENDING, prior.getStatus());
        assertNull(venue.getContact());
        assertNull(prior.getSourceReviewConfirmedAt());
        verify(venues, never()).save(any());
        verify(submissions, never()).save(any());
        verifyNoInteractions(decisionNotifications);
    }

    @Test
    void attestationAloneNeverOverridesMissingContactSource() {
        var prior = submitted("Campo Original", "Campo Corrigido");
        prior.setBeforeJson("{\"contact\":null}");
        prior.setChangesJson("{\"contact\":\"225073300\"}");
        when(submissions.findForReview(44L)).thenReturn(Optional.of(prior));
        when(venues.findForEditReview(5L)).thenReturn(Optional.of(venue));
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.approve(44L, "Fonte revista", "moderator", true)
        ).getStatusCode().value());
        verify(venues, never()).save(any());
    }


    @Test
    void contactRemovalMayBeApprovedButDoesNotClaimSourceVerification() {
        venue.setContact("225073300");
        var prior = submitted("Campo Original", "Campo Corrigido");
        prior.setBeforeJson("{\"contact\":\"225073300\"}");
        prior.setChangesJson("{\"contact\":\"\"}");
        when(submissions.findForReview(44L)).thenReturn(Optional.of(prior));
        when(venues.findForEditReview(5L)).thenReturn(Optional.of(venue));
        when(submissions.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = service.approve(44L, "O contacto já não pertence ao recinto",
                "moderator", false);
        assertEquals("APPROVED", result.status());
        assertNull(venue.getContact());
        assertNull(result.sourceReviewConfirmedAt());
        assertNotNull(result.reviewedAt());
        verify(venues).save(venue);
    }

    @Test
    void rejectedProposalsNeverClaimContactSourceReview() {
        var prior = submitted("Campo Original", "Campo Corrigido");
        prior.setBeforeJson("{\"contact\":null}");
        prior.setChangesJson("{\"contact\":\"225073300\"}");
        prior.setSourceUrl("https://www.agoraporto.pt/piscinas-municipais/piscina-municipal-da-constituicao");
        when(submissions.findForReview(44L)).thenReturn(Optional.of(prior));
        when(submissions.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = service.reject(44L, "Dados não suficientemente confirmados", "moderator");
        assertEquals("REJECTED", result.status());
        assertNull(result.sourceReviewConfirmedAt());
        assertNull(prior.getSourceReviewConfirmedAt());
        verify(venues, never()).save(any());
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
