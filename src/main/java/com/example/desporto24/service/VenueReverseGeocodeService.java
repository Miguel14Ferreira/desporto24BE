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
    private static final long CACHE_MS = 15 * 60_000L;
    private final HttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ConcurrentHashMap<String, Cached> cache = new ConcurrentHashMap<>();
    private final AtomicLong nextAllowedMs = new AtomicLong(0);

    public record ReverseLocation(String city, String address, Integer distanceMeters, String provider) {}
    private record Cached(ReverseLocation result, long expiresAt) {}

    public VenueReverseGeocodeService() {
        this(HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build());
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
        if (nextAllowedMs.getAndUpdate(prev -> Math.max(prev, now) + 1000) > now) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Volta a tentar dentro de alguns segundos");
        }
        String query = "?lat=" + URLEncoder.encode(Double.toString(latitude), StandardCharsets.UTF_8)
                + "&lon=" + URLEncoder.encode(Double.toString(longitude), StandardCharsets.UTF_8)
                + "&level=address&radius=1500&lang=pt";
        HttpRequest request = HttpRequest.newBuilder(URI.create(ENDPOINT + query))
                .timeout(Duration.ofSeconds(5))
                .header("Accept", "application/json")
                .header("User-Agent", "Jynlo/1.0 (https://d24sports.com)")
                .GET().build();
        try {
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
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Pesquisa de morada indisponível");
        }
    }

    ReverseLocation parse(String json, Double accuracyMeters) throws java.io.IOException {
        JsonNode result = mapper.readTree(json).path("result");
        if (!result.isObject()) return new ReverseLocation(null, null, null, "My Geocode");
        JsonNode fields = result.path("components");
        String city = first(fields, "city", "town", "village", "municipality", "suburb", "county");
        String road = first(fields, "road");
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
