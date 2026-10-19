package com.example.desporto24.service;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

/** Known Cascais parish aliases from the October 2026 venue catalogue review.
 * Never overwrite the historic parish/city submitted by a venue creator.
 * Expand to other municipalities only after the equivalent CAOP review.
 */
public final class VenueMunicipalityAliases {
    private VenueMunicipalityAliases() {}

    private static final List<String> CASCAIS = List.of(
            "cascais", "cascais e estoril", "u.f. cascais e estoril",
            "uniao das freguesias de cascais e estoril",
            "alcabideche", "sao domingos de rana", "s. domingos de rana",
            "u.f. carcavelos e parede", "carcavelos e parede",
            "uniao das freguesias de carcavelos e parede",
            "carcavelos", "parede", "estoril", "aboboda");

    public static boolean isCascais(String city) {
        if (city == null) return false;
        return "cascais".equals(Normalizer.normalize(city.trim().toLowerCase(Locale.ROOT),
                Normalizer.Form.NFD).replaceAll("\\p{M}+", ""));
    }

    public static List<String> cascaisParishes() {
        return CASCAIS;
    }
}
