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

    private void persist(Profile owner, String name, CommunityVisibility visibility) {
        entityManager.persist(Community.builder().owner(owner).name(name)
                .visibility(visibility).rankingsEnabled(false).createdAt(Instant.now()).build());
    }
}
