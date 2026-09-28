package com.example.desporto24.repository.venue;

import com.example.desporto24.model.venue.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findAllByOrderByNameAsc();
}
