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

    boolean existsByInformationSourceAndSourceReference(
            Venue.InformationSource informationSource,
            String sourceReference);

    @Query("""
            SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END
            FROM Venue v
            WHERE LOWER(v.name) = LOWER(:name)
              AND v.latitude BETWEEN :minLatitude AND :maxLatitude
              AND v.longitude BETWEEN :minLongitude AND :maxLongitude
            """)
    boolean existsNearNameAndCoordinates(
            @Param("name") String name,
            @Param("minLatitude") double minLatitude,
            @Param("maxLatitude") double maxLatitude,
            @Param("minLongitude") double minLongitude,
            @Param("maxLongitude") double maxLongitude);


    @Query("""
            SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END
            FROM Venue v
            WHERE v.latitude BETWEEN :minLatitude AND :maxLatitude
              AND v.longitude BETWEEN :minLongitude AND :maxLongitude
            """)
    boolean existsWithinCoordinateBox(
            @Param("minLatitude") double minLatitude,
            @Param("maxLatitude") double maxLatitude,
            @Param("minLongitude") double minLongitude,
            @Param("maxLongitude") double maxLongitude);

    long countByInformationSourceAndSourceReferenceStartingWith(
            Venue.InformationSource informationSource,
            String sourceReferencePrefix);

    long countByInformationSourceAndSourceReferenceStartingWithAndSportsIsNotNull(
            Venue.InformationSource informationSource,
            String sourceReferencePrefix);

    List<Venue> findTop12ByInformationSourceAndSourceReferenceStartingWithAndSportsIsNullOrderByNameAsc(
            Venue.InformationSource informationSource,
            String sourceReferencePrefix);

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
