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
        "spring.datasource.url=jdbc:h2:mem:venue-quality-city;MODE=LEGACY;NON_KEYWORDS=SESSION;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.hikari.schema=PUBLIC"
})
class VenueQualityByCityRepositoryTest {
    @Autowired VenueRepository repository;
    @Autowired EntityManager em;

    @Test void sortsByMissingSportsAndBoundsTheResponse() {
        add("A", "Alcabideche", null, null);
        add("B", "Alcabideche", " ", null);
        add("C", "Alcabideche", "Padel", "Lisboa");
        add("D", "Porto", "Futebol", "Porto");
        em.flush();
        em.clear();

        var top = repository.qualityByCity(PageRequest.of(0, 1));
        assertEquals(1, top.size());
        assertEquals("Alcabideche", top.getFirst()[0]);
        assertEquals(3L, ((Number) top.getFirst()[1]).longValue());
        assertEquals(2L, ((Number) top.getFirst()[2]).longValue());
        assertEquals(2L, ((Number) top.getFirst()[3]).longValue());
        assertEquals(3L, ((Number) top.getFirst()[4]).longValue());
    }

    private void add(String name, String city, String sport, String district) {
        em.persist(Venue.builder().name(name).city(city).address("Rua A")
                .sports(sport).district(district).createdAt(Instant.now()).build());
    }
}
