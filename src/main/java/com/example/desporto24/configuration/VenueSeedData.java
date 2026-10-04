package com.example.desporto24.configuration;

import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.repository.venue.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class VenueSeedData implements ApplicationRunner {

    private static final String CNG_SOURCE_REFERENCE = "OFFICIAL:CNG:PAREDE";
    private static final String ABOBODA_SOURCE_REFERENCE = "OFFICIAL:CASCAIS:CDM-ABOBODA";

    private final VenueRepository venueRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) {
        jdbcTemplate.execute("ALTER TABLE venue ALTER COLUMN created_by_profile_id DROP NOT NULL");
        jdbcTemplate.execute("ALTER TABLE venue_photo ALTER COLUMN uploaded_by_profile_id DROP NOT NULL");

        seedCng();
        seedAboboda();
    }

    private void seedCng() {
        if (venueRepository.findByInformationSourceAndSourceReference(
                Venue.InformationSource.OFFICIAL,
                CNG_SOURCE_REFERENCE).isPresent()) {
            return;
        }

        venueRepository.save(Venue.builder()
                .name("Clube Nacional de Ginástica - Parque Desportivo")
                .city("Parede")
                .district("Lisboa")
                .address("Rua Machado dos Santos, 112, 2775-236 Parede")
                .sports("Futebol;Futsal;Ginásio;Ginástica;Ginástica Rítmica;Judo;Karaté;Natação;Pilates;Taekwondo;Ténis;Voleibol;Yoga")
                .venueType("Complexo desportivo")
                .ownershipType(Venue.OwnershipType.PRIVATE)
                .environmentType(Venue.EnvironmentType.BOTH)
                .indoor(true)
                .changingRooms(true)
                .parking(false)
                .lighting(true)
                .latitude(38.6895611)
                .longitude(-9.3474667)
                .website("https://cng.pt/")
                .contact("214 571 010")
                .openingHours("Segunda a sábado 08:00-23:00; domingo e feriados 08:00-21:00")
                .informationSource(Venue.InformationSource.OFFICIAL)
                .sourceReference(CNG_SOURCE_REFERENCE)
                .createdAt(Instant.now())
                .build());
    }

    private void seedAboboda() {
        if (venueRepository.findByInformationSourceAndSourceReference(
                Venue.InformationSource.OFFICIAL,
                ABOBODA_SOURCE_REFERENCE).isPresent()) {
            return;
        }

        venueRepository.save(Venue.builder()
                .name("Complexo Desportivo Municipal da Abóboda")
                .city("São Domingos de Rana")
                .district("Lisboa")
                .address("Rua Mouzinho de Albuquerque, 186, 2785-032 São Domingos de Rana")
                .sports("Natação;Pólo Aquático;Hidroginástica;Ginásio;Pilates;Yoga;Taekwondo;Jiu-Jitsu;Boxe;Kickboxing")
                .venueType("Complexo desportivo")
                .ownershipType(Venue.OwnershipType.PUBLIC)
                .environmentType(Venue.EnvironmentType.INDOOR)
                .indoor(true)
                .changingRooms(true)
                .toilets(true)
                .showers(true)
                .stands(true)
                .cafe(true)
                .parking(true)
                .lighting(true)
                .latitude(38.7263)
                .longitude(-9.3307)
                .website("https://complexodesportivoaboboda.com/")
                .contact("+351 214 489 720")
                .openingHours("Segunda a sexta 07:00-22:00; sábado 08:00-20:00; domingo piscina 08:00-14:00")
                .informationSource(Venue.InformationSource.OFFICIAL)
                .sourceReference(ABOBODA_SOURCE_REFERENCE)
                .createdAt(Instant.now())
                .build());
    }
}
