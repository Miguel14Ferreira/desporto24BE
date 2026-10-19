package com.example.desporto24.service;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.Locale;
import java.text.Normalizer;

/** Canonical mainland Portugal district names; empty means unrestricted legacy search. */
public final class ContinentalDistricts {
    private ContinentalDistricts() {}
    public static final Set<String> NAMES = Set.of(
            "Aveiro", "Beja", "Braga", "Bragança", "Castelo Branco", "Coimbra",
            "Évora", "Faro", "Guarda", "Leiria", "Lisboa", "Portalegre",
            "Porto", "Santarém", "Setúbal", "Viana do Castelo", "Vila Real", "Viseu");
    private static final Map<String,String> CANONICAL = NAMES.stream().collect(Collectors.toUnmodifiableMap(
            ContinentalDistricts::fold, value -> value));

    private static String fold(String input) {
        return Normalizer.normalize(input.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "").toLowerCase(Locale.ROOT);
    }

    public static String normalizeOptional(String district) {
        if (district == null || district.isBlank()) return null;
        String canonical = CANONICAL.get(fold(district));
        if (canonical == null) throw new IllegalArgumentException("Distrito de Portugal continental inválido");
        return canonical;
    }

    public static String normalizeFilter(String district) {
        String canonical = normalizeOptional(district);
        return canonical == null ? "" : canonical.toLowerCase(Locale.ROOT);
    }
}
