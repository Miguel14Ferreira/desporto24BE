package com.example.desporto24.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Reverse-geocodes a location explicitly shared by the user while creating a session.
 * My Geocode supports bounded, keyless HTTPS reverse lookups (not the public OSMF
 * Nominatim endpoint). Do not transmit device location until the user taps GPS.
 * The returned street is always approximate and editable, never a substitute
 * for the original browser GPS coordinates.
 */
@Service
public class VenueReverseGeocodeService {
    private static final String ENDPOINT = "https://api.mygeocode.com/v1/reverse";
    // Public Photon demo is for modest interactive use only; keep this endpoint
    // server-side, cached, throttled and easy to replace with a managed provider.
    private static final String STREET_ENDPOINT = "https://photon.komoot.io/reverse";
    private static final long CACHE_MS = 15 * 60_000L;
    private final HttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ConcurrentHashMap<String, Cached> cache = new ConcurrentHashMap<>();
    private final AtomicLong nextAllowedMs = new AtomicLong(0);

    public record ReverseLocation(String city, String address, Integer distanceMeters, String provider) {}
    private record Cached(ReverseLocation result, long expiresAt) {}

    public VenueReverseGeocodeService() {
        this(HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(8)).build());
    }
    VenueReverseGeocodeService(HttpClient client) {
        this.client = client;
    }

    public ReverseLocation reverse(double latitude, double longitude, Double accuracyMeters) {
        if (!Double.isFinite(latitude) || Math.abs(latitude) > 90
                || !Double.isFinite(longitude) || Math.abs(longitude) > 180
                || accuracyMeters != null && (!Double.isFinite(accuracyMeters)
                    || accuracyMeters < 0 || accuracyMeters > 100_000)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Coordenadas GPS inválidas");
        }
        // Round only the provider lookup/cached key (~11 m), not the stored session GPS.
        String locationKey = String.format(Locale.ROOT, "%.4f:%.4f:%.0f",
                latitude, longitude, accuracyMeters == null ? 100 : accuracyMeters);
        Cached cached = cache.get(locationKey);
        long now = System.currentTimeMillis();
        if (cached != null && cached.expiresAt > now) return cached.result;
        // A small per-process cap avoids turning this interactive UI into an
        // unlimited third-party proxy. Client receives a nonblocking warning.
        long previous = nextAllowedMs.get();
        if (previous > now || !nextAllowedMs.compareAndSet(previous, now + 1000)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Volta a tentar dentro de alguns segundos");
        }
        String query = "?lat=" + URLEncoder.encode(Double.toString(latitude), StandardCharsets.UTF_8)
                + "&lon=" + URLEncoder.encode(Double.toString(longitude), StandardCharsets.UTF_8)
                + "&level=address&radius=1500&lang=pt";
        HttpRequest request = HttpRequest.newBuilder(URI.create(ENDPOINT + query))
                .timeout(Duration.ofSeconds(14))
                .header("Accept", "application/json")
                .header("User-Agent", "Jynlo/1.0 (https://d24sports.com)")
                .GET().build();
        try {
            // My Geocode often returns only municipality-level "Sintra" for
            // urban Cacém. Photon/OSM street-layer data resolves the nearby
            // Rua do Olival and the more useful locality "Cacém".
            try {
                String streetQuery = "?lat=" + latitude + "&lon=" + longitude
                        + "&radius=0.5&layer=street&limit=1";
                HttpRequest streetRequest = HttpRequest.newBuilder(
                        URI.create(STREET_ENDPOINT + streetQuery))
                        .timeout(Duration.ofSeconds(7))
                        .header("Accept", "application/json")
                        .header("User-Agent", "Jynlo/1.0 (https://d24sports.com)")
                        .GET().build();
                HttpResponse<String> streetResponse =
                        client.send(streetRequest, HttpResponse.BodyHandlers.ofString());
                if (streetResponse.statusCode() == 200) {
                    ReverseLocation street = parsePhoton(streetResponse.body(), latitude, longitude);
                    if (street.address() != null && street.city() != null) {
                        if (cache.size() > 400) cache.clear();
                        cache.put(locationKey, new Cached(street, now + CACHE_MS));
                        return street;
                    }
                }
            } catch (java.io.IOException ignored) {
                // This optional street lookup must not break the old provider.
            }
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "Pesquisa de morada temporariamente indisponível");
            }
            ReverseLocation location = parse(response.body(), accuracyMeters);
            if (cache.size() > 400) cache.clear();
            cache.put(locationKey, new Cached(location, now + CACHE_MS));
            return location;
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Pesquisa de morada interrompida");
        } catch (java.io.IOException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Serviço de localização temporariamente inacessível", ex);
        }
    }

    /**
     * Read one street feature returned by Photon (OpenStreetMap-based).
     * Do not turn the street's midpoint into a new session GPS coordinate.
     */
    ReverseLocation parsePhoton(String json, double latitude, double longitude) throws java.io.IOException {
        JsonNode feature = mapper.readTree(json).path("features").path(0);
        JsonNode details = feature.path("properties");
        if (!details.isObject()) return new ReverseLocation(null, null, null, "OpenStreetMap / Photon");
        String city = first(details, "district", "locality", "city", "county");
        String road = first(details, "street");
        if (road == null && "street".equals(details.path("type").asText())) {
            road = first(details, "name");
        }
        JsonNode coordinates = feature.path("geometry").path("coordinates");
        if (coordinates.size() < 2 || !coordinates.path(0).isNumber()
                || !coordinates.path(1).isNumber()) {
            return new ReverseLocation(city, null, null, "OpenStreetMap / Photon");
        }
        double roadLongitude = coordinates.path(0).asDouble();
        double roadLatitude = coordinates.path(1).asDouble();
        double dLat = Math.toRadians(roadLatitude - latitude);
        double dLon = Math.toRadians(roadLongitude - longitude);
        double arc = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(roadLatitude))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        int metres = (int) Math.round(2 * 6_371_000 * Math.asin(Math.min(1, Math.sqrt(arc))));
        String address = road == null || metres > 500 ? null
                : metres > 160 ? "Perto de " + road : road;
        return new ReverseLocation(city, address, metres, "OpenStreetMap / Photon");
    }

    ReverseLocation parse(String json, Double accuracyMeters) throws java.io.IOException {
        JsonNode result = mapper.readTree(json).path("result");
        if (!result.isObject()) return new ReverseLocation(null, null, null, "My Geocode");
        JsonNode fields = result.path("components");
        String city = first(fields, "city", "town", "village", "locality", "municipality",
                "city_district", "suburb", "quarter", "county");
        String road = first(fields, "road", "street", "pedestrian", "residential");
        String houseNumber = first(fields, "house_number");
        JsonNode distanceNode = result.path("distance_m");
        int distance = distanceNode.isNumber() ? Math.max(0, distanceNode.asInt()) : Integer.MAX_VALUE;
        // No guesswork: don't label a potentially different street as the field's address.
        int streetRadius = accuracyMeters == null ? 140
                : (int)Math.max(90, Math.min(220, accuracyMeters + 90));
        String address = null;
        if (road != null && distance <= streetRadius) {
            address = houseNumber != null && distance <= 35
                    && accuracyMeters != null && accuracyMeters <= 25
                    ? road + ", " + houseNumber : road;
        }
        return new ReverseLocation(city, address,
                distance == Integer.MAX_VALUE ? null : distance, "My Geocode");
    }

    private static String first(JsonNode node, String... keys) {
        for (String key : keys) {
            JsonNode value = node.path(key);
            if (!value.isTextual()) continue;
            String text = value.asText().strip().replaceAll("[\\p{Cntrl}]+", " ");
            if (!text.isEmpty() && text.length() <= 120) return text;
        }
        return null;
    }
}
