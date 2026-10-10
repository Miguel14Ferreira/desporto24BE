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
