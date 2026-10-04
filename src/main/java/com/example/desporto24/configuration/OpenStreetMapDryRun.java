package com.example.desporto24.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenStreetMapDryRun implements ApplicationRunner {

    private static final String OVERPASS_URL = "https://overpass-api.de/api/interpreter";

    private final ObjectMapper objectMapper;

    @Value("${d24.osm.dry-run-bbox:}")
    private String bbox;

    @Override
    public void run(ApplicationArguments args) {
        if (bbox == null || bbox.isBlank()) {
            return;
        }

        try {
            String query = """
                    [out:json][timeout:120];
                    (
                      nwr["leisure"~"sports_centre|pitch|stadium|track|swimming_pool"](%s);
                      nwr["building"="sports_hall"](%s);
                    );
                    out center tags;
                    """.formatted(bbox, bbox).trim();

            String body = "data=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OVERPASS_URL))
                    .header("User-Agent", "Desporto24-venue-import/1.0")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                log.warn("OSM dry run failed with HTTP {}", response.statusCode());
                return;
            }

            JsonNode elements = objectMapper.readTree(response.body()).path("elements");
            int rawCount = elements.isArray() ? elements.size() : 0;
            List<String> importableSamples = new ArrayList<>();
            int importableCount = 0;

            for (JsonNode element : elements) {
                JsonNode tags = element.path("tags");
                String name = text(tags, "name");
                String city = firstText(tags, "addr:city", "addr:town", "addr:village", "is_in:city");
                String street = text(tags, "addr:street");
                String number = text(tags, "addr:housenumber");
                String postcode = text(tags, "addr:postcode");

                String streetLine = String.join(" ",
                        List.of(street, number).stream().filter(value -> !value.isBlank()).toList());
                String suffix = String.join(" ",
                        List.of(postcode, city).stream().filter(value -> !value.isBlank()).toList());
                String address = String.join(", ",
                        List.of(streetLine, suffix).stream().filter(value -> !value.isBlank()).toList());

                if (name.isBlank() || city.isBlank() || address.isBlank()) {
                    continue;
                }

                importableCount++;
                if (importableSamples.size() < 5) {
                    importableSamples.add(name + " | " + city + " | " + address);
                }
            }

            log.info("OSM dry run for bbox {}: {} raw elements, {} importable venues",
                    bbox, rawCount, importableCount);
            importableSamples.forEach(sample -> log.info("OSM dry run sample: {}", sample));
        } catch (Exception ex) {
            log.warn("OSM dry run could not be completed: {}", ex.getMessage());
        }
    }

    private String text(JsonNode node, String field) {
        return node.path(field).asText("").trim();
    }

    private String firstText(JsonNode node, String... fields) {
        for (String field : fields) {
            String value = text(node, field);
            if (!value.isBlank()) {
                return value;
            }
        }
        return "";
    }
}
