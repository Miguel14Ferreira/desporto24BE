package com.example.desporto24.repository.venue;

import com.example.desporto24.model.venue.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
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
            WHERE (:namePattern = '%' OR lower(v.name) LIKE :namePattern ESCAPE '~'
                   OR lower(v.address) LIKE :namePattern ESCAPE '~')
              AND (:cityPattern = '%' OR lower(v.city) LIKE :cityPattern ESCAPE '~')
              AND (:sport = '' OR EXISTS (
                   SELECT 1
                   FROM regexp_split_to_table(coalesce(v.sports, ''), '[;,]') AS token
                   WHERE lower(btrim(token)) = :sport
              ))
            ORDER BY lower(v.name), lower(v.city), v.id
            LIMIT 41 OFFSET :offset
            """, nativeQuery = true)
    List<Venue> findSuggestions(
            @Param("namePattern") String namePattern,
            @Param("cityPattern") String cityPattern,
            @Param("sport") String sport,
            @Param("offset") int offset);

    /**
     * Inventory existing records by the district as stored. An empty district
     * remains an explicit unknown bucket; never infer a district from city.
     */
    @Query(value = """
            SELECT coalesce(nullif(btrim(v.district), ''), '(sem distrito)') AS district,
                   count(*) AS total,
                   count(*) FILTER (WHERE v.latitude IS NULL OR v.longitude IS NULL) AS no_coordinates,
                   count(*) FILTER (WHERE v.sports IS NULL OR btrim(v.sports) = '') AS no_sports,
                   count(*) FILTER (WHERE v.information_source = 'OPENSTREETMAP') AS osm_total
            FROM venue v
            GROUP BY 1
            ORDER BY 1
            """, nativeQuery = true)
    List<Object[]> countCoverageByDistrict();

    Optional<Venue> findByInformationSourceAndSourceReference(
            Venue.InformationSource informationSource,
            String sourceReference);
}
