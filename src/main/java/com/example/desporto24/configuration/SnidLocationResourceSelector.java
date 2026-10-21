package com.example.desporto24.configuration;

import java.net.URI;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Conservative, read-only selector for the official SNID location JSON.
 * Never falls back to demos, arbitrary shards, third-party mirrors or user URLs.
 * Missing file-size metadata is not evidence that a resource is empty.
 */
final class SnidLocationResourceSelector {

    private SnidLocationResourceSelector() {}

    static boolean matchesDatasetTitle(String title) {
        String normalized = normalize(title);
        return normalized.contains("sistema-nacional-de-informacao-desportiva");
    }

    static String findLocationJsonUrl(List<?> resources) {
        if (resources == null) return null;
        for (Object entry : resources) {
            if (!(entry instanceof Map<?, ?> resource)) continue;
            String title = normalize(value(resource.get("title")));
            if (!title.startsWith("snid-localizacao")) continue;
            String format = normalize(value(resource.get("format")));
            String mime = value(resource.get("mime"));
            if (mime == null) mime = value(resource.get("mime_type"));
            String url = value(resource.get("url"));
            if (url == null) continue;
            // The source can omit a filesize, but an explicitly zero or
            // negative size is not an acceptable venue-data resource.
            if (resource.get("filesize") instanceof Number size && size.longValue() <= 0) continue;
            if (!isJsonFormat(format, mime, url) || !isOfficialHttpsUrl(url)) continue;
            return url;
        }
        return null;
    }

    private static boolean isJsonFormat(String format, String mime, String url) {
        if ("json".equals(format)) return true;
        if (mime != null && "application/json".equalsIgnoreCase(mime.strip())) return true;
        try {
            String path = URI.create(url).getPath();
            return path != null && path.toLowerCase(Locale.ROOT).endsWith(".json");
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    static boolean isOfficialHttpsUrl(String url) {
        try {
            URI uri = URI.create(url);
            return "https".equalsIgnoreCase(uri.getScheme())
                    && "dados.gov.pt".equalsIgnoreCase(uri.getHost())
                    && (uri.getPort() == -1 || uri.getPort() == 443)
                    && uri.getRawUserInfo() == null
                    && uri.getRawFragment() == null;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private static String value(Object value) {
        if (!(value instanceof String text)) return null;
        String trimmed = text.strip();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static String normalize(String value) {
        if (value == null) return "";
        String lower = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "").toLowerCase(Locale.ROOT);
        return lower.replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
    }
}
