package com.example.desporto24.repository;

import com.example.desporto24.model.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:profile-discovery;MODE=LEGACY;NON_KEYWORDS=SESSION;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.hikari.schema=PUBLIC"
})
class ProfileRepositoryDiscoveryTest {
    @Autowired private ProfileRepository repository;

    @BeforeEach
    void createProfiles() {
        save("actor", true);
        save("active-player", true);
        save("deleted-152-fixture", false);
        save("unverified-player", false);
        save("legacy-unknown", null);
    }

    @Test
    void discoveryExcludesDeletedAndUnverifiedProfilesBeforePagination() {
        List<Profile> results = repository.discoverProfiles("actor", PageRequest.of(0, 1));
        assertEquals(List.of("active-player"), results.stream().map(Profile::getUsername).toList());
    }

    @Test
    void publicSearchOnlyReturnsEnabledProfiles() {
        List<Profile> results = repository.searchPublicCandidates(
                "actor", "", "", "", "", PageRequest.of(0, 100));
        assertEquals(List.of("active-player"), results.stream().map(Profile::getUsername).toList());
    }

    @Test
    void searchingDeletedUsernameDoesNotRevealAnonymizedAccount() {
        assertTrue(repository.searchPublicCandidates(
                "actor", "deleted", "", "", "", PageRequest.of(0, 100)).isEmpty());
    }

    private void save(String username, Boolean enabled) {
        Profile profile = new Profile();
        profile.setUsername(username);
        profile.setUserId(username);
        profile.setEnabled(enabled);
        repository.saveAndFlush(profile);
    }
}
