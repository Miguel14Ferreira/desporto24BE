package com.example.desporto24.configuration;

import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.repository.venue.VenueRepository;
import com.example.desporto24.service.VenueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PilotNationalVenuesImportOnReady {

    private final VenueRepository venueRepository;
    private final VenueService venueService;

    @EventListener(ApplicationReadyEvent.class)
    public void importPilotVenues() {
        importIfAbsent("OSM:WAY:305711614", "Piscina Municipal da Constituição",
                "Porto", "Rua do Almirante Leote do Rêgo, 4200-260 Porto",
                "Natação", "Piscina", 41.16518, -8.61414);
        importIfAbsent("OSM:WAY:58257179", "Pavilhão Municipal Multidesportos Mário Mexia",
                "Coimbra", "Praça Heróis do Ultramar, 3030-327 Coimbra",
                "Andebol;Basquetebol;Voleibol;Futsal;Patinagem;Ginástica",
                "Pavilhão Desportivo", 40.20540, -8.40816);
    }

    private void importIfAbsent(String reference, String name, String city,
                                String address, String sports, String venueType,
                                double latitude, double longitude) {
        try {
            if (venueRepository.findByInformationSourceAndSourceReference(
                    Venue.InformationSource.OPENSTREETMAP, reference).isPresent()) {
                log.info("PILOT_VENUE_IMPORT reference={} status=ALREADY_IMPORTED", reference);
                return;
            }
            boolean candidateExists = venueRepository
                    .findByCityContainingIgnoreCaseOrderByNameAsc(city).stream()
                    .anyMatch(venue -> venue.getName().trim().equalsIgnoreCase(name)
                            || (venue.getLatitude() != null && venue.getLongitude() != null
                            && Math.abs(venue.getLatitude() - latitude) <= 0.0025
                            && Math.abs(venue.getLongitude() - longitude) <= 0.0025));
            if (candidateExists) {
                log.warn("PILOT_VENUE_IMPORT reference={} status=SKIPPED_POSSIBLE_DUPLICATE", reference);
                return;
            }
            var result = venueService.upsertOpenStreetMapVenue(reference, name, city, address,
                    sports, venueType, Venue.EnvironmentType.INDOOR, latitude, longitude);
            log.info("PILOT_VENUE_IMPORT reference={} status=IMPORTED id={}", reference, result.id());
        } catch (Exception ex) {
            log.error("PILOT_VENUE_IMPORT reference={} status=FAILED", reference, ex);
        }
    }
}
