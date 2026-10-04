package com.example.desporto24.repository.venue;

import com.example.desporto24.model.venue.VenueSportCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VenueSportConditionRepository extends JpaRepository<VenueSportCondition, Long> {

    List<VenueSportCondition> findByVenueIdOrderBySportAsc(Long venueId);

    Optional<VenueSportCondition> findByVenueIdAndSportIgnoreCase(Long venueId, String sport);
}
