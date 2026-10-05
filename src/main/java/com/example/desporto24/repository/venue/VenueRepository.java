package com.example.desporto24.repository.venue;

import com.example.desporto24.model.venue.Venue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findAllByOrderByNameAsc();

    Optional<Venue> findByInformationSourceAndSourceReference(
            Venue.InformationSource informationSource,
            String sourceReference);

    @Query("""
            SELECT v
            FROM Venue v
            WHERE (
                :city = ''
                OR LOWER(v.city) LIKE CONCAT(:city, '%')
            )
              AND (
                :sport = ''
                OR EXISTS (
                    SELECT sc
                    FROM Venue v2
                    JOIN v2.sportCodes sc
                    WHERE v2 = v
                      AND LOWER(sc) = :sport
                )
              )
            ORDER BY LOWER(v.name), v.id
            """)
    Page<Venue> search(
            @Param("city") String city,
            @Param("sport") String sport,
            Pageable pageable);
}
