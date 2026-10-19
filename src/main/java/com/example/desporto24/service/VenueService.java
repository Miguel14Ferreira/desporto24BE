package com.example.desporto24.service;

import com.example.desporto24.constant.SportsCatalog;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.venue.CreateVenueRequest;
import com.example.desporto24.model.dto.venue.VenueSearchPageResponse;
import com.example.desporto24.model.dto.venue.OpenStreetMapVenueImportRequest;
import com.example.desporto24.model.dto.venue.VenueResponse;
import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.text.Normalizer;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Service
@RequiredArgsConstructor
@Transactional
public class VenueService {

    private static final double EARTH_RADIUS_KM = 6371.0088;
    private static final int VENUE_SUGGEST_PAGE_SIZE = 40;
    private static final int VENUE_SUGGEST_MAX_PAGE = 1000;

    private final VenueRepository venueRepository;
    private final ProfileRepository profileRepository;
    private final SessionDistanceCalculator distanceCalculator;

    @Transactional(readOnly = true)
    public List<VenueResponse> search(String city, String sport) {
        return search(city, sport, null, null, null);
    }

    @Transactional(readOnly = true)
    public List<VenueResponse> search(
            String city, String sport, Double latitude, Double longitude, Double radiusKm) {
        String sportQuery = normalize(sport);
        boolean geoSearch = latitude != null || longitude != null || radiusKm != null;
        if (geoSearch) {
            validateGeoSearch(latitude, longitude, radiusKm);
            double radius = radiusKm == null ? 25.0 : radiusKm;
            // When coordinates are provided, the radius replaces the textual city filter.
            return nearbyCandidates(latitude, longitude, radius).stream()
                    .filter(venue -> sportQuery == null
                            || (venue.getSports() != null && hasSport(venue.getSports(), sportQuery)))
                    .filter(venue -> venue.getLatitude() != null && venue.getLongitude() != null)
                    .map(venue -> new NearbyVenue(venue, distanceCalculator.kilometres(
                            latitude, longitude, venue.getLatitude(), venue.getLongitude())))
                    .filter(candidate -> candidate.distanceKm() <= radius)
                    .sorted(Comparator.comparingDouble(NearbyVenue::distanceKm)
                            .thenComparing(candidate -> candidate.venue().getName()))
                    .map(candidate -> toResponse(candidate.venue()))
                    .toList();
        }

        String cityQuery = normalize(city);
        List<Venue> venues = cityQuery == null
                ? venueRepository.findAllByOrderByNameAsc()
                : venueRepository.findByCityContainingIgnoreCaseOrderByNameAsc(cityQuery);

        return venues.stream()
                .filter(venue -> sportQuery == null
                        || (venue.getSports() != null && hasSport(venue.getSports(), sportQuery)))
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VenueSearchPageResponse suggest(String name, String city, String sport, int page) {
        if (page < 0 || page > VENUE_SUGGEST_MAX_PAGE) {
            throw new IllegalArgumentException("Página de pesquisa de recintos inválida");
        }
        String sportFilter = normalizeSearchFilter(sport).replace(" ", "");
        var venues = venueRepository.suggest(
                escapeSearchPattern(normalizeSearchFilter(name)),
                escapeSearchPattern(normalizeSearchFilter(city)),
                escapeSearchPattern(sportFilter),
                PageRequest.of(page, VENUE_SUGGEST_PAGE_SIZE));
        return new VenueSearchPageResponse(
                venues.getContent().stream().map(this::toResponse).toList(),
                page, venues.hasNext());
    }

    private static String normalizeSearchFilter(String input) {
        if (input == null || input.isBlank()) return "";
        String trimmed = input.trim();
        if (trimmed.length() > 120) {
            throw new IllegalArgumentException("Filtro de recintos demasiado comprido");
        }
        return Normalizer.normalize(trimmed.toLowerCase(Locale.ROOT), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
    }

    private static String escapeSearchPattern(String input) {
        return input.replace("!", "!!").replace("%", "!%").replace("_", "!_");
    }

    private record NearbyVenue(Venue venue, double distanceKm) {}

    private static void validateGeoSearch(Double latitude, Double longitude, Double radiusKm) {
        if (latitude == null || longitude == null
                || !Double.isFinite(latitude) || !Double.isFinite(longitude)
                || Math.abs(latitude) > 90 || Math.abs(longitude) > 180
                || (radiusKm != null && (!Double.isFinite(radiusKm)
                || radiusKm <= 0 || radiusKm > 100))) {
            throw new IllegalArgumentException("Coordenadas ou raio de pesquisa inválidos");
        }
    }

    private List<Venue> nearbyCandidates(double latitude, double longitude, double radiusKm) {
        double angularRadius = radiusKm / EARTH_RADIUS_KM;
        double latitudeDelta = Math.toDegrees(angularRadius);
        double minLatitude = Math.max(-90.0, latitude - latitudeDelta);
        double maxLatitude = Math.min(90.0, latitude + latitudeDelta);

        // Near the poles every longitude is a candidate.
        if (minLatitude <= -90.0 || maxLatitude >= 90.0) {
            return nearbyBox(minLatitude, maxLatitude, -180.0, 180.0);
        }

        double longitudeRatio = Math.sin(angularRadius) / Math.cos(Math.toRadians(latitude));
        double longitudeDelta = longitudeRatio >= 1.0
                ? 180.0 : Math.toDegrees(Math.asin(longitudeRatio));
        if (longitudeDelta >= 180.0) {
            return nearbyBox(minLatitude, maxLatitude, -180.0, 180.0);
        }
        double minLongitude = longitude - longitudeDelta;
        double maxLongitude = longitude + longitudeDelta;
        if (minLongitude < -180.0) {
            List<Venue> candidates = new ArrayList<>(
                    nearbyBox(minLatitude, maxLatitude, -180.0, maxLongitude));
            candidates.addAll(nearbyBox(minLatitude, maxLatitude, minLongitude + 360.0, 180.0));
            return candidates;
        }
        if (maxLongitude > 180.0) {
            List<Venue> candidates = new ArrayList<>(
                    nearbyBox(minLatitude, maxLatitude, minLongitude, 180.0));
            candidates.addAll(nearbyBox(minLatitude, maxLatitude, -180.0, maxLongitude - 360.0));
            return candidates;
        }
        return nearbyBox(minLatitude, maxLatitude, minLongitude, maxLongitude);
    }

    private List<Venue> nearbyBox(double minLat, double maxLat, double minLon, double maxLon) {
        return venueRepository.findByLatitudeBetweenAndLongitudeBetweenOrderByNameAsc(
                minLat, maxLat, minLon, maxLon);
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
                        request.longitude(),
                        request.district()))
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
        return upsertOpenStreetMapVenue(sourceReference, name, city, address,
                sports, venueType, environmentType, latitude, longitude, null);
    }

    public VenueResponse upsertOpenStreetMapVenue(
            String sourceReference, String name, String city, String address,
            String sports, String venueType, Venue.EnvironmentType environmentType,
            Double latitude, Double longitude, String district) {
        String normalizedSourceReference = requireText(sourceReference, "Referência externa obrigatória");
        String normalizedName = requireText(name, "Nome do recinto obrigatório");
        String normalizedCity = requireText(city, "Cidade do recinto obrigatória");
        String normalizedAddress = requireText(address, "Morada do recinto obrigatória");

        String normalizedSports = trimToNull(sports);
        if (normalizedSports != null && java.util.Arrays.stream(normalizedSports.split(";", -1))
                .map(String::trim)
                .anyMatch(sport -> !SportsCatalog.ALLOWED.contains(sport))) {
            throw new IllegalArgumentException("Modalidade inválida no recinto");
        }

        String stableReference = normalizedSourceReference.replaceFirst(
                "(?i)^OSM:(NODE|WAY|RELATION)[/:]([0-9]+)$", "OSM:$1:$2");
        String[] parts = stableReference.split(":", -1);
        if (parts.length == 3 && parts[0].equalsIgnoreCase("OSM")) {
            stableReference = "OSM:" + parts[1].toUpperCase(java.util.Locale.ROOT) + ":" + parts[2];
        }
        if (!stableReference.matches("OSM:(NODE|WAY|RELATION):[1-9][0-9]*")) {
            throw new IllegalArgumentException("Invalid OSM source reference");
        }
        if (latitude == null || longitude == null
                || !Double.isFinite(latitude) || !Double.isFinite(longitude)
                || latitude < 36.8 || latitude > 42.3 || longitude < -9.6 || longitude > -6.0) {
            throw new IllegalArgumentException("Coordinates must be in mainland Portugal");
        }
        String normalizedDistrict = trimToNull(district);
        if (normalizedDistrict != null && !java.util.Set.of(
                "Aveiro", "Beja", "Braga", "Bragança", "Castelo Branco", "Coimbra",
                "Évora", "Faro", "Guarda", "Leiria", "Lisboa", "Portalegre",
                "Porto", "Santarém", "Setúbal", "Viana do Castelo", "Vila Real", "Viseu"
        ).contains(normalizedDistrict)) {
            throw new IllegalArgumentException("Unknown mainland Portugal district");
        }
        Venue existingBySource = venueRepository.findByInformationSourceAndSourceReference(
                Venue.InformationSource.OPENSTREETMAP, stableReference).orElse(null);
        if (existingBySource == null && latitude != null && longitude != null) {
            // Reuse an exact matching venue from any source without overwriting it.
            // Narrow geographic candidate check, without merging different named courts.
            List<Venue> nearby = venueRepository
                    .findByLatitudeBetweenAndLongitudeBetweenOrderByNameAsc(
                            latitude - 0.00023, latitude + 0.00023,
                            longitude - 0.00035, longitude + 0.00035);
            String normalizedComparable = normalizedName.toLowerCase(java.util.Locale.ROOT)
                    .replaceAll("[^\\p{L}\\p{N}]+", "");
            for (Venue candidate : nearby) {
                if (candidate.getLatitude() == null || candidate.getLongitude() == null
                        || candidate.getName() == null) continue;
                String otherName = candidate.getName().toLowerCase(java.util.Locale.ROOT)
                        .replaceAll("[^\\p{L}\\p{N}]+", "");
                if (normalizedComparable.equals(otherName)
                        && distanceCalculator.kilometres(
                                latitude, longitude,
                                candidate.getLatitude(), candidate.getLongitude()) <= 0.03) {
                    return toResponse(candidate);
                }
            }
        }
        Venue venue = existingBySource != null ? existingBySource : Venue.builder()
                .informationSource(Venue.InformationSource.OPENSTREETMAP)
                .sourceReference(stableReference)
                .createdAt(Instant.now())
                .build();

        venue.setName(normalizedName);
        if (normalizedDistrict != null) venue.setDistrict(normalizedDistrict);
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

        if (venueRepository.existsByNameIgnoreCaseAndCityIgnoreCaseAndAddressIgnoreCase(
                request.name().trim(), request.city().trim(), request.address().trim())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT,
                    "Já existe um recinto com este nome, cidade e morada");
        }

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
