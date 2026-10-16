package com.example.desporto24.repository.community;

import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:community-public-discovery;MODE=LEGACY;NON_KEYWORDS=SESSION;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.hikari.schema=PUBLIC"
})
class CommunityPublicDiscoveryTest {
    @Autowired private CommunityRepository repository;
    @Autowired private EntityManager entityManager;

    @Test
    void unfilteredBrowseReturnsOnlyPublicCommunitiesWithPagination() {
        Profile owner = new Profile();
        owner.setUsername("discover-fixture");
        entityManager.persist(owner);

        persist(owner, "A - pública", CommunityVisibility.PUBLIC);
        persist(owner, "B - privada", CommunityVisibility.PRIVATE);
        persist(owner, "C - pública", CommunityVisibility.PUBLIC);
        entityManager.flush();
        entityManager.clear();

        var first = repository.discoverPublicPresentations(PageRequest.of(0, 1));
        assertEquals(1, first.getContent().size());
        assertEquals("A - pública", first.getContent().get(0).name());
        assertTrue(first.hasNext());
        assertEquals(CommunityVisibility.PUBLIC, first.getContent().get(0).visibility());

        var second = repository.discoverPublicPresentations(PageRequest.of(1, 1));
        assertEquals("C - pública", second.getContent().get(0).name());
        assertFalse(second.hasNext());
    }


    @Test
    void filteredSearchMatchesLegacyAndAnySelectedSportWithoutDuplicates() {
        Profile owner = new Profile();
        owner.setUsername("multi-sport-discovery-fixture");
        entityManager.persist(owner);

        persistWithSports(owner, "A - antiga", "Padel", java.util.List.of());
        persistWithSports(owner, "B - multidesporto", "Futebol", java.util.List.of("Futebol", "PADEL"));
        persistWithSports(owner, "C - principal e lista", "Padel", java.util.List.of("Padel", "Ténis"));
        persistWithSports(owner, "D - outra", "Basquetebol", java.util.List.of("Basquetebol"));
        entityManager.flush();
        entityManager.clear();

        var first = repository.searchPresentations("", "padel", PageRequest.of(0, 2));
        assertEquals(java.util.List.of("A - antiga", "B - multidesporto"),
                first.getContent().stream().map(result -> result.name()).toList());
        assertTrue(first.hasNext());

        var second = repository.searchPresentations("", "padel", PageRequest.of(1, 2));
        assertEquals(java.util.List.of("C - principal e lista"),
                second.getContent().stream().map(result -> result.name()).toList());
        assertFalse(second.hasNext());

        var nameAndSport = repository.searchPresentations("multidesporto", "padel", PageRequest.of(0, 20));
        assertEquals(1, nameAndSport.getContent().size());
        assertEquals("B - multidesporto", nameAndSport.getContent().get(0).name());
        assertTrue(repository.searchPresentations("", "corrida", PageRequest.of(0, 20)).isEmpty());
    }

    private void persistWithSports(Profile owner, String name, String sport, java.util.List<String> sports) {
        entityManager.persist(Community.builder().owner(owner).name(name).sport(sport)
                .sports(new java.util.ArrayList<>(sports))
                .visibility(CommunityVisibility.PUBLIC).rankingsEnabled(false)
                .createdAt(Instant.now()).build());
    }

    private void persist(Profile owner, String name, CommunityVisibility visibility) {
        entityManager.persist(Community.builder().owner(owner).name(name)
                .visibility(visibility).rankingsEnabled(false).createdAt(Instant.now()).build());
    }
}
