package com.example.desporto24.configuration;

import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.repository.venue.VenueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CascaisVenueImportData implements ApplicationRunner {

    private static final String PACKAGE_URL =
            "https://dadosabertos.cascais.pt/api/3/action/package_show?id=geocascais-equipamentodesportivo";
    private static final String SOURCE_PREFIX = "CASCAIS:SPORTS:";

    private final VenueRepository venueRepository;

    private final RestClient restClient = RestClient.builder()
            .build();

    @Override
    public void run(ApplicationArguments args) {
        try {
            String geoJsonUrl = findGeoJsonUrl();
            if (geoJsonUrl == null || geoJsonUrl.isBlank()) {
                log.warn("Cascais venue import skipped: GeoJSON resource not found");
                return;
            }

            Map<?, ?> geoJson = restClient.get()
                    .uri(geoJsonUrl)
                    .retrieve()
                    .body(Map.class);

            if (geoJson == null || !(geoJson.get("features") instanceof List<?> features)) {
                log.warn("Cascais venue import skipped: invalid GeoJSON payload");
                return;
            }

            int inserted = 0;
            int updatedSports = 0;
            int skipped = 0;
            List<Venue> knownVenues = venueRepository.findAllByOrderByNameAsc();

            for (Object featureValue : features) {
                if (!(featureValue instanceof Map<?, ?> feature)) {
                    skipped++;
                    continue;
                }

                Map<?, ?> properties = asMap(feature.get("properties"));
                Map<?, ?> geometry = asMap(feature.get("geometry"));

                String fid = firstText(properties, "FID", "fid", "OBJECTID", "objectid", "id");
                if (fid == null) {
                    fid = clean(string(feature.get("id")));
                }
                String name = firstText(
                        properties,
                        "nome", "NOME",
                        "designacao", "DESIGNACAO",
                        "name", "NAME");

                double[] coordinates = geometryCoordinates(geometry);
                if (fid == null || name == null || coordinates == null) {
                    skipped++;
                    continue;
                }

                String sourceReference = SOURCE_PREFIX + fid;
                String sports = inferSports(properties, name);

                var existing = venueRepository.findByInformationSourceAndSourceReference(
                        Venue.InformationSource.OFFICIAL,
                        sourceReference);

                if (existing.isPresent()) {
                    Venue venue = existing.get();
                    if ((venue.getSports() == null || venue.getSports().isBlank())
                            && sports != null) {
                        venue.setSports(limit(sports, 255));
                        venueRepository.save(venue);
                        updatedSports++;
                    }
                    skipped++;
                    continue;
                }

                if (isNearExistingVenue(knownVenues, name, coordinates[1], coordinates[0])) {
                    skipped++;
                    continue;
                }

                String parish = fallback(
                        firstText(properties, "freguesia", "FREGUESIA", "localidade", "LOCALIDADE"),
                        "Cascais");
                String address = buildAddress(properties, parish);

                Venue savedVenue = venueRepository.save(Venue.builder()
                        .name(limit(name, 140))
                        .city(limit(parish, 120))
                        .district("Lisboa")
                        .address(limit(address, 255))
                        .sports(limit(sports, 255))
                        .venueType("Equipamento desportivo")
                        .website(limit(firstText(
                                properties,
                                "endereco_internet", "ENDERECO_INTERNET",
                                "website", "WEBSITE", "url", "URL"), 255))
                        .contact(limit(buildContact(properties), 255))
                        .latitude(coordinates[1])
                        .longitude(coordinates[0])
                        .informationSource(Venue.InformationSource.OFFICIAL)
                        .sourceReference(limit(sourceReference, 255))
                        .createdAt(Instant.now())
                        .build());

                knownVenues.add(savedVenue);
                inserted++;
            }

            long cascaisTotal = venueRepository.findAllByOrderByNameAsc().stream()
                    .filter(venue -> venue.getInformationSource() == Venue.InformationSource.OFFICIAL)
                    .filter(venue -> venue.getSourceReference() != null
                            && venue.getSourceReference().startsWith(SOURCE_PREFIX))
                    .count();

            long cascaisClassified = venueRepository.findAllByOrderByNameAsc().stream()
                    .filter(venue -> venue.getInformationSource() == Venue.InformationSource.OFFICIAL)
                    .filter(venue -> venue.getSourceReference() != null
                            && venue.getSourceReference().startsWith(SOURCE_PREFIX))
                    .filter(venue -> venue.getSports() != null && !venue.getSports().isBlank())
                    .count();

            log.info(
                    "Cascais venue import completed: inserted={}, sportsUpdated={}, skipped={}, total={}, classified={}, unclassified={}",
                    inserted,
                    updatedSports,
                    skipped,
                    features.size(),
                    cascaisClassified,
                    cascaisTotal - cascaisClassified);
        } catch (Exception ex) {
            log.warn("Cascais venue import failed without blocking startup: {}", ex.getMessage());
        }
    }

    private boolean isNearExistingVenue(List<Venue> venues, String name, double latitude, double longitude) {
        String normalizedName = name.trim().toLowerCase();

        for (Venue venue : venues) {
            if (venue.getLatitude() == null || venue.getLongitude() == null) {
                continue;
            }

            double distance = distanceMeters(
                    latitude,
                    longitude,
                    venue.getLatitude(),
                    venue.getLongitude());

            if (distance <= 35.0) {
                return true;
            }

            if (distance <= 100.0
                    && venue.getName() != null
                    && venue.getName().trim().toLowerCase().equals(normalizedName)) {
                return true;
            }
        }

        return false;
    }

    private double distanceMeters(double lat1, double lon1, double lat2, double lon2) {
        double earthRadiusMeters = 6_371_000.0;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusMeters * c;
    }

    private String findGeoJsonUrl() {
        Map<?, ?> packageResponse = restClient.get()
                .uri(PACKAGE_URL)
                .retrieve()
                .body(Map.class);

        if (packageResponse == null || !(packageResponse.get("result") instanceof Map<?, ?> result)) {
            return null;
        }

        if (!(result.get("resources") instanceof List<?> resources)) {
            return null;
        }

        for (Object resourceValue : resources) {
            if (!(resourceValue instanceof Map<?, ?> resource)) {
                continue;
            }

            String format = string(resource.get("format"));
            String url = string(resource.get("url"));

            if ("geojson".equalsIgnoreCase(format) && url != null && !url.isBlank()) {
                return url;
            }
        }

        return null;
    }

    private double[] geometryCoordinates(Map<?, ?> geometry) {
        if (geometry == null) {
            return null;
        }

        Object coordinates = geometry.get("coordinates");
        CoordinateAccumulator accumulator = new CoordinateAccumulator();
        collectCoordinates(coordinates, accumulator);

        if (accumulator.count == 0) {
            return null;
        }

        double lon = accumulator.longitudeSum / accumulator.count;
        double lat = accumulator.latitudeSum / accumulator.count;

        if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
            return null;
        }

        return new double[]{lon, lat};
    }

    private void collectCoordinates(Object value, CoordinateAccumulator accumulator) {
        if (!(value instanceof List<?> list) || list.isEmpty()) {
            return;
        }

        if (list.size() >= 2
                && list.get(0) instanceof Number longitude
                && list.get(1) instanceof Number latitude) {
            double lon = longitude.doubleValue();
            double lat = latitude.doubleValue();

            if (lat >= -90 && lat <= 90 && lon >= -180 && lon <= 180) {
                accumulator.longitudeSum += lon;
                accumulator.latitudeSum += lat;
                accumulator.count++;
            }
            return;
        }

        for (Object nested : list) {
            collectCoordinates(nested, accumulator);
        }
    }

    private static final class CoordinateAccumulator {
        private double longitudeSum;
        private double latitudeSum;
        private int count;
    }

    private String inferSports(Map<?, ?> properties, String name) {
        String official = firstText(
                properties,
                "modalidade", "MODALIDADE",
                "modalidades", "MODALIDADES",
                "desporto", "DESPORTO",
                "desportos", "DESPORTOS",
                "atividade", "ATIVIDADE",
                "atividades", "ATIVIDADES");

        if (official != null) {
            return normalizeSports(official);
        }

        String value = name == null ? "" : name.toLowerCase(java.util.Locale.ROOT);
        java.util.LinkedHashSet<String> sports = new java.util.LinkedHashSet<>();

        addSportIf(value, sports, "futebol", "Futebol");
        addSportIf(value, sports, "futsal", "Futsal");
        addSportIf(value, sports, "padel", "Padel");
        addSportIf(value, sports, "ténis", "Ténis");
        addSportIf(value, sports, "tenis", "Ténis");
        addSportIf(value, sports, "basquet", "Basquetebol");
        addSportIf(value, sports, "voleib", "Voleibol");
        addSportIf(value, sports, "andebol", "Andebol");
        addSportIf(value, sports, "rugby", "Rugby");
        addSportIf(value, sports, "hóquei", "Hóquei");
        addSportIf(value, sports, "hoquei", "Hóquei");
        addSportIf(value, sports, "natação", "Natação");
        addSportIf(value, sports, "natacao", "Natação");
        addSportIf(value, sports, "piscina", "Natação");
        addSportIf(value, sports, "atlet", "Atletismo");
        addSportIf(value, sports, "skate", "Skate");
        addSportIf(value, sports, "bmx", "BMX");
        addSportIf(value, sports, "ginásio", "Ginásio");
        addSportIf(value, sports, "ginasio", "Ginásio");
        addSportIf(value, sports, "ginástica", "Ginástica");
        addSportIf(value, sports, "ginastica", "Ginástica");

        return sports.isEmpty() ? null : String.join(";", sports);
    }

    private String normalizeSports(String value) {
        if (value == null) {
            return null;
        }

        String normalized = value
                .replace(",", ";")
                .replace("/", ";")
                .replace("|", ";");

        java.util.LinkedHashSet<String> sports = new java.util.LinkedHashSet<>();
        for (String item : normalized.split(";")) {
            String trimmed = item.trim();
            if (!trimmed.isEmpty()) {
                sports.add(trimmed);
            }
        }

        return sports.isEmpty() ? null : String.join(";", sports);
    }

    private void addSportIf(
            String value,
            java.util.Set<String> sports,
            String token,
            String sport) {
        if (value.contains(token)) {
            sports.add(sport);
        }
    }

    private String buildAddress(Map<?, ?> properties, String parish) {
        String street = firstText(properties, "rua", "RUA", "morada", "MORADA", "endereco", "ENDERECO");
        String policeNumber = firstText(properties, "numero_policia", "NUMERO_POLICIA", "porta", "PORTA");
        String postalCode = firstText(properties, "codigo_postal", "CODIGO_POSTAL", "cp", "CP");

        StringBuilder address = new StringBuilder();

        append(address, street);
        append(address, policeNumber);
        append(address, postalCode);
        append(address, parish);

        return address.length() == 0 ? "Cascais" : address.toString();
    }

    private String buildContact(Map<?, ?> properties) {
        String phone = firstText(properties, "telefone", "TELEFONE", "phone", "PHONE");
        String email = firstText(
                properties,
                "endereco_correio_eletronico", "ENDERECO_CORREIO_ELETRONICO",
                "email", "EMAIL");

        if (phone == null) {
            return email;
        }
        if (email == null) {
            return phone;
        }
        return phone + " · " + email;
    }

    private void append(StringBuilder builder, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        if (builder.length() > 0) {
            builder.append(", ");
        }
        builder.append(value.trim());
    }

    private Map<?, ?> asMap(Object value) {
        return value instanceof Map<?, ?> map ? map : null;
    }

    private String firstText(Map<?, ?> map, String... keys) {
        if (map == null) {
            return null;
        }

        for (String key : keys) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (entry.getKey() != null
                        && key.equalsIgnoreCase(String.valueOf(entry.getKey()))) {
                    String value = clean(string(entry.getValue()));
                    if (value != null) {
                        return value;
                    }
                }
            }
        }

        return null;
    }

    private String string(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private String clean(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() || "null".equalsIgnoreCase(trimmed) ? null : trimmed;
    }

    private String fallback(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private String limit(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }
}
