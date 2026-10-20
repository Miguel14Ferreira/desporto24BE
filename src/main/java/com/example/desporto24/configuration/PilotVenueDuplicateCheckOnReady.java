package com.example.desporto24.configuration;

import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.repository.venue.VenueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class PilotVenueDuplicateCheckOnReady {

    private final VenueRepository venueRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void verifyImportedVenues() {
        verify(261L, "OSM:WAY:305711614", "Porto", "Natação");
        verify(262L, "OSM:WAY:58257179", "Coimbra",
                "Andebol;Basquetebol;Voleibol;Futsal;Patinagem;Ginástica");
    }

    private void verify(Long id, String reference, String city, String sports) {
        try {
            venueRepository.findById(id).ifPresentOrElse(venue -> {
                boolean valid = venue.getInformationSource() == Venue.InformationSource.OPENSTREETMAP
                        && reference.equals(venue.getSourceReference())
                        && city.equalsIgnoreCase(venue.getCity())
                        && sports.equals(venue.getSports());
                log.info("PILOT_VENUE_VERIFY id={} result={} name={} city={} sports={}",
                        id, valid ? "OK" : "MISMATCH",
                        venue.getName(), venue.getCity(), venue.getSports());
            }, () -> log.warn("PILOT_VENUE_VERIFY id={} result=NOT_FOUND", id));
        } catch (Exception ex) {
            log.warn("PILOT_VENUE_VERIFY id={} result=FAILED error={}", id, ex.toString());
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void checkPilotVenues() {
        check("OSM:WAY:305711614", "Piscina Municipal da Constituição", "Porto",
                41.16518, -8.61414);
        check("OSM:WAY:58257179", "Pavilhão Municipal Multidesportos Mário Mexia", "Coimbra",
                40.20540, -8.40816);
    }

    private void check(String reference, String name, String city, double latitude, double longitude) {
        try {
            boolean sourceExists = venueRepository.findByInformationSourceAndSourceReference(
                    Venue.InformationSource.OPENSTREETMAP, reference).isPresent();
            List<Venue> candidates = venueRepository
                    .findByCityContainingIgnoreCaseOrderByNameAsc(city).stream()
                    .filter(venue -> venue.getName().trim().toLowerCase(Locale.ROOT)
                            .equals(name.toLowerCase(Locale.ROOT))
                            || (venue.getLatitude() != null && venue.getLongitude() != null
                            && Math.abs(venue.getLatitude() - latitude) <= 0.0025
                            && Math.abs(venue.getLongitude() - longitude) <= 0.0025))
                    .limit(10)
                    .toList();
            log.info("PILOT_VENUE_CHECK reference={} status={} candidates={}",
                    reference, sourceExists ? "EXISTING_SOURCE"
                            : candidates.isEmpty() ? "NO_CANDIDATE" : "REVIEW_CANDIDATES",
                    candidates.stream().map(venue -> venue.getId() + ":" + venue.getName()).toList());
        } catch (Exception ex) {
            log.warn("PILOT_VENUE_CHECK reference={} failed: {}", reference, ex.toString());
        }
    }
}
