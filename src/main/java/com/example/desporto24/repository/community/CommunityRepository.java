package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import com.example.desporto24.model.dto.community.CommunityDiscoveryResponse;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    @Query("""
            SELECT new com.example.desporto24.model.dto.community.CommunityDiscoveryResponse(
                c.id, c.name, c.description, c.sport, c.photoUrl, c.visibility)
            FROM Community c
            WHERE (:name = '' OR LOWER(c.name) LIKE CONCAT('%', :name, '%') ESCAPE '!')
              AND (:sport = ''
                   OR LOWER(COALESCE(c.sport, '')) LIKE CONCAT('%', :sport, '%') ESCAPE '!'
                   OR EXISTS (
                       SELECT 1 FROM Community matching
                       JOIN matching.sports selectedSport
                       WHERE matching.id = c.id
                         AND LOWER(selectedSport) LIKE CONCAT('%', :sport, '%') ESCAPE '!'
                   ))
            ORDER BY LOWER(c.name), c.id
            """)
    Slice<CommunityDiscoveryResponse> searchPresentations(
            @Param("name") String name, @Param("sport") String sport, Pageable pageable);

    @Query("""
            SELECT new com.example.desporto24.model.dto.community.CommunityDiscoveryResponse(
                c.id, c.name, c.description, c.sport, c.photoUrl, c.visibility)
            FROM Community c
            WHERE c.visibility = com.example.desporto24.enumeration.CommunityVisibility.PUBLIC
            ORDER BY LOWER(c.name), c.id
            """)
    Slice<CommunityDiscoveryResponse> discoverPublicPresentations(Pageable pageable);

    // Load all sport tags for one result page in one query (no N+1, no pagination over joins).
    @Query("SELECT DISTINCT c FROM Community c LEFT JOIN FETCH c.sports WHERE c.id IN :ids")
    java.util.List<Community> findAllWithSportsByIdIn(@Param("ids") java.util.List<Long> ids);

    @Query("SELECT c FROM Community c LEFT JOIN FETCH c.sports WHERE c.id = :id")
    java.util.Optional<Community> findWithSportsById(@Param("id") Long id);

    boolean existsByOwnerId(Long ownerId);
}
