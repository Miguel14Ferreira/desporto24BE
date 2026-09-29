package com.example.desporto24.repository.venue;

import com.example.desporto24.model.venue.VenuePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VenuePhotoRepository extends JpaRepository<VenuePhoto, Long> {

    List<VenuePhoto> findByVenueIdOrderByCreatedAtAsc(Long venueId);
}
