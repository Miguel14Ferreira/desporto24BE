package com.example.desporto24.repository.venue;

import com.example.desporto24.model.venue.Venue;
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
        "spring.datasource.url=jdbc:h2:mem:venue-search-page;MODE=LEGACY;NON_KEYWORDS=SESSION;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.hikari.schema=PUBLIC"
})
class VenuePagedSearchRepositoryTest {
    @Autowired VenueRepository repository;
    @Autowired EntityManager entityManager;

    @Test
    void supportsAccentInsensitiveNamesExactSportTokensAndStablePages() {
        persist("Pavilhão Norte", "Lisboa", "Futebol;Futsal");
        persist("Pavilhão Norte", "Lisboa", "Futsal;Padel");
        persist("Pavilhão Sul", "Lisboa", "Futebol Americano");
        persist("Campo do Ténis", "Porto", "Ténis");
        entityManager.flush();
        entityManager.clear();

        var first = repository.suggest("pavilhao", "lisboa", "futsal", "", PageRequest.of(0, 1));
        var second = repository.suggest("pavilhao", "lisboa", "futsal", "", PageRequest.of(1, 1));
        assertEquals(1, first.getContent().size());
        assertTrue(first.hasNext());
        assertEquals(1, second.getContent().size());
        assertFalse(second.hasNext());
        assertNotEquals(first.getContent().get(0).getId(), second.getContent().get(0).getId());

        var football = repository.suggest("", "", "futebol", "", PageRequest.of(0, 40));
        assertEquals(1, football.getContent().size());
        assertEquals("Pavilhão Norte", football.getContent().get(0).getName());

        var tennis = repository.suggest("tenis", "porto", "tenis", "", PageRequest.of(0, 40));
        assertEquals("Campo do Ténis", tennis.getContent().get(0).getName());
    }

    @Test
    void filtersByDistrictWithoutIncludingLegacyUnclassifiedVenues() {
        entityManager.persist(Venue.builder().name("Campo da Cidade").city("Lisboa")
                .district("Lisboa").address("Rua A").sports("Padel")
                .informationSource(Venue.InformationSource.COMMUNITY)
                .createdAt(Instant.now()).build());
        entityManager.persist(Venue.builder().name("Campo da Cidade").city("Porto")
                .district("Porto").address("Rua B").sports("Padel")
                .informationSource(Venue.InformationSource.COMMUNITY)
                .createdAt(Instant.now()).build());
        persist("Campo antigo", "Lisboa", "Padel"); // Still findable by unrestricted legacy search.
        entityManager.flush();
        entityManager.clear();

        var district = repository.suggest("", "", "padel", "lisboa", PageRequest.of(0, 40));
        assertEquals(1, district.getContent().size());
        assertEquals("Lisboa", district.getContent().getFirst().getDistrict());
        var fallback = repository.suggest("campo antigo", "", "padel", "", PageRequest.of(0, 40));
        assertEquals(1, fallback.getContent().size());
    }

    private void persist(String name, String city, String sports) {
        entityManager.persist(Venue.builder()
                .name(name).city(city).address("Rua A")
                .sports(sports).informationSource(Venue.InformationSource.COMMUNITY)
                .createdAt(Instant.now()).build());
    }
}
