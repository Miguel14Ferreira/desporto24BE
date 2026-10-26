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

        var district = repository.suggest("", "", "padel", "Lisboa", PageRequest.of(0, 40));
        assertEquals(1, district.getContent().size());
        assertEquals("Lisboa", district.getContent().getFirst().getDistrict());
        var fallback = repository.suggest("campo antigo", "", "padel", "", PageRequest.of(0, 40));
        assertEquals(1, fallback.getContent().size());
    }


    @Test
    void ranksExactNameThenPrefixBeforeSubstringAcrossPageBoundaries() {
        persist("Alto Campo Central", "Lisboa", "Futsal");
        persist("Campo Norte", "Lisboa", "Futsal");
        persist("Campo", "Lisboa", "Futsal");
        entityManager.flush();
        entityManager.clear();

        var first = repository.suggest("campo", "", "futsal", "", PageRequest.of(0, 1));
        var second = repository.suggest("campo", "", "futsal", "", PageRequest.of(1, 1));
        var third = repository.suggest("campo", "", "futsal", "", PageRequest.of(2, 1));

        assertEquals("Campo", first.getContent().getFirst().getName());
        assertEquals("Campo Norte", second.getContent().getFirst().getName());
        assertEquals("Alto Campo Central", third.getContent().getFirst().getName());
        assertTrue(first.hasNext());
        assertTrue(second.hasNext());
        assertFalse(third.hasNext());
    }

    @Test
    void ranksExactCityAndCompleteVenueDetailsBeforePaginationNotJustWithinOnePage() {
        persistWithInfo("A - Lisboa Metropolitana", "Grande Lisboa", "Futsal", "Rua A", 38.7, -9.1, "Pavilhão");
        persistWithInfo("B - Lisboa incompleto", "Lisboa", null, "", null, null, null);
        persistWithInfo("C - Lisboa confirmado", "Lisboa", "Futsal", "Rua C", 38.72, -9.12, "Pavilhão");
        persistWithInfo("D - Lisboa GPS", "Lisboa", "Futsal", "", 38.73, -9.13, "Campo");
        persistWithInfo("E - Lisboa sem localização", "Lisboa", "Futsal", "", null, null, null);
        entityManager.flush();
        entityManager.clear();

        var ordered = java.util.stream.IntStream.range(0, 5)
                .mapToObj(page -> repository.suggest("", "lisboa", "", "", PageRequest.of(page, 1)))
                .toList();

        assertEquals(java.util.List.of(
                "C - Lisboa confirmado", "D - Lisboa GPS",
                "E - Lisboa sem localização", "B - Lisboa incompleto",
                "A - Lisboa Metropolitana"),
                ordered.stream().map(p -> p.getContent().getFirst().getName()).toList());
        assertEquals(5, ordered.stream().map(p -> p.getContent().getFirst().getId()).distinct().count());
        assertTrue(ordered.get(3).hasNext());
        assertFalse(ordered.get(4).hasNext());
    }

    @Test
    void stableTieOrderUsesNameCityAndImmutableIdAcrossPages() {
        persistWithInfo("Campo Igual", "Lisboa", "Padel", "Rua A", null, null, "Campo");
        persistWithInfo("Campo Igual", "Lisboa", "Padel", "Rua A", null, null, "Campo");
        persistWithInfo("Campo Igual", "Lisboa", "Padel", "Rua A", null, null, "Campo");
        entityManager.flush();
        entityManager.clear();

        var first = repository.suggest("", "", "", "", PageRequest.of(0, 1));
        var middle = repository.suggest("", "", "", "", PageRequest.of(1, 1));
        var last = repository.suggest("", "", "", "", PageRequest.of(2, 1));
        assertTrue(first.hasNext());
        assertTrue(middle.hasNext());
        assertFalse(last.hasNext());
        long firstId = first.getContent().getFirst().getId();
        long middleId = middle.getContent().getFirst().getId();
        long lastId = last.getContent().getFirst().getId();
        assertTrue(firstId < middleId && middleId < lastId);
    }

    private void persistWithInfo(String name, String city, String sports, String address,
                                 Double latitude, Double longitude, String type) {
        entityManager.persist(Venue.builder().name(name).city(city).sports(sports)
                .address(address).latitude(latitude).longitude(longitude).venueType(type)
                .informationSource(Venue.InformationSource.COMMUNITY)
                .createdAt(Instant.now()).build());
    }

    private void persist(String name, String city, String sports) {
        entityManager.persist(Venue.builder()
                .name(name).city(city).address("Rua A")
                .sports(sports).informationSource(Venue.InformationSource.COMMUNITY)
                .createdAt(Instant.now()).build());
    }
}
