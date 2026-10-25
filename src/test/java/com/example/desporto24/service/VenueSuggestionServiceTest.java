package com.example.desporto24.service;

import com.example.desporto24.model.dto.venue.CreateVenueRequest;
import com.example.desporto24.model.dto.venue.VenueResponse;
import com.example.desporto24.model.venue.VenueSuggestion;
import com.example.desporto24.repository.venue.VenueSuggestionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenueSuggestionServiceTest {
    @Mock VenueSuggestionRepository repository;
    @Mock VenueService venueService;
    private final ObjectMapper mapper = new ObjectMapper();
    VenueSuggestionService service;

    @BeforeEach
    void setUp() {
        service = new VenueSuggestionService(repository, venueService);
    }

    @Test
    void serviceConstructorDoesNotRequireAnInjectedJacksonTwoBean() {
        var constructors = VenueSuggestionService.class.getDeclaredConstructors();
        assertEquals(1, constructors.length);
        assertArrayEquals(new Class<?>[]{
                VenueSuggestionRepository.class, VenueService.class
        }, constructors[0].getParameterTypes());
        assertNotNull(service);
    }

    private CreateVenueRequest request() throws Exception {
        return mapper.readValue(
            "{\"name\":\"Pavilhão Novo\",\"city\":\"Sintra\",\"district\":\"Lisboa\",\"address\":\"Rua A\",\"sports\":\"Futsal\"}",
            CreateVenueRequest.class);
    }

    @Test
    void submittingOnlyCreatesPendingSuggestionNeverPublishesVenue() throws Exception {
        when(repository.save(any(VenueSuggestion.class))).thenAnswer(inv -> {
            VenueSuggestion suggestion = inv.getArgument(0);
            suggestion.setId(19L);
            return suggestion;
        });
        var response = service.submit(request(), "tester");
        assertEquals(19L, response.id());
        assertEquals("PENDING", response.status());
        assertEquals("Pavilhão Novo", response.venue().name());
        assertEquals("tester", response.submittedByUsername());
        verifyNoInteractions(venueService);
    }

    @Test
    void repeatedPendingSubmissionFromSameUserIsRejected() throws Exception {
        when(repository.existsByStatusAndSubmittedByUsernameIgnoreCaseAndNameIgnoreCaseAndCityIgnoreCase(
            VenueSuggestion.Status.PENDING,"tester","Pavilhão Novo","Sintra")).thenReturn(true);
        assertThrows(ResponseStatusException.class, () -> service.submit(request(), "tester"));
        verify(repository, never()).save(any());
        verifyNoInteractions(venueService);
    }

    @Test
    void approvalPublishesOnceAndRecordsModerator() throws Exception {
        var pending = VenueSuggestion.builder()
            .id(19L).name("Pavilhão Novo").city("Sintra")
            .requestPayload(mapper.writeValueAsString(request()))
            .submittedByUsername("tester").status(VenueSuggestion.Status.PENDING)
            .build();
        when(repository.findForReview(19L)).thenReturn(Optional.of(pending));
        var published = mock(VenueResponse.class);
        when(published.id()).thenReturn(55L);
        when(venueService.create(any(CreateVenueRequest.class), eq("tester"))).thenReturn(published);
        when(repository.save(any(VenueSuggestion.class))).thenAnswer(inv -> inv.getArgument(0));
        var output = service.approve(19L, "admin");
        assertEquals("APPROVED",output.status());
        assertEquals(55L, output.publishedVenueId());
        assertEquals("admin", output.reviewedByUsername());
        assertNotNull(output.reviewedAt());
        verify(venueService, times(1)).create(any(CreateVenueRequest.class), eq("tester"));
    }

    @Test
    void rejectionNeverPublishesAndRecordsDecision() throws Exception {
        var pending = VenueSuggestion.builder()
            .id(19L).name("Pavilhão Novo").city("Sintra")
            .requestPayload(mapper.writeValueAsString(request()))
            .submittedByUsername("tester").status(VenueSuggestion.Status.PENDING)
            .build();
        when(repository.findForReview(19L)).thenReturn(Optional.of(pending));
        when(repository.save(any(VenueSuggestion.class))).thenAnswer(inv -> inv.getArgument(0));
        var output = service.reject(19L, "admin");
        assertEquals("REJECTED",output.status());
        assertNull(output.publishedVenueId());
        assertNotNull(output.reviewedAt());
        verifyNoInteractions(venueService);
    }

    @Test
    void alreadyReviewedSuggestionCannotPublishOrBeReviewedTwice() throws Exception {
        var reviewed = VenueSuggestion.builder().id(19L)
            .status(VenueSuggestion.Status.APPROVED).build();
        when(repository.findForReview(19L)).thenReturn(Optional.of(reviewed));
        assertThrows(ResponseStatusException.class, () -> service.approve(19L, "admin"));
        assertThrows(ResponseStatusException.class, () -> service.reject(19L, "admin"));
        verify(repository, never()).save(any());
        verifyNoInteractions(venueService);
    }

    @Test
    void rejectedOrFailedApprovalLeavesSuggestionPending() throws Exception {
        var pending = VenueSuggestion.builder().id(19L)
            .submittedByUsername("tester").status(VenueSuggestion.Status.PENDING)
            .requestPayload(mapper.writeValueAsString(request())).build();
        when(repository.findForReview(19L)).thenReturn(Optional.of(pending));
        when(venueService.create(any(CreateVenueRequest.class), eq("tester")))
            .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT));
        assertThrows(ResponseStatusException.class, () -> service.approve(19L, "admin"));
        assertEquals(VenueSuggestion.Status.PENDING, pending.getStatus());
        verify(repository, never()).save(any());
    }
}
