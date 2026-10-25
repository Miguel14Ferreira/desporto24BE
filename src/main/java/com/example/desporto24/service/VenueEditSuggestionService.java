package com.example.desporto24.service;

import com.example.desporto24.constant.SportsCatalog;
import com.example.desporto24.model.dto.venue.*;
import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.model.venue.VenueEditSuggestion;
import com.example.desporto24.repository.venue.VenueEditSuggestionRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Moderated, allowlisted changes to existing venues.
 * No public venue field is mutated during submission.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class VenueEditSuggestionService {
    private static final Set<String> EDITABLE = Set.of(
            "name", "city", "district", "address", "sports",
            "openingHours", "website", "contact");
    private static final Set<String> DISTRICTS = Set.of(
            "Aveiro", "Beja", "Braga", "Bragança", "Castelo Branco", "Coimbra",
            "Évora", "Faro", "Guarda", "Leiria", "Lisboa", "Portalegre",
            "Porto", "Santarém", "Setúbal", "Viana do Castelo", "Vila Real", "Viseu");
    private static final TypeReference<LinkedHashMap<String, String>> MAP_TYPE = new TypeReference<>() {};

    private final VenueEditSuggestionRepository repository;
    private final VenueRepository venues;
    private final VenueEditDecisionNotificationService decisionNotifications;
    // Do not inject the Jackson 2 mapper into Spring Boot 4's Jackson 3 HTTP context.
    private final ObjectMapper mapper = new ObjectMapper();

    public VenueEditSuggestionResponse submit(Long venueId, VenueEditSuggestionRequest request, String username) {
        if (username == null || username.isBlank()) throw bad(HttpStatus.UNAUTHORIZED, "Autenticação obrigatória");
        if (request == null || request.changes() == null || request.changes().isEmpty()
                || request.changes().size() > EDITABLE.size()) {
            throw bad(HttpStatus.BAD_REQUEST, "Seleciona pelo menos uma alteração");
        }
        if (request.reason() == null || request.reason().trim().length() < 10
                || request.reason().trim().length() > 1000) {
            throw bad(HttpStatus.BAD_REQUEST, "Explica o motivo da alteração (10 a 1000 caracteres)");
        }
        Venue venue = venues.findById(venueId)
                .orElseThrow(() -> bad(HttpStatus.NOT_FOUND, "Recinto inexistente"));
        if (repository.existsByVenue_IdAndSubmittedByUsernameIgnoreCaseAndStatus(
                venueId, username, VenueEditSuggestion.Status.PENDING)) {
            throw bad(HttpStatus.CONFLICT, "Já tens uma alteração deste recinto em análise");
        }
        Map<String, String> before = new LinkedHashMap<>();
        Map<String, String> after = new LinkedHashMap<>();
        for (var entry : request.changes().entrySet()) {
            String field = entry.getKey();
            if (field == null || !EDITABLE.contains(field)) {
                throw bad(HttpStatus.BAD_REQUEST, "Campo não editável");
            }
            String proposed = validate(field, entry.getValue());
            String current = value(venue, field);
            if (!Objects.equals(current, proposed)) {
                before.put(field, current);
                after.put(field, proposed);
            }
        }
        if (after.isEmpty()) throw bad(HttpStatus.BAD_REQUEST, "Nenhuma alteração efetiva");
        String sourceUrl = validateSourceUrl(request.sourceUrl());
        if (after.containsKey("contact") && !after.get("contact").isBlank() && sourceUrl == null) {
            throw bad(HttpStatus.BAD_REQUEST, "Indica a ligação HTTPS da fonte pública do contacto");
        }
        var suggestion = VenueEditSuggestion.builder()
                .venue(venue)
                .beforeJson(json(before))
                .changesJson(json(after))
                .reason(request.reason().trim())
                .sourceUrl(sourceUrl)
                .submittedByUsername(username)
                .submittedAt(Instant.now())
                .status(VenueEditSuggestion.Status.PENDING)
                .build();
        return response(repository.save(suggestion));
    }

    @Transactional(readOnly = true)
    public VenueEditSuggestionResponse mine(Long venueId, String username) {
        if (username == null || username.isBlank()) throw bad(HttpStatus.UNAUTHORIZED, "Autenticação obrigatória");
        return repository.findFirstByVenue_IdAndSubmittedByUsernameIgnoreCaseOrderBySubmittedAtDesc(
                venueId, username).map(this::response)
                .orElseThrow(() -> bad(HttpStatus.NOT_FOUND, "Sem propostas anteriores"));
    }

    @Transactional(readOnly = true)
    public VenueEditSuggestionPageResponse pending(int page) {
        if (page < 0 || page > 1000) throw bad(HttpStatus.BAD_REQUEST, "Página inválida");
        var slice = repository.findByStatusOrderBySubmittedAtAsc(
                VenueEditSuggestion.Status.PENDING, PageRequest.of(page, 20));
        return new VenueEditSuggestionPageResponse(
                slice.getContent().stream().map(this::response).toList(), page, slice.hasNext());
    }

    /**
     * Backward-compatible Java entry point. No implicit contact-source review.
     * Existing approvals of non-contact fields continue to work.
     */
    public VenueEditSuggestionResponse approve(long id, String note, String moderator) {
        return approve(id, note, moderator, false);
    }

    public VenueEditSuggestionResponse approve(long id, String note, String moderator,
            boolean sourceReviewed) {
        var proposal = pendingForReview(id);
        // A row lock prevents two parallel approvals from overwriting each other.
        var venue = venues.findForEditReview(proposal.getVenue().getId())
                .orElseThrow(() -> bad(HttpStatus.NOT_FOUND, "Recinto inexistente"));
        Map<String, String> before = decode(proposal.getBeforeJson());
        Map<String, String> proposed = decode(proposal.getChangesJson());
        for (String field : proposed.keySet()) {
            if (!EDITABLE.contains(field) || !before.containsKey(field)) {
                throw bad(HttpStatus.BAD_REQUEST, "Proposta de alteração inválida");
            }
            if (!Objects.equals(value(venue, field), before.get(field))) {
                // Do not silently overwrite a more recent edit/import.
                throw bad(HttpStatus.CONFLICT,
                        "O recinto mudou entretanto. Reavalia a proposta antes de aprovar");
            }
            validate(field, proposed.get(field));
        }
        // Every contact publication needs BOTH an evidence link and an explicit
        // server-side attestation by the authenticated ADMIN/SUPER_ADMIN.
        // A browser checkbox alone is bypassable by a direct API call.
        if (proposed.containsKey("contact") && !proposed.get("contact").isBlank()) {
            if (validateSourceUrl(proposal.getSourceUrl()) == null) {
                throw bad(HttpStatus.BAD_REQUEST, "Contacto sem fonte HTTPS; rejeita e pede nova proposta");
            }
            if (!sourceReviewed) {
                throw bad(HttpStatus.BAD_REQUEST, "Confirma a verificação da fonte antes de aprovar o contacto");
            }
        }
        proposed.forEach((field, value) -> setValue(venue, field, value));
        venues.save(venue);
        proposal.setStatus(VenueEditSuggestion.Status.APPROVED);
        proposal.setReviewedByUsername(moderator);
        proposal.setReviewedAt(Instant.now());
        proposal.setDecisionNote(cleanNote(note));
        var result = response(repository.save(proposal));
        decisionNotifications.decision(id, venue.getId(), venue.getName(),
                proposal.getSubmittedByUsername(), true, proposal.getDecisionNote());
        return result;
    }

    public VenueEditSuggestionResponse reject(long id, String note, String moderator) {
        if (note == null || note.trim().length() < 5 || note.trim().length() > 1000) {
            throw bad(HttpStatus.BAD_REQUEST, "Explica a rejeição ao utilizador");
        }
        var proposal = pendingForReview(id);
        proposal.setStatus(VenueEditSuggestion.Status.REJECTED);
        proposal.setReviewedByUsername(moderator);
        proposal.setReviewedAt(Instant.now());
        proposal.setDecisionNote(note.trim());
        var result = response(repository.save(proposal));
        decisionNotifications.decision(id, proposal.getVenue().getId(), proposal.getVenue().getName(),
                proposal.getSubmittedByUsername(), false, proposal.getDecisionNote());
        return result;
    }

    private VenueEditSuggestion pendingForReview(long id) {
        var proposal = repository.findForReview(id)
                .orElseThrow(() -> bad(HttpStatus.NOT_FOUND, "Proposta inexistente"));
        if (proposal.getStatus() != VenueEditSuggestion.Status.PENDING) {
            throw bad(HttpStatus.CONFLICT, "Proposta já analisada");
        }
        return proposal;
    }

    private String validate(String field, String raw) {
        if (raw == null) throw bad(HttpStatus.BAD_REQUEST, "Valor inválido");
        String value = raw.trim();
        int limit = switch (field) {
            case "name" -> 140;
            case "city", "district" -> 120;
            case "openingHours" -> 1000;
            default -> 255;
        };
        if (value.length() > limit) throw bad(HttpStatus.BAD_REQUEST, "Valor demasiado longo");
        if (value.isBlank() && !Set.of("openingHours", "website", "contact").contains(field)) {
            throw bad(HttpStatus.BAD_REQUEST, "Este campo não pode ficar vazio");
        }
        if ("district".equals(field) && !DISTRICTS.contains(value)) {
            throw bad(HttpStatus.BAD_REQUEST, "Distrito inválido");
        }
        if ("sports".equals(field) &&
                java.util.Arrays.stream(value.split(";", -1)).map(String::trim)
                        .anyMatch(s -> s.isBlank() || !SportsCatalog.ALLOWED.contains(s))) {
            throw bad(HttpStatus.BAD_REQUEST, "Escolhe modalidades do catálogo Jynlo");
        }
        if ("website".equals(field) && !value.isEmpty()) {
            try {
                URI url = URI.create(value);
                if (!"https".equalsIgnoreCase(url.getScheme()) || url.getHost() == null
                        || url.getRawUserInfo() != null || (url.getPort() != -1 && url.getPort() != 443)) {
                    throw bad(HttpStatus.BAD_REQUEST, "Website HTTPS inválido");
                }
            } catch (IllegalArgumentException exception) {
                throw bad(HttpStatus.BAD_REQUEST, "Website HTTPS inválido");
            }
        }
        return value;
    }

    /** Validates syntax only; NEVER performs an HTTP request or treats the URL as verified. */
    private String validateSourceUrl(String raw) {
        if (raw == null || raw.isBlank()) return null;
        String value = raw.trim();
        if (value.length() > 500) throw bad(HttpStatus.BAD_REQUEST, "Fonte demasiado longa");
        try {
            URI uri = URI.create(value);
            String host = uri.getHost();
            if (!"https".equalsIgnoreCase(uri.getScheme()) || host == null
                    || host.length() > 253 || !host.contains(".")
                    || host.endsWith(".") || uri.getRawUserInfo() != null
                    || (uri.getPort() != -1 && uri.getPort() != 443)
                    || uri.getRawFragment() != null
                    || !host.matches("(?i)[a-z0-9-]+(\\.[a-z0-9-]+)+")
                    || host.matches("[0-9.]+")) {
                throw bad(HttpStatus.BAD_REQUEST, "Fonte deve ser uma página pública HTTPS válida");
            }
        } catch (IllegalArgumentException exception) {
            throw bad(HttpStatus.BAD_REQUEST, "Fonte deve ser uma página pública HTTPS válida");
        }
        return value;
    }

    private String value(Venue venue, String field) {
        return switch (field) {
            case "name" -> venue.getName();
            case "city" -> venue.getCity();
            case "district" -> venue.getDistrict();
            case "address" -> venue.getAddress();
            case "sports" -> venue.getSports();
            case "openingHours" -> venue.getOpeningHours();
            case "website" -> venue.getWebsite();
            case "contact" -> venue.getContact();
            default -> throw bad(HttpStatus.BAD_REQUEST, "Campo não editável");
        };
    }

    private void setValue(Venue venue, String field, String input) {
        String value = input.isEmpty() ? null : input;
        switch (field) {
            case "name" -> venue.setName(input);
            case "city" -> venue.setCity(input);
            case "district" -> venue.setDistrict(input);
            case "address" -> venue.setAddress(input);
            case "sports" -> venue.setSports(input);
            case "openingHours" -> venue.setOpeningHours(value);
            case "website" -> venue.setWebsite(value);
            case "contact" -> venue.setContact(value);
            default -> throw bad(HttpStatus.BAD_REQUEST, "Campo não editável");
        }
    }

    private String json(Map<String, String> values) {
        try { return mapper.writeValueAsString(values); }
        catch (JsonProcessingException e) { throw new IllegalStateException("Não foi possível guardar a sugestão", e); }
    }

    private Map<String, String> decode(String payload) {
        try { return mapper.readValue(payload, MAP_TYPE); }
        catch (JsonProcessingException e) { throw new IllegalStateException("Proposta inválida", e); }
    }

    private VenueEditSuggestionResponse response(VenueEditSuggestion proposal) {
        return new VenueEditSuggestionResponse(
                proposal.getId(), proposal.getVenue().getId(), proposal.getVenue().getName(),
                proposal.getStatus().name(), decode(proposal.getBeforeJson()),
                decode(proposal.getChangesJson()), proposal.getReason(), proposal.getSubmittedByUsername(),
                proposal.getSubmittedAt(), proposal.getReviewedByUsername(), proposal.getReviewedAt(),
                proposal.getDecisionNote(), proposal.getSourceUrl());
    }

    private String cleanNote(String value) {
        return value == null || value.trim().isEmpty() ? null
                : value.trim().substring(0, Math.min(1000, value.trim().length()));
    }

    private ResponseStatusException bad(HttpStatus status, String detail) {
        return new ResponseStatusException(status, detail);
    }
}
