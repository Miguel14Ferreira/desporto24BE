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

    @Test
    void adminSearchIncludesSuspendedAccountsButExcludesDeletedAccounts() {
        Profile deleted = repository.findUserByUsername("deleted-152-fixture").orElseThrow();
        deleted.setEmail("deleted-152-fixture@deleted.invalid");
        repository.saveAndFlush(deleted);
        assertEquals(List.of("unverified-player"),
                repository.searchAdminPlayers("unverified", PageRequest.of(0, 20))
                        .getContent().stream().map(Profile::getUsername).toList());
        assertTrue(repository.searchAdminPlayers("deleted", PageRequest.of(0, 20)).isEmpty());
    }

    @Test
    void adminSearchMatchesNameAndNicknameAndReportsMorePages() {
        Profile named = repository.findUserByUsername("active-player").orElseThrow();
        named.setFullName("Ana Silva");
        named.setNickname("Anita");
        repository.saveAndFlush(named);
        assertEquals("active-player", repository.searchAdminPlayers("ana", PageRequest.of(0, 20))
                .getContent().get(0).getUsername());
        assertEquals("active-player", repository.searchAdminPlayers("anita", PageRequest.of(0, 20))
                .getContent().get(0).getUsername());
        assertTrue(repository.searchAdminPlayers("player", PageRequest.of(0, 1)).hasNext());
    }

    private void save(String username, Boolean enabled) {
        Profile profile = new Profile();
        profile.setUsername(username);
        profile.setUserId(username);
        profile.setEnabled(enabled);
        repository.saveAndFlush(profile);
    }
}
