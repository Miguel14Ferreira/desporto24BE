package com.example.desporto24.configuration;

import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.repository.venue.VenueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private static final String[] DATASET_SEARCH_URLS = {
            DATASET_SEARCH_URL,
            "https://dados.gov.pt/api/1/datasets/?q=SNID",
            "https://dados.gov.pt/api/1/datasets/?q=instala%C3%A7%C3%B5es%20desportivas"
    };
    private static final String SOURCE_PREFIX = "SNID:";
    private static final int SAVE_BATCH_SIZE = 500;
    private static final Pattern JSON_KEY = Pattern.compile("\\\"([^\\\"]{1,80})\\\"\\s*:");

    private final VenueRepository venueRepository;

    private final RestClient restClient = RestClient.builder().build();

    /** Read-only check of the remote GeoJSON and approximate Portuguese coverage. */
    /** Validate the official SNID location JSON without persisting records or loading the file into memory. */
    public Map<String, Object> validateSource() {
        String url = findSnidLocationJsonUrl();
        if (url == null) throw new IllegalStateException("SNID location JSON resource not found");
        HttpURLConnection connection = null;
        try {
            URI uri = URI.create(url);
            if (!"https".equalsIgnoreCase(uri.getScheme())
                    || !"dados.gov.pt".equalsIgnoreCase(uri.getHost())) {
                throw new IllegalArgumentException("Unexpected SNID resource host");
            }
            connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(15000);
            connection.setInstanceFollowRedirects(false);
            if (connection.getResponseCode() != 200) {
                throw new IllegalStateException("SNID JSON HTTP " + connection.getResponseCode());
            }
            int total = 0, valid = 0, mainland = 0, madeira = 0, azores = 0;
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            try (var input = connection.getInputStream();
                 var parser = mapper.getFactory().createParser(input)) {
                while (parser.nextToken() != null) {
                    var record = mapper.readTree(parser);
                    total++;
                    if (record == null || !record.path("name").isTextual()
                            || record.path("name").asText().isBlank()) continue;
                    com.fasterxml.jackson.databind.JsonNode location;
                    try {
                        location = mapper.readTree(record.path("location").asText(""));
                    } catch (com.fasterxml.jackson.core.JsonProcessingException ex) {
                        continue;
                    }
                    if (location == null || !"Point".equals(location.path("type").asText())) continue;
                    var coords = location.path("coordinates");
                    if (!coords.isArray() || coords.size() != 2
                            || !coords.get(0).isNumber() || !coords.get(1).isNumber()) continue;
                    double lon = coords.get(0).asDouble(), lat = coords.get(1).asDouble();
                    if (!Double.isFinite(lon) || !Double.isFinite(lat)) continue;
                    valid++;
                    if (lat >= 36.9 && lat <= 42.2 && lon >= -9.6 && lon <= -6.1) mainland++;
                    else if (lat >= 32.0 && lat <= 33.3 && lon >= -17.5 && lon <= -15.5) madeira++;
                    else if (lat >= 36.8 && lat <= 40.2 && lon >= -31.5 && lon <= -24.5) azores++;
                }
            }
            return Map.of("total", total, "valid", valid, "mainland", mainland,
                    "madeira", madeira, "azores", azores,
                    "otherValid", valid - mainland - madeira - azores, "source", url);
        } catch (java.io.IOException ex) {
            throw new IllegalStateException("SNID JSON validation failed", ex);
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    private String findSnidLocationJsonUrl() {
        for (String searchUrl : DATASET_SEARCH_URLS) {
            try {
                Map<?, ?> response = restClient.get().uri(searchUrl).retrieve().body(Map.class);
                if (response == null || !(response.get("data") instanceof List<?> datasets)) continue;
                for (Object item : datasets) {
                    if (!(item instanceof Map<?, ?> dataset)) continue;
                    String title = clean(string(dataset.get("title")));
                    if (title == null || !title.toLowerCase(Locale.ROOT)
                            .contains("sistema nacional de informação desportiva")) continue;
                    if (!(dataset.get("resources") instanceof List<?> resources)) continue;
                    for (Object resourceValue : resources) {
                        if (!(resourceValue instanceof Map<?, ?> resource)) continue;
                        String resourceTitle = clean(string(resource.get("title")));
                        String format = clean(string(resource.get("format")));
                        String url = clean(string(resource.get("url")));
                        if (resourceTitle != null && resourceTitle.startsWith("snid-localizacao")
                                && format != null && format.equalsIgnoreCase("json")
                                && resource.get("filesize") instanceof Number size && size.longValue() > 0
                                && url != null) return url;
                    }
                }
            } catch (org.springframework.web.client.RestClientException ex) {
                log.warn("SNID location dataset search unavailable for {}: {}", searchUrl,
                        ex.getClass().getSimpleName());
            }
        }
        return null;
    }

    public ImportResult importVenues() {
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
            return new ImportResult(features.size(), inserted, skipped);
        } catch (Exception ex) {
            log.error("SNID venue import failed: {}", ex.getMessage(), ex);
            throw new IllegalStateException("SNID venue import failed", ex);
        }
    }

    public record ImportResult(int processed, int inserted, int skipped) {}

    private String findSnidGeoJsonUrl() {
        for (String searchUrl : DATASET_SEARCH_URLS) {
            try {
                String found = findSnidGeoJsonUrl(searchUrl);
                if (found != null) return found;
            } catch (org.springframework.web.client.RestClientException ex) {
                log.warn("SNID dataset search unavailable for {}: {}", searchUrl,
                        ex.getClass().getSimpleName());
            }
        }
        return null;
    }

    private String findSnidGeoJsonUrl(String searchUrl) {
        Map<?, ?> response = restClient.get()
                .uri(searchUrl)
                .retrieve()
                .body(Map.class);

        if (response == null || !(response.get("data") instanceof List<?> datasets)) {
            log.warn("SNID dataset search returned invalid response for {}", searchUrl);
            return null;
        }

        log.info("SNID dataset search returned {} candidate datasets for {}", datasets.size(), searchUrl);

        boolean inspected = false;
        for (Object datasetValue : datasets) {
            if (!(datasetValue instanceof Map<?, ?> dataset)) {
                continue;
            }

            String title = clean(string(dataset.get("title")));
            log.info("SNID dataset candidate: title={}, resources={}",
                    title, dataset.get("resources") instanceof List<?> items ? items.size() : 0);
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

                log.info("SNID matching dataset resource: format={}, urlPresent={}, title={}, mime={}, size={}",
                        format, url != null, clean(string(resource.get("title"))),
                        clean(string(resource.get("mime"))), resource.get("filesize"));
                if (url != null && format != null && format.equalsIgnoreCase("json")
                        && clean(string(resource.get("title"))) != null
                        && clean(string(resource.get("title"))).startsWith("snid-localizacao")
                        && resource.get("filesize") instanceof Number size && size.longValue() > 0) {
                    log.info("SNID JSON resource available for read-only inspection: title={}, url={}",
                            clean(string(resource.get("title"))), url);
                    if (!inspected && clean(string(resource.get("title"))).startsWith("snid-localizacao")) {
                        inspected = true;
                        inspectJsonPrefix(url);
                    }
                }
                String mime = clean(string(resource.get("mime")));
                String resourcePath = url == null ? null : URI.create(url).getPath();
                boolean geoJsonFormat = format != null
                        && format.toLowerCase(Locale.ROOT).contains("geojson");
                boolean geoJsonMime = mime != null
                        && mime.equalsIgnoreCase("application/geo+json");
                boolean geoJsonFile = resourcePath != null
                        && resourcePath.toLowerCase(Locale.ROOT).endsWith(".geojson");
                if (url != null && (geoJsonFormat || geoJsonMime || geoJsonFile)) {
                    log.info("SNID GeoJSON resource selected: {}", url);
                    return url;
                }
            }
        }

        return null;
    }

    /** Inspect only the first 8 KiB; never log record values or persist data. */
    private void inspectJsonPrefix(String resourceUrl) {
        HttpURLConnection connection = null;
        try {
            URI uri = URI.create(resourceUrl);
            if (!"https".equalsIgnoreCase(uri.getScheme())
                    || !"dados.gov.pt".equalsIgnoreCase(uri.getHost())) {
                throw new IllegalArgumentException("Unexpected SNID resource host");
            }
            connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            connection.setRequestProperty("Range", "bytes=0-8191");
            connection.setInstanceFollowRedirects(false);
            int status = connection.getResponseCode();
            if (status != 200 && status != 206) {
                log.warn("SNID JSON prefix inspection returned HTTP {}", status);
                return;
            }
            byte[] prefix;
            try (var stream = connection.getInputStream()) {
                prefix = stream.readNBytes(8192);
            }
            String sample = new String(prefix, StandardCharsets.UTF_8);
            Matcher matcher = JSON_KEY.matcher(sample);
            Set<String> keys = new LinkedHashSet<>();
            while (matcher.find() && keys.size() < 25) keys.add(matcher.group(1));
            log.info("SNID JSON prefix field types: location={}, coordinates={}, address={}, coordinatesTokenPresent={}, escapedCoordinatesKeyPresent={}",
                    jsonFieldShape(sample, "location"), jsonFieldShape(sample, "coordinates"),
                    jsonFieldShape(sample, "address"), sample.contains("coordinates"),
                    sample.contains("\\\"coordinates\\\""));
            log.info("SNID sample record fields: id={}, name={}, category={}, district={}, municipalityInfo={}, dataProvider={}",
                    jsonFieldShape(sample, "id"), jsonFieldShape(sample, "name"),
                    jsonFieldShape(sample, "category"), jsonFieldShape(sample, "district"),
                    jsonFieldShape(sample, "municipalityInfo"), jsonFieldShape(sample, "dataProvider"));
            inspectSnidLocationShape(sample);
            inspectSnidRecordBoundaries(sample);
            // Full SNID streaming validation is intentionally not run during application startup.
            log.info("SNID JSON prefix inspection: httpStatus={}, bytesRead={}, firstChar={}, candidateKeys={}",
                    status, prefix.length, sample.stripLeading().isEmpty() ? "empty"
                            : String.valueOf(sample.stripLeading().charAt(0)), keys);
        } catch (Exception ex) {
            log.warn("SNID JSON prefix inspection unavailable: {}", ex.toString());
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    /** Stream the official JSON records without persisting or retaining the dataset in memory. */
    private void validateSnidStreamReadOnly(String resourceUrl) {
        HttpURLConnection connection = null;
        int total = 0, valid = 0, invalid = 0;
        try {
            URI uri = URI.create(resourceUrl);
            if (!"https".equalsIgnoreCase(uri.getScheme())
                    || !"dados.gov.pt".equalsIgnoreCase(uri.getHost())) {
                throw new IllegalArgumentException("Unexpected SNID resource host");
            }
            connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(15000);
            connection.setInstanceFollowRedirects(false);
            if (connection.getResponseCode() != 200) {
                log.warn("SNID stream validation HTTP {}", connection.getResponseCode());
                return;
            }
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            try (java.io.InputStream input = connection.getInputStream();
                 com.fasterxml.jackson.core.JsonParser parser = mapper.getFactory().createParser(input)) {
                while (parser.nextToken() != null) {
                    com.fasterxml.jackson.databind.JsonNode record = mapper.readTree(parser);
                    total++;
                    boolean usable = false;
                    if (record != null && record.path("name").isTextual()
                            && !record.path("name").asText().isBlank()) {
                        try {
                            com.fasterxml.jackson.databind.JsonNode location =
                                    mapper.readTree(record.path("location").asText(""));
                            com.fasterxml.jackson.databind.JsonNode coords =
                                    location == null ? null : location.path("coordinates");
                            usable = location != null && "Point".equals(location.path("type").asText())
                                    && coords != null && coords.isArray() && coords.size() == 2
                                    && coords.get(0).isNumber() && coords.get(1).isNumber()
                                    && Double.isFinite(coords.get(0).asDouble())
                                    && Double.isFinite(coords.get(1).asDouble());
                        } catch (com.fasterxml.jackson.core.JsonProcessingException ignored) {
                            // Invalid location JSON is counted below.
                        }
                    }
                    if (usable) valid++; else invalid++;
                }
            }
            log.info("SNID full stream validation (read-only): total={}, valid={}, invalid={}",
                    total, valid, invalid);
        } catch (Exception ex) {
            log.warn("SNID full stream validation interrupted after {} records: {}", total,
                    ex.getClass().getSimpleName());
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    /** Count complete top-level JSON records in the bounded prefix, without logging values. */
    private void inspectSnidRecordBoundaries(String sample) {
        int complete = 0;
        int valid = 0;
        int invalid = 0;
        boolean truncated = false;
        try (com.fasterxml.jackson.core.JsonParser parser =
                     new com.fasterxml.jackson.databind.ObjectMapper().getFactory().createParser(sample)) {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            while (complete < 10 && parser.nextToken() != null) {
                com.fasterxml.jackson.databind.JsonNode record = mapper.readTree(parser);
                complete++;
                try {
                    com.fasterxml.jackson.databind.JsonNode location =
                            mapper.readTree(record.path("location").asText(""));
                    com.fasterxml.jackson.databind.JsonNode point =
                            location == null ? null : location.path("coordinates");
                    boolean usable = record.path("name").isTextual()
                            && !record.path("name").asText().isBlank()
                            && location != null && "Point".equals(location.path("type").asText())
                            && point != null && point.isArray() && point.size() == 2
                            && point.get(0).isNumber() && point.get(1).isNumber()
                            && Double.isFinite(point.get(0).asDouble())
                            && Double.isFinite(point.get(1).asDouble());
                    if (usable) valid++; else invalid++;
                } catch (com.fasterxml.jackson.core.JsonProcessingException ex) {
                    invalid++;
                }
            }
        } catch (com.fasterxml.jackson.core.JsonProcessingException ex) {
            truncated = true;
        } catch (java.io.IOException ex) {
            log.warn("SNID record boundary inspection unavailable: {}", ex.toString());
            return;
        }
        log.info("SNID bounded record inspection: completeTopLevelValues={}, valid={}, invalid={}, truncatedAtPrefix={}",
                complete, valid, invalid, truncated);
    }

    /** Parse only the location field from the bounded JSON prefix; no persistence. */
    private void inspectSnidLocationShape(String sample) {
        try (com.fasterxml.jackson.core.JsonParser parser =
                     new com.fasterxml.jackson.databind.ObjectMapper().getFactory().createParser(sample)) {
            if (parser.nextToken() != com.fasterxml.jackson.core.JsonToken.START_OBJECT) return;
            while (parser.nextToken() == com.fasterxml.jackson.core.JsonToken.FIELD_NAME) {
                String field = parser.currentName();
                com.fasterxml.jackson.core.JsonToken token = parser.nextToken();
                if ("location".equals(field) && token == com.fasterxml.jackson.core.JsonToken.VALUE_STRING) {
                    com.fasterxml.jackson.databind.JsonNode geometry =
                            new com.fasterxml.jackson.databind.ObjectMapper().readTree(parser.getText());
                    com.fasterxml.jackson.databind.JsonNode coordinates = geometry.path("coordinates");
                    boolean portugalPoint = coordinates.isArray() && coordinates.size() == 2
                            && coordinates.get(0).isNumber() && coordinates.get(1).isNumber()
                            && ((coordinates.get(1).asDouble() >= 36.9 && coordinates.get(1).asDouble() <= 42.2
                                    && coordinates.get(0).asDouble() >= -9.6 && coordinates.get(0).asDouble() <= -6.1)
                                || (coordinates.get(1).asDouble() >= 32.0 && coordinates.get(1).asDouble() <= 33.3
                                    && coordinates.get(0).asDouble() >= -17.5 && coordinates.get(0).asDouble() <= -15.5)
                                || (coordinates.get(1).asDouble() >= 36.8 && coordinates.get(1).asDouble() <= 40.2
                                    && coordinates.get(0).asDouble() >= -31.5 && coordinates.get(0).asDouble() <= -24.5));
                    log.info("SNID location point within Portugal bounds (GeoJSON lon/lat order): {}", portugalPoint);
                    log.info("SNID location geometry: type={}, coordinatesArray={}, coordinateCount={}",
                            geometry.path("type").asText("missing"), coordinates.isArray(),
                            coordinates.isArray() ? coordinates.size() : 0);
                    return;
                }
                parser.skipChildren();
            }
        } catch (com.fasterxml.jackson.core.JsonProcessingException ex) {
            log.info("SNID location not fully available in 8KiB prefix: {}",
                    ex.getOriginalMessage());
        } catch (java.io.IOException ex) {
            log.warn("SNID location prefix inspection unavailable: {}", ex.toString());
        }
    }

    /** Classify JSON field shape without exposing its value. */
    private String jsonFieldShape(String sample, String key) {
        int start = sample.indexOf("\"" + key + "\"");
        if (start < 0) return "not-in-prefix";
        int colon = sample.indexOf(':', start + key.length() + 2);
        if (colon < 0) return "truncated";
        int i = colon + 1;
        while (i < sample.length() && Character.isWhitespace(sample.charAt(i))) i++;
        if (i == sample.length()) return "truncated";
        return switch (sample.charAt(i)) {
            case '{' -> "object";
            case '[' -> "array";
            case '"' -> "string";
            case 'n' -> "null";
            default -> "scalar";
        };
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
