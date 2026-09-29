package com.example.desporto24.service;

import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.venue.CreateVenueRequest;
import com.example.desporto24.model.dto.venue.VenueResponse;
import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Service
@RequiredArgsConstructor
@Transactional
public class VenueService {

    private final VenueRepository venueRepository;
    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public List<VenueResponse> search(String city, String sport) {
        String cityQuery = normalize(city);
        String sportQuery = normalize(sport);

        return venueRepository.findAllByOrderByNameAsc()
                .stream()
                .filter(venue -> cityQuery == null
                        || normalize(venue.getCity()).contains(cityQuery))
                .filter(venue -> sportQuery == null
                        || venue.getSports() == null
                        || normalize(venue.getSports()).contains(sportQuery))
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VenueResponse get(Long id) {
        return toResponse(venue(id));
    }

    public VenueResponse create(CreateVenueRequest request, String username) {
        Profile creator = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        Venue venue = venueRepository.save(Venue.builder()
                .name(request.name().trim())
                .city(request.city().trim())
                .address(request.address().trim())
                .sports(trimToNull(request.sports()))
                .surfaceType(trimToNull(request.surfaceType()))
                .indoor(request.indoor())
                .changingRooms(request.changingRooms())
                .parking(request.parking())
                .lighting(request.lighting())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .createdBy(creator)
                .createdAt(Instant.now())
                .build());

        return toResponse(venue);
    }

    public Venue venue(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recinto não encontrado"));
    }

    public VenueResponse toResponse(Venue venue) {
        return new VenueResponse(
                venue.getId(),
                venue.getName(),
                venue.getCity(),
                venue.getAddress(),
                venue.getSports(),
                venue.getSurfaceType(),
                venue.isIndoor(),
                venue.isChangingRooms(),
                venue.isParking(),
                venue.isLighting(),
                venue.getLatitude(),
                venue.getLongitude(),
                informationSource(venue),
                venue.getCreatedBy() == null ? null : venue.getCreatedBy().getUsername()
        );
    }

    private String informationSource(Venue venue) {
        return venue.getInformationSource() == null
                ? Venue.InformationSource.COMMUNITY.name()
                : venue.getInformationSource().name();
    }

    private String normalize(String value) {
        String trimmed = trimToNull(value);
        return trimmed == null ? null : trimmed.toLowerCase(Locale.ROOT);
    }

    private String trimToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
