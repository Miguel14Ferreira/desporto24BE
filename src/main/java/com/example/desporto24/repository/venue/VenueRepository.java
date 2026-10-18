package com.example.desporto24.repository.venue;

import com.example.desporto24.model.venue.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findAllByOrderByNameAsc();
    List<Venue> findByCityContainingIgnoreCaseOrderByNameAsc(String city);

    /**
     * PostgreSQL-backed search: the filter is applied BEFORE OFFSET/LIMIT,
     * not to a downloaded catalog. A sport must equal a complete token in
     * the semicolon/comma-separated catalog, never a substring of another sport.
     * Stable name/city/id ordering prevents missing equal-name results.
     */
    @Query(value = """
            SELECT v.*
            FROM venue v
            WHERE (:name = '' OR strpos(lower(v.name), :name) > 0
                   OR strpos(lower(v.address), :name) > 0)
              AND (:city = '' OR strpos(lower(v.city), :city) > 0)
              AND (:sport = '' OR EXISTS (
                   SELECT 1
                   FROM regexp_split_to_table(coalesce(v.sports, ''), '[;,]') AS token
                   WHERE lower(btrim(token)) = :sport
              ))
            ORDER BY lower(v.name), lower(v.city), v.id
            """, nativeQuery = true)
    List<Venue> findSuggestions(
            @Param("name") String name,
            @Param("city") String city,
            @Param("sport") String sport,
            Pageable pageable);

    Optional<Venue> findByInformationSourceAndSourceReference(
            Venue.InformationSource informationSource,
            String sourceReference);
}
