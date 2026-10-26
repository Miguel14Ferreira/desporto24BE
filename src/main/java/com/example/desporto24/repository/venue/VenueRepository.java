package com.example.desporto24.repository.venue;

import com.example.desporto24.model.venue.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findAllByOrderByNameAsc();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT v FROM Venue v WHERE v.id = :id")
    Optional<Venue> findForEditReview(@Param("id") Long id);

    /**
     * Filter and paginate in the database; never fetch the national catalog in memory.
     * Full modality tokens prevent Futebol matching Futebol Americano.
     * PostgreSQL and H2 both support translate for accent-insensitive search.
     * Relevance is computed before the database applies the page boundary, so
     * matches cannot be stranded on later pages. All ties end with immutable id.
     */
    @Query("""
           SELECT v FROM Venue v
           WHERE (:name = '' OR
                  LOWER(FUNCTION('translate', v.name,
                    'áàâãäéèêëíìîïóòôõöúùûüç',
                    'aaaaaeeeeiiiiooooouuuuc')) LIKE CONCAT('%', :name, '%') ESCAPE '!')
             AND (:city = '' OR
                  LOWER(FUNCTION('translate', v.city,
                    'áàâãäéèêëíìîïóòôõöúùûüç',
                    'aaaaaeeeeiiiiooooouuuuc')) LIKE CONCAT('%', :city, '%') ESCAPE '!')
             AND (:district = '' OR v.district = :district)
             AND (:sport = '' OR
                  LOWER(FUNCTION('translate',
                    CONCAT(';', REPLACE(REPLACE(COALESCE(v.sports, ''), ' ', ''), ',', ';'), ';'),
                    'áàâãäéèêëíìîïóòôõöúùûüç',
                    'aaaaaeeeeiiiiooooouuuuc'))
                    LIKE CONCAT('%;', :sport, ';%') ESCAPE '!')
           ORDER BY
             CASE
               WHEN :name = '' THEN 0
               WHEN LOWER(FUNCTION('translate', v.name,
                    'áàâãäéèêëíìîïóòôõöúùûüç',
                    'aaaaaeeeeiiiiooooouuuuc')) = :name THEN 0
               WHEN LOWER(FUNCTION('translate', v.name,
                    'áàâãäéèêëíìîïóòôõöúùûüç',
                    'aaaaaeeeeiiiiooooouuuuc')) LIKE CONCAT(:name, '%') ESCAPE '!' THEN 1
               ELSE 2
             END,
             CASE
               WHEN :city = '' THEN 0
               WHEN LOWER(FUNCTION('translate', v.city,
                    'áàâãäéèêëíìîïóòôõöúùûüç',
                    'aaaaaeeeeiiiiooooouuuuc')) = :city THEN 0
               ELSE 1
             END,
             CASE WHEN v.sports IS NOT NULL AND TRIM(v.sports) <> '' THEN 0 ELSE 1 END,
             CASE WHEN (v.address IS NOT NULL AND TRIM(v.address) <> '')
                        AND v.latitude IS NOT NULL AND v.longitude IS NOT NULL THEN 0
                  WHEN (v.address IS NOT NULL AND TRIM(v.address) <> '')
                        OR (v.latitude IS NOT NULL AND v.longitude IS NOT NULL) THEN 1
                  ELSE 2 END,
             CASE WHEN v.venueType IS NOT NULL AND TRIM(v.venueType) <> '' THEN 0 ELSE 1 END,
             LOWER(v.name), LOWER(v.city), v.id
           """)
    Slice<Venue> suggest(
            @Param("name") String name,
            @Param("city") String city,
            @Param("sport") String sport,
            @Param("district") String district,
            Pageable pageable);

    /**
     * Approximate reference for an exact registered locality. No external
     * geocoding, and no guess if none of its venues have valid coordinates.
     */
    @Query("""
           SELECT AVG(v.latitude), AVG(v.longitude), COUNT(v.id)
           FROM Venue v
           WHERE LOWER(FUNCTION('translate', v.city,
                   'áàâãäéèêëíìîïóòôõöúùûüç',
                   'aaaaaeeeeiiiiooooouuuuc')) = :city
             AND v.latitude BETWEEN -90 AND 90
             AND v.longitude BETWEEN -180 AND 180
           """)
    List<Object[]> nearbyCityReference(@Param("city") String city);

    /** Only the fields needed for import deduplication, not complete Venue entities. */
    interface VenueDeduplicationView {
        String getName();
        Double getLatitude();
        Double getLongitude();
        String getSourceReference();
    }

    List<VenueDeduplicationView> findAllProjectedBy();

    /** Grouped coverage report; no full-table entity loading. */
    @Query("""
           SELECT v.district, COUNT(v),
              SUM(CASE WHEN v.sports IS NULL OR v.sports = '' THEN 1 ELSE 0 END),
              SUM(CASE WHEN v.latitude IS NULL OR v.longitude IS NULL THEN 1 ELSE 0 END)
           FROM Venue v GROUP BY v.district
           """)
    List<Object[]> coverageByDistrict();

    /**
     * Admin-only, paginated GPS verification queue. Real saved addresses and
     * source references are clues for human review, never inferred coordinates.
     * Deterministic order across pages; no loading the whole national catalogue.
     */
    @Query("""
           SELECT v FROM Venue v
           WHERE (v.latitude IS NULL OR v.longitude IS NULL)
             AND (:district = '' OR v.district = :district)
           ORDER BY
             CASE WHEN v.address IS NOT NULL AND TRIM(v.address) <> '' THEN 0 ELSE 1 END,
             CASE WHEN v.sourceReference IS NOT NULL AND TRIM(v.sourceReference) <> '' THEN 0 ELSE 1 END,
             LOWER(v.name), LOWER(v.city), v.id
           """)
    Slice<Venue> missingGpsReview(@Param("district") String district, Pageable pageable);

    /** Admin-only quality coverage; counts, never personal data or full venue entities. */
    @Query("""
           SELECT v.district, COUNT(v),
              SUM(CASE WHEN v.sports IS NULL OR TRIM(v.sports) = '' THEN 1 ELSE 0 END),
              SUM(CASE WHEN v.latitude IS NULL OR v.longitude IS NULL THEN 1 ELSE 0 END),
              SUM(CASE WHEN v.address IS NULL OR TRIM(v.address) = '' THEN 1 ELSE 0 END),
              SUM(CASE WHEN (v.address IS NULL OR TRIM(v.address) = '')
                     AND (v.latitude IS NULL OR v.longitude IS NULL)
                  THEN 1 ELSE 0 END)
           FROM Venue v GROUP BY v.district
           """)
    List<Object[]> qualityByDistrict();


    List<Venue> findByCityContainingIgnoreCaseOrderByNameAsc(String city);

    List<Venue> findByLatitudeBetweenAndLongitudeBetweenOrderByNameAsc(
            Double minLatitude, Double maxLatitude, Double minLongitude, Double maxLongitude);

    boolean existsByNameIgnoreCaseAndCityIgnoreCaseAndAddressIgnoreCase(String name, String city, String address);

    Optional<Venue> findFirstByNameIgnoreCaseAndLatitudeAndLongitude(
            String name, Double latitude, Double longitude);

    Optional<Venue> findByInformationSourceAndSourceReference(
            Venue.InformationSource informationSource,
            String sourceReference);
}
