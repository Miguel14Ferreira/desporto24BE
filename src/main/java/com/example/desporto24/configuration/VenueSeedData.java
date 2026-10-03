package com.example.desporto24.configuration;

import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.repository.venue.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class VenueSeedData implements ApplicationRunner {

    private static final String CNG_SOURCE_REFERENCE = "OFFICIAL:CNG:PAREDE";

    private final VenueRepository venueRepository;

    @Override
    public void run(ApplicationArguments args) {
        boolean alreadySeeded = venueRepository.findAllByOrderByNameAsc().stream()
                .anyMatch(venue -> venue.getInformationSource() == Venue.InformationSource.OFFICIAL
                        && CNG_SOURCE_REFERENCE.equals(venue.getSourceReference()));

        if (alreadySeeded) {
            return;
        }

        venueRepository.save(Venue.builder()
                .name("Clube Nacional de Ginástica - Parque Desportivo")
                .city("Parede")
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
}
