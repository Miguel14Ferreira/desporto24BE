package com.example.desporto24.service;

import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.venue.CreateVenueSportConditionRequest;
import com.example.desporto24.model.dto.venue.VenueSportConditionResponse;
import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.model.venue.VenueSportCondition;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import com.example.desporto24.repository.venue.VenueSportConditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Service
@RequiredArgsConstructor
@Transactional
public class VenueSportConditionService {

    private final VenueRepository venueRepository;
    private final VenueSportConditionRepository venueSportConditionRepository;
    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public List<VenueSportConditionResponse> list(Long venueId) {
        if (!venueRepository.existsById(venueId)) {
            throw new IllegalArgumentException("Recinto não encontrado");
        }

        return venueSportConditionRepository.findByVenueIdOrderBySportAsc(venueId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public VenueSportConditionResponse create(
            Long venueId,
            CreateVenueSportConditionRequest request,
            String username) {

        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("Recinto não encontrado"));

        Profile creator = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        String sport = request.sport().trim();
        if (venueSportConditionRepository
                .findByVenueIdAndSportIgnoreCase(venueId, sport)
                .isPresent()) {
            throw new IllegalArgumentException("Já existem condições para este desporto neste recinto");
        }

        VenueSportCondition saved = venueSportConditionRepository.save(
                VenueSportCondition.builder()
                        .venue(venue)
                        .sport(sport)
                        .conditions(request.conditions().trim())
                        .informationSource(Venue.InformationSource.COMMUNITY)
                        .createdBy(creator)
                        .createdAt(Instant.now())
                        .build()
        );

        return toResponse(saved);
    }

    private VenueSportConditionResponse toResponse(VenueSportCondition condition) {
        return new VenueSportConditionResponse(
                condition.getId(),
                condition.getSport(),
                condition.getConditions(),
                condition.getInformationSource() == null
                        ? Venue.InformationSource.COMMUNITY.name()
                        : condition.getInformationSource().name(),
                condition.getCreatedBy() == null ? null : condition.getCreatedBy().getUsername(),
                condition.getCreatedAt()
        );
    }
}
