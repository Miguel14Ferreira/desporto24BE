package com.example.desporto24.service;

import com.example.desporto24.model.dto.venue.CreateVenueRequest;
import com.example.desporto24.model.dto.venue.VenueSuggestionPageResponse;
import com.example.desporto24.model.dto.venue.VenueSuggestionResponse;
import com.example.desporto24.model.venue.VenueSuggestion;
import com.example.desporto24.repository.venue.VenueSuggestionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class VenueSuggestionService {
    private final VenueSuggestionRepository repository;
    private final VenueService venueService;
    private final ObjectMapper objectMapper;

    public VenueSuggestionResponse submit(CreateVenueRequest request, String username) {
        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (repository.existsByStatusAndSubmittedByUsernameIgnoreCaseAndNameIgnoreCaseAndCityIgnoreCase(
                VenueSuggestion.Status.PENDING, username, request.name().trim(), request.city().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe uma sugestão tua para este recinto em análise");
        }
        final String payload;
        try {
            payload = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Não foi possível guardar a sugestão", exception);
        }
        var suggestion = VenueSuggestion.builder()
                .name(request.name().trim())
                .city(request.city().trim())
                .district(request.district())
                .address(request.address())
                .sports(request.sports())
                .submittedByUsername(username)
                .submittedAt(Instant.now())
                .status(VenueSuggestion.Status.PENDING)
                .requestPayload(payload)
                .build();
        return response(repository.save(suggestion));
    }

    @Transactional(readOnly = true)
    public VenueSuggestionPageResponse pending(int page) {
        if (page < 0 || page > 1000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Página inválida");
        }
        var slice = repository.findByStatusOrderBySubmittedAtAsc(
                VenueSuggestion.Status.PENDING, PageRequest.of(page, 20));
        return new VenueSuggestionPageResponse(
                slice.getContent().stream().map(this::response).toList(), page, slice.hasNext());
    }

    public VenueSuggestionResponse approve(long id, String adminUsername) {
        var suggestion = pendingForReview(id);
        var original = parse(suggestion.getRequestPayload());
        // Reuse full venue validation and duplicate checks at the moment of approval.
        // Any exception rolls back both the venue insert and moderation decision.
        var published = venueService.create(original, suggestion.getSubmittedByUsername());
        suggestion.setStatus(VenueSuggestion.Status.APPROVED);
        suggestion.setPublishedVenueId(published.id());
        suggestion.setReviewedAt(Instant.now());
        suggestion.setReviewedByUsername(adminUsername);
        return response(repository.save(suggestion));
    }

    public VenueSuggestionResponse reject(long id, String adminUsername) {
        var suggestion = pendingForReview(id);
        suggestion.setStatus(VenueSuggestion.Status.REJECTED);
        suggestion.setReviewedByUsername(adminUsername);
        suggestion.setReviewedAt(Instant.now());
        return response(repository.save(suggestion));
    }

    private VenueSuggestion pendingForReview(long id) {
        var suggestion = repository.findForReview(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sugestão inexistente"));
        if (suggestion.getStatus() != VenueSuggestion.Status.PENDING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Sugestão já analisada");
        }
        return suggestion;
    }

    private CreateVenueRequest parse(String payload) {
        try {
            return objectMapper.readValue(payload, CreateVenueRequest.class);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Sugestão guardada não pode ser lida", ex);
        }
    }

    private VenueSuggestionResponse response(VenueSuggestion s) {
        return new VenueSuggestionResponse(s.getId(), s.getStatus().name(),
                parse(s.getRequestPayload()), s.getSubmittedByUsername(), s.getSubmittedAt(),
                s.getReviewedByUsername(), s.getReviewedAt(), s.getPublishedVenueId());
    }
}
