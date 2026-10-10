package com.example.desporto24.repository.venue;

import com.example.desporto24.model.venue.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findAllByOrderByNameAsc();

    /** Only the fields needed for import deduplication, not complete Venue entities. */
    interface VenueDeduplicationView {
        String getName();
        Double getLatitude();
        Double getLongitude();
        String getSourceReference();
    }

    List<VenueDeduplicationView> findAllProjectedBy();

    List<Venue> findByCityContainingIgnoreCaseOrderByNameAsc(String city);

    Optional<Venue> findByInformationSourceAndSourceReference(
            Venue.InformationSource informationSource,
            String sourceReference);
}
