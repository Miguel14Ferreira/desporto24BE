package com.example.desporto24.service;

import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.venue.CreateVenueRequest;
import com.example.desporto24.model.dto.venue.OpenStreetMapVenueImportRequest;
import com.example.desporto24.model.dto.venue.VenueResponse;
import com.example.desporto24.model.dto.venue.VenueSuggestionPage;
import com.example.desporto24.model.dto.venue.VenueCoverageReport;
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

        List<Venue> venues = cityQuery == null
                ? venueRepository.findAllByOrderByNameAsc()
                : venueRepository.findByCityContainingIgnoreCaseOrderByNameAsc(cityQuery);

        return venues.stream()
                .filter(venue -> sportQuery == null
                        || (venue.getSports() != null
                        && hasSport(venue.getSports(), sportQuery)))
                .map(this::toResponse)
                .toList();
    }

    /**
     * Return exactly the frontend autocomplete contract: up to 40 results,
     * page index and a hasMore flag. Query 41 rows to avoid a count(*) scan.
     */
    @Transactional(readOnly = true)
    public VenueSuggestionPage suggest(String name, String city, String sport, int page) {
        if (page < 0 || page > 1000) {
            throw new IllegalArgumentException("Página de recintos inválida");
        }
        final int pageSize = 40;
        List<Venue> matches = venueRepository.findSuggestions(
                containsPattern(name),
                containsPattern(city),
                searchText(sport),
                page * pageSize);
        boolean hasMore = matches.size() > pageSize;
        return new VenueSuggestionPage(
                matches.stream().limit(pageSize).map(this::toResponse).toList(),
                page,
                hasMore);
    }

    private String searchText(String input) {
        String normalized = normalize(input);
        return normalized == null ? "" : normalized;
    }

    /**
     * Escape LIKE metacharacters while preserving partial-name search.
     * '~' is explicitly the PostgreSQL escape character in the repository query.
     * Values remain JDBC bind parameters, never interpolated into SQL.
     */
    private String containsPattern(String input) {
        String value = searchText(input);
        if (value.isEmpty()) return "%";
        return "%" + value.replace("~", "~~")
                          .replace("%", "~%")
                          .replace("_", "~_") + "%";
    }

    /**
     * An inventory of existing DB records, NOT a claim that a whole district
     * has been mapped. Null/blank district is deliberately left unclassified.
     */
    @Transactional(readOnly = true)
    public VenueCoverageReport coverage() {
        List<VenueCoverageReport.DistrictCoverage> districts =
                venueRepository.countCoverageByDistrict().stream()
                        .map(row -> new VenueCoverageReport.DistrictCoverage(
                                (String) row[0],
                                ((Number) row[1]).longValue(),
                                ((Number) row[2]).longValue(),
                                ((Number) row[3]).longValue(),
                                ((Number) row[4]).longValue()))
                        .toList();
        return new VenueCoverageReport(
                districts.stream().mapToLong(VenueCoverageReport.DistrictCoverage::venues).sum(),
                districts.stream().filter(row -> "(sem distrito)".equals(row.district()))
                        .mapToLong(VenueCoverageReport.DistrictCoverage::venues).sum(),
                districts.stream().mapToLong(VenueCoverageReport.DistrictCoverage::missingCoordinates).sum(),
                districts.stream().mapToLong(VenueCoverageReport.DistrictCoverage::missingSports).sum(),
                districts);
    }

    @Transactional(readOnly = true)
    public VenueResponse get(Long id) {
        return toResponse(venue(id));
    }

    public List<VenueResponse> importOpenStreetMapVenues(
            List<OpenStreetMapVenueImportRequest> requests) {
        return requests.stream()
                .map(request -> upsertOpenStreetMapVenue(
                        request.sourceReference(),
                        request.name(),
                        request.city(),
                        request.address(),
                        request.sports(),
                        request.venueType(),
                        request.environmentType(),
                        request.latitude(),
                        request.longitude()))
                .toList();
    }

    public VenueResponse upsertOpenStreetMapVenue(
            String sourceReference,
            String name,
            String city,
            String address,
            String sports,
            String venueType,
            Venue.EnvironmentType environmentType,
            Double latitude,
            Double longitude) {
        String normalizedSourceReference = requireText(sourceReference, "Referência externa obrigatória");
        String normalizedName = requireText(name, "Nome do recinto obrigatório");
        String normalizedCity = requireText(city, "Cidade do recinto obrigatória");
        String normalizedAddress = requireText(address, "Morada do recinto obrigatória");

        Venue venue = venueRepository.findByInformationSourceAndSourceReference(
                        Venue.InformationSource.OPENSTREETMAP,
                        normalizedSourceReference)
                .orElseGet(() -> Venue.builder()
                        .informationSource(Venue.InformationSource.OPENSTREETMAP)
                        .sourceReference(normalizedSourceReference)
                        .createdAt(Instant.now())
                        .build());

        venue.setName(normalizedName);
        venue.setCity(normalizedCity);
        venue.setAddress(normalizedAddress);
        venue.setSports(trimToNull(sports));
        venue.setVenueType(trimToNull(venueType));
        venue.setEnvironmentType(environmentType);
        venue.setIndoor(environmentType == Venue.EnvironmentType.INDOOR
                || environmentType == Venue.EnvironmentType.BOTH);
        venue.setLatitude(latitude);
        venue.setLongitude(longitude);

        return toResponse(venueRepository.save(venue));
    }

    public VenueResponse create(CreateVenueRequest request, String username) {
        Profile creator = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        Venue venue = venueRepository.save(Venue.builder()
                .name(request.name().trim())
                .city(request.city().trim())
                .address(request.address().trim())
                .sports(trimToNull(request.sports()))
                .venueType(trimToNull(request.venueType()))
                .ownershipType(request.ownershipType())
                .surfaceType(trimToNull(request.surfaceType()))
                .environmentType(request.environmentType())
                .indoor(request.environmentType() == Venue.EnvironmentType.INDOOR
                        || request.environmentType() == Venue.EnvironmentType.BOTH
                        || (request.environmentType() == null && request.indoor()))
                .changingRooms(request.changingRooms())
                .changingRoomTypes(request.changingRooms() ? normalizeChangingRoomTypes(request.changingRoomTypes()) : null)
                .toilets(request.toilets())
                .lockers(request.lockers())
                .showers(request.showers())
                .stands(request.stands())
                .drinkingFountain(request.drinkingFountain())
                .accessible(request.accessible())
                .firstAid(request.firstAid())
                .aed(request.aed())
                .medicalStation(request.medicalStation())
                .lifeguard(request.lifeguard())
                .staffOnSite(request.staffOnSite())
                .securitySurveillance(request.securitySurveillance())
                .cafe(request.cafe())
                .restaurant(request.restaurant())
                .vendingMachines(request.vendingMachines())
                .sportsShop(request.sportsShop())
                .wifi(request.wifi())
                .phoneCharging(request.phoneCharging())
                .nearbyRadiusMeters(request.nearbyRadiusMeters())
                .nearbyPharmacy(request.nearbyPharmacy())
                .nearbyAtm(request.nearbyAtm())
                .services(trimToNull(request.services()))
                .transport(trimToNull(request.transport()))
                .openingHours(trimToNull(request.openingHours()))
                .priceInfo(trimToNull(request.priceInfo()))
                .website(trimToNull(request.website()))
                .contact(trimToNull(request.contact()))
                .parking(request.parking())
                .freeParking(request.freeParking())
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
                venue.getVenueType(),
                ownershipType(venue),
                venue.getSurfaceType(),
                environmentType(venue),
                venue.isIndoor(),
                venue.isChangingRooms(),
                venue.getChangingRoomTypes(),
                venue.getToilets(),
                venue.getLockers(),
                venue.getShowers(),
                venue.getStands(),
                venue.getDrinkingFountain(),
                venue.getAccessible(),
                venue.getFirstAid(),
                venue.getAed(),
                venue.getMedicalStation(),
                venue.getLifeguard(),
                venue.getStaffOnSite(),
                venue.getSecuritySurveillance(),
                venue.getCafe(),
                venue.getRestaurant(),
                venue.getVendingMachines(),
                venue.getSportsShop(),
                venue.getWifi(),
                venue.getPhoneCharging(),
                venue.getNearbyRadiusMeters(),
                venue.getNearbyPharmacy(),
                venue.getNearbyAtm(),
                venue.getServices(),
                venue.getTransport(),
                venue.getOpeningHours(),
                venue.getPriceInfo(),
                venue.getWebsite(),
                venue.getContact(),
                venue.isParking(),
                venue.getFreeParking(),
                venue.isLighting(),
                venue.getLatitude(),
                venue.getLongitude(),
                informationSource(venue),
                venue.getCreatedBy() == null ? null : venue.getCreatedBy().getUsername(),
                venue.getCreatedAt()
        );
    }

    private String normalizeChangingRoomTypes(String value) {
        String normalized = trimToNull(value);
        if (normalized == null) return null;
        var allowed = java.util.Set.of("MALE", "FEMALE", "INDIVIDUAL", "ADAPTED");
        var types = java.util.Arrays.stream(normalized.split(";"))
                .map(String::trim)
                .filter(type -> !type.isEmpty())
                .map(type -> type.toUpperCase(Locale.ROOT))
                .distinct()
                .toList();
        if (types.stream().anyMatch(type -> !allowed.contains(type))) {
            throw new IllegalArgumentException("Tipo de balneário inválido");
        }
        return types.isEmpty() ? null : String.join(";", types);
    }

    private String environmentType(Venue venue) {
        if (venue.getEnvironmentType() != null) return venue.getEnvironmentType().name();
        return venue.isIndoor() ? Venue.EnvironmentType.INDOOR.name() : Venue.EnvironmentType.OUTDOOR.name();
    }

    private String ownershipType(Venue venue) {
        return venue.getOwnershipType() == null ? null : venue.getOwnershipType().name();
    }

    private String informationSource(Venue venue) {
        return venue.getInformationSource() == null
                ? Venue.InformationSource.COMMUNITY.name()
                : venue.getInformationSource().name();
    }

    private boolean hasSport(String sports, String sportQuery) {
        if (sports == null) return false;
        return java.util.Arrays.stream(sports.split(";"))
                .map(this::normalize)
                .anyMatch(sportQuery::equals);
    }

    private String normalize(String value) {
        String trimmed = trimToNull(value);
        return trimmed == null ? null : trimmed.toLowerCase(Locale.ROOT);
    }

    private String requireText(String value, String message) {
        String normalized = trimToNull(value);
        if (normalized == null) {
            throw new IllegalArgumentException(message);
        }
        return normalized;
    }

    private String trimToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
