package com.example.desporto24.service;

import com.example.desporto24.model.dto.venue.ResolveVenueMapLinkResponse;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VenueMapLinkService {

    private static final int MAX_REDIRECTS = 5;
    private static final Pattern AT_COORDINATES = Pattern.compile(
            "@(-?\\d{1,2}(?:\\.\\d+)?),(-?\\d{1,3}(?:\\.\\d+)?)");
    private static final Pattern DATA_COORDINATES = Pattern.compile(
            "!3d(-?\\d{1,2}(?:\\.\\d+)?)!4d(-?\\d{1,3}(?:\\.\\d+)?)");
    private static final Pattern QUERY_COORDINATES = Pattern.compile(
            "(?:[?&](?:q|query)=)(-?\\d{1,2}(?:\\.\\d+)?),(-?\\d{1,3}(?:\\.\\d+)?)");

    private final HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NEVER)
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public ResolveVenueMapLinkResponse resolve(String rawUrl) {
        URI submitted = parseAndValidate(rawUrl);
        // Coordinates embedded in a genuine Google Maps URL are sufficient;
        // do not require an unnecessary outgoing network request to Google.
        double[] directCoordinates = coordinates(submitted.toString());
        if (directCoordinates != null) {
            return new ResolveVenueMapLinkResponse(
                    submitted.toString(), directCoordinates[0], directCoordinates[1]);
        }

        URI resolved = resolveRedirects(submitted);
        double[] coordinates = coordinates(resolved.toString());

        if (coordinates == null) {
            throw new IllegalArgumentException("Não foi possível identificar as coordenadas deste link do Maps");
        }

        return new ResolveVenueMapLinkResponse(
                resolved.toString(),
                coordinates[0],
                coordinates[1]);
    }

    private URI resolveRedirects(URI start) {
        URI current = start;

        for (int redirect = 0; redirect <= MAX_REDIRECTS; redirect++) {
            validateAllowedHost(current);

            HttpRequest request = HttpRequest.newBuilder(current)
                    .timeout(Duration.ofSeconds(8))
                    .header("User-Agent", "Desporto24/1.0")
                    .GET()
                    .build();

            try {
                HttpResponse<Void> response = httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.discarding());

                int status = response.statusCode();
                if (status < 300 || status >= 400) {
                    return current;
                }

                String location = response.headers().firstValue("location")
                        .orElseThrow(() -> new IllegalArgumentException("Redirecionamento Maps inválido"));

                current = current.resolve(location);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new IllegalArgumentException("Não foi possível resolver o link do Maps", ex);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Não foi possível resolver o link do Maps", ex);
            }
        }

        throw new IllegalArgumentException("O link do Maps tem demasiados redirecionamentos");
    }

    private URI parseAndValidate(String rawUrl) {
        try {
            URI uri = URI.create(rawUrl.trim());
            if (!"https".equalsIgnoreCase(uri.getScheme())) {
                throw new IllegalArgumentException("O link do Maps tem de usar HTTPS");
            }
            validateAllowedHost(uri);
            return uri;
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Link do Maps inválido", ex);
        }
    }

    private void validateAllowedHost(URI uri) {
        String host = uri.getHost();
        if (host == null) {
            throw new IllegalArgumentException("Link do Maps inválido");
        }

        String normalized = host.toLowerCase();
        boolean allowed = normalized.equals("maps.app.goo.gl")
                || normalized.equals("goo.gl")
                || normalized.equals("google.com")
                || normalized.endsWith(".google.com");

        if (!allowed) {
            throw new IllegalArgumentException("Só são aceites links do Google Maps");
        }
    }

    private double[] coordinates(String url) {
        String decoded = URLDecoder.decode(url, StandardCharsets.UTF_8);

        Matcher matcher = firstMatch(decoded);
        if (matcher == null) {
            return null;
        }

        double latitude = Double.parseDouble(matcher.group(1));
        double longitude = Double.parseDouble(matcher.group(2));

        if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
            return null;
        }

        return new double[]{latitude, longitude};
    }

    private Matcher firstMatch(String decoded) {
        // !3d/!4d and explicit search query identify the place. Google Maps
        // @lat,lon commonly describes only the map *viewport*, not the venue.
        for (Pattern pattern : new Pattern[]{DATA_COORDINATES, QUERY_COORDINATES, AT_COORDINATES}) {
            Matcher matcher = pattern.matcher(decoded);
            if (matcher.find()) {
                return matcher;
            }
        }
        return null;
    }
}
