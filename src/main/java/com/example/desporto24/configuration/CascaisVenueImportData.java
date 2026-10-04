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
            int skipped = 0;

            for (Object featureValue : features) {
                if (!(featureValue instanceof Map<?, ?> feature)) {
                    skipped++;
                    continue;
                }

                Map<?, ?> properties = asMap(feature.get("properties"));
                Map<?, ?> geometry = asMap(feature.get("geometry"));

                String fid = text(properties, "FID");
                String name = text(properties, "nome");

                double[] coordinates = pointCoordinates(geometry);
                if (fid == null || name == null || coordinates == null) {
                    skipped++;
                    continue;
                }

                String sourceReference = SOURCE_PREFIX + fid;
                if (venueRepository.findByInformationSourceAndSourceReference(
                        Venue.InformationSource.OFFICIAL,
                        sourceReference).isPresent()) {
                    skipped++;
                    continue;
                }

                String parish = fallback(text(properties, "freguesia"), "Cascais");
                String address = buildAddress(properties, parish);

                venueRepository.save(Venue.builder()
                        .name(limit(name, 140))
                        .city(limit(parish, 120))
                        .district("Lisboa")
                        .address(limit(address, 255))
                        .venueType("Equipamento desportivo")
                        .website(limit(text(properties, "endereco_internet"), 255))
                        .contact(limit(buildContact(properties), 255))
                        .latitude(coordinates[1])
                        .longitude(coordinates[0])
                        .informationSource(Venue.InformationSource.OFFICIAL)
                        .sourceReference(limit(sourceReference, 255))
                        .createdAt(Instant.now())
                        .build());

                inserted++;
            }

            log.info("Cascais venue import completed: inserted={}, skipped={}, total={}",
                    inserted, skipped, features.size());
        } catch (Exception ex) {
            log.warn("Cascais venue import failed without blocking startup: {}", ex.getMessage());
        }
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

    private double[] pointCoordinates(Map<?, ?> geometry) {
        if (geometry == null || !"Point".equalsIgnoreCase(string(geometry.get("type")))) {
            return null;
        }

        if (!(geometry.get("coordinates") instanceof List<?> coordinates)
                || coordinates.size() < 2
                || !(coordinates.get(0) instanceof Number longitude)
                || !(coordinates.get(1) instanceof Number latitude)) {
            return null;
        }

        double lon = longitude.doubleValue();
        double lat = latitude.doubleValue();

        if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
            return null;
        }

        return new double[]{lon, lat};
    }

    private String buildAddress(Map<?, ?> properties, String parish) {
        String street = text(properties, "rua");
        String policeNumber = text(properties, "numero_policia");
        String postalCode = text(properties, "codigo_postal");

        StringBuilder address = new StringBuilder();

        append(address, street);
        append(address, policeNumber);
        append(address, postalCode);
        append(address, parish);

        return address.length() == 0 ? "Cascais" : address.toString();
    }

    private String buildContact(Map<?, ?> properties) {
        String phone = text(properties, "telefone");
        String email = text(properties, "endereco_correio_eletronico");

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

    private String text(Map<?, ?> map, String key) {
        return map == null ? null : clean(string(map.get(key)));
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
