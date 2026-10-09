package com.example.desporto24.configuration;

import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.repository.venue.VenueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnidVenueImportData {

    private static final String DATASET_SEARCH_URL =
            "https://dados.gov.pt/api/1/datasets/?q=Sistema%20Nacional%20de%20Informa%C3%A7%C3%A3o%20Desportiva";
    private static final String SOURCE_PREFIX = "SNID:";
    private static final int SAVE_BATCH_SIZE = 500;

    private final VenueRepository venueRepository;

    private final RestClient restClient = RestClient.builder().build();

    public void importVenues() {
        try {
            String geoJsonUrl = findSnidGeoJsonUrl();
            if (geoJsonUrl == null || geoJsonUrl.isBlank()) {
                throw new IllegalStateException("SNID GeoJSON resource not found");
            }

            Map<?, ?> geoJson = restClient.get()
                    .uri(geoJsonUrl)
                    .retrieve()
                    .body(Map.class);

            if (geoJson == null || !(geoJson.get("features") instanceof List<?> features)) {
                throw new IllegalStateException("Invalid SNID GeoJSON payload");
            }

            List<Venue> existingVenues = venueRepository.findAllByOrderByNameAsc();
            Set<String> sourceReferences = new HashSet<>();
            Set<String> venueKeys = new HashSet<>();

            for (Venue venue : existingVenues) {
                if (venue.getSourceReference() != null) {
                    sourceReferences.add(venue.getSourceReference());
                }
                String key = venueKey(
                        venue.getName(),
                        venue.getLatitude(),
                        venue.getLongitude());
                if (key != null) {
                    venueKeys.add(key);
                }
            }

            int inserted = 0;
            int skipped = 0;
            List<Venue> batch = new ArrayList<>(SAVE_BATCH_SIZE);

            for (Object featureValue : features) {
                if (!(featureValue instanceof Map<?, ?> feature)) {
                    skipped++;
                    continue;
                }

                Map<?, ?> properties = asMap(feature.get("properties"));
                Map<?, ?> geometry = asMap(feature.get("geometry"));

                String name = firstText(
                        properties,
                        "nome", "name", "designacao", "denominacao",
                        "instalacao", "instalação", "infraestrutura");

                double[] coordinates = geometryCoordinates(geometry);
                if (name == null || coordinates == null) {
                    skipped++;
                    continue;
                }

                String municipality = firstText(
                        properties,
                        "concelho", "municipio", "município",
                        "localidade", "freguesia", "city");

                String district = firstText(
                        properties,
                        "distrito", "district");

                String address = firstText(
                        properties,
                        "morada", "endereco", "endereço",
                        "address", "rua");

                String sports = normalizeSports(firstText(
                        properties,
                        "modalidade", "modalidades",
                        "desporto", "desportos",
                        "atividade", "atividades",
                        "disciplina", "disciplinas"));

                String venueType = firstText(
                        properties,
                        "tipo", "tipologia",
                        "tipo_instalacao", "tipo instalação",
                        "tipo_de_instalacao", "tipo de instalação",
                        "categoria");

                String rawId = firstText(
                        properties,
                        "id", "uuid", "codigo", "código",
                        "id_instalacao", "id instalação",
                        "identificador");

                String sourceReference = SOURCE_PREFIX + stableId(
                        rawId,
                        name,
                        municipality,
                        coordinates[1],
                        coordinates[0]);

                String key = venueKey(name, coordinates[1], coordinates[0]);

                if (sourceReferences.contains(sourceReference)
                        || (key != null && venueKeys.contains(key))) {
                    skipped++;
                    continue;
                }

                String city = fallback(municipality, fallback(district, "Portugal"));
                String resolvedAddress = fallback(
                        address,
                        name + (municipality == null ? "" : ", " + municipality));

                Venue venue = Venue.builder()
                        .name(limit(name, 140))
                        .city(limit(city, 120))
                        .district(limit(district, 120))
                        .address(limit(resolvedAddress, 255))
                        .sports(limit(sports, 255))
                        .venueType(limit(venueType, 120))
                        .latitude(coordinates[1])
                        .longitude(coordinates[0])
                        .informationSource(Venue.InformationSource.OFFICIAL)
                        .sourceReference(limit(sourceReference, 255))
                        .createdAt(Instant.now())
                        .build();

                batch.add(venue);
                sourceReferences.add(sourceReference);
                if (key != null) {
                    venueKeys.add(key);
                }

                if (batch.size() >= SAVE_BATCH_SIZE) {
                    venueRepository.saveAll(batch);
                    inserted += batch.size();
                    batch.clear();
                }
            }

            if (!batch.isEmpty()) {
                venueRepository.saveAll(batch);
                inserted += batch.size();
            }

            log.info(
                    "SNID venue import completed: inserted={}, skipped={}, total={}, source={}",
                    inserted,
                    skipped,
                    features.size(),
                    geoJsonUrl);
        } catch (Exception ex) {
            log.error("SNID venue import failed: {}", ex.getMessage(), ex);
            throw new IllegalStateException("SNID venue import failed", ex);
        }
    }

    private String findSnidGeoJsonUrl() {
        Map<?, ?> response = restClient.get()
                .uri(DATASET_SEARCH_URL)
                .retrieve()
                .body(Map.class);

        if (response == null || !(response.get("data") instanceof List<?> datasets)) {
            return null;
        }

        for (Object datasetValue : datasets) {
            if (!(datasetValue instanceof Map<?, ?> dataset)) {
                continue;
            }

            String title = clean(string(dataset.get("title")));
            if (title == null
                    || !title.toLowerCase(Locale.ROOT)
                    .contains("sistema nacional de informação desportiva")) {
                continue;
            }

            if (!(dataset.get("resources") instanceof List<?> resources)) {
                continue;
            }

            for (Object resourceValue : resources) {
                if (!(resourceValue instanceof Map<?, ?> resource)) {
                    continue;
                }

                String format = clean(string(resource.get("format")));
                String url = clean(string(resource.get("url")));

                if (url != null
                        && format != null
                        && format.toLowerCase(Locale.ROOT).contains("geojson")) {
                    log.info("SNID GeoJSON resource selected: {}", url);
                    return url;
                }
            }
        }

        return null;
    }

    private String stableId(
            String rawId,
            String name,
            String municipality,
            double latitude,
            double longitude) {
        if (rawId != null) {
            return rawId;
        }

        String material = String.join(
                "|",
                name == null ? "" : name,
                municipality == null ? "" : municipality,
                String.format(Locale.ROOT, "%.6f", latitude),
                String.format(Locale.ROOT, "%.6f", longitude));

        return UUID.nameUUIDFromBytes(
                material.getBytes(StandardCharsets.UTF_8))
                .toString();
    }

    private String venueKey(
            String name,
            Double latitude,
            Double longitude) {
        if (name == null || latitude == null || longitude == null) {
            return null;
        }

        return normalize(name)
                + "|"
                + String.format(Locale.ROOT, "%.4f", latitude)
                + "|"
                + String.format(Locale.ROOT, "%.4f", longitude);
    }

    private String normalize(String value) {
        return value == null
                ? ""
                : value.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
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

    private static final class CoordinateAccumulator {
        private double longitudeSum;
        private double latitudeSum;
        private int count;
    }
}
