package com.example.desporto24.service;

import com.example.desporto24.enumeration.RegistrationOpeningMode;
import com.example.desporto24.enumeration.SessionRecurrence;
import com.example.desporto24.model.dto.session.CreateSessionRequest;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Plans the complete set of matches before the first database write.
 * Existing one-game-per-week callers retain their previous request shape.
 */
public final class CommunitySessionSchedule {
    public static final int MAX_WEEKLY_SLOTS = 7;
    public static final int MAX_SESSIONS_PER_CREATION = 52;

    private CommunitySessionSchedule() { }

    public record Occurrence(Instant kickoff, Instant registrationOpensAt) { }

    public static List<Occurrence> expand(CreateSessionRequest request, Instant now) {
        SessionRecurrence recurrence = request.recurrence() == null
                ? SessionRecurrence.NONE : request.recurrence();
        List<Instant> extra = request.additionalWeeklyGameDates() == null
                ? List.of() : request.additionalWeeklyGameDates();

        if (recurrence != SessionRecurrence.WEEKLY && !extra.isEmpty()) {
            throw new IllegalArgumentException("Os horários adicionais exigem repetição semanal");
        }
        if (extra.size() >= MAX_WEEKLY_SLOTS || extra.stream().anyMatch(date -> date == null)) {
            throw new IllegalArgumentException("Escolhe entre 1 e 7 sessões por semana");
        }

        int repetitions = recurrence == SessionRecurrence.NONE ? 1
                : request.recurrenceOccurrences() == null ? 0 : request.recurrenceOccurrences();
        if (repetitions < 1 || repetitions > MAX_SESSIONS_PER_CREATION
                || (recurrence != SessionRecurrence.NONE && repetitions < 2)
                || (long) repetitions * (extra.size() + 1) > MAX_SESSIONS_PER_CREATION) {
            throw new IllegalArgumentException("O plano pode ter no máximo 52 sessões");
        }

        final ZoneId zone;
        try {
            zone = request.recurrenceTimeZone() == null || request.recurrenceTimeZone().isBlank()
                    ? ZoneId.of("UTC") : ZoneId.of(request.recurrenceTimeZone());
        } catch (DateTimeException invalid) {
            throw new IllegalArgumentException("Fuso horário inválido", invalid);
        }

        ZonedDateTime first = request.gameDate().atZone(zone);
        List<ZonedDateTime> week = new ArrayList<>();
        week.add(first);
        ZonedDateTime nextCycle = first.plusWeeks(1);
        for (Instant kickoff : extra) {
            ZonedDateTime slot = kickoff.atZone(zone);
            if (!slot.isAfter(first) || !slot.isBefore(nextCycle)) {
                throw new IllegalArgumentException(
                        "Os horários adicionais devem ser posteriores ao primeiro jogo, nos 7 dias seguintes");
            }
            week.add(slot);
        }
        week.sort(ZonedDateTime::compareTo);

        Set<Instant> unique = new HashSet<>();
        for (ZonedDateTime slot : week) {
            if (!unique.add(slot.toInstant())) {
                throw new IllegalArgumentException("Não podes repetir o mesmo dia e hora");
            }
        }

        RegistrationOpeningMode openingMode = request.registrationOpeningMode() == null
                ? RegistrationOpeningMode.IMMEDIATE : request.registrationOpeningMode();
        Duration wallClockLead = null;
        if (openingMode == RegistrationOpeningMode.SCHEDULED) {
            if (request.registrationOpensAt() == null
                    || !request.registrationOpensAt().isAfter(now)
                    || !request.registrationOpensAt().isBefore(request.gameDate())) {
                throw new IllegalArgumentException("A convocatória deve abrir antes do jogo e numa data futura");
            }
            wallClockLead = Duration.between(
                    request.registrationOpensAt().atZone(zone).toLocalDateTime(),
                    first.toLocalDateTime());
        }

        List<Occurrence> result = new ArrayList<>(repetitions * week.size());
        int intervalWeeks = recurrence == SessionRecurrence.BIWEEKLY ? 2 : 1;
        for (int repeat = 0; repeat < repetitions; repeat++) {
            for (ZonedDateTime slot : week) {
                ZonedDateTime future = slot.plusWeeks((long) repeat * intervalWeeks);
                Instant kickoff = future.toInstant();
                if (!kickoff.isAfter(now)) {
                    throw new IllegalArgumentException("Todas as sessões devem ser futuras");
                }
                Instant opensAt = null;
                if (wallClockLead != null) {
                    opensAt = future.toLocalDateTime().minus(wallClockLead).atZone(zone).toInstant();
                    if (!opensAt.isAfter(now) || !opensAt.isBefore(kickoff)) {
                        throw new IllegalArgumentException(
                                "Cada convocatória deve abrir no futuro e antes da sessão");
                    }
                }
                result.add(new Occurrence(kickoff, opensAt));
            }
        }
        return List.copyOf(result);
    }
}
