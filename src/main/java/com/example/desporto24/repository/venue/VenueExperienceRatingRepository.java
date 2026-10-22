package com.example.desporto24.repository.venue;

import com.example.desporto24.model.venue.VenueExperienceRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VenueExperienceRatingRepository extends JpaRepository<VenueExperienceRating, Long> {
    Optional<VenueExperienceRating> findByVenueIdAndProfileId(Long venueId, Long profileId);

    long countByVenueId(Long venueId);

    @Query("select avg(r.score) from VenueExperienceRating r where r.venue.id = :venueId")
    Double averageScoreForVenue(@Param("venueId") Long venueId);
}
