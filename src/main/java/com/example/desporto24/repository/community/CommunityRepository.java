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
              AND (:sport = '' OR LOWER(COALESCE(c.sport, '')) LIKE CONCAT('%', :sport, '%') ESCAPE '!')
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

    boolean existsByOwnerId(Long ownerId);
}
