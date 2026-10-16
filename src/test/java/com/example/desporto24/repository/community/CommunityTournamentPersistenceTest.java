package com.example.desporto24.repository.community;

import com.example.desporto24.enumeration.TournamentFormat;
import com.example.desporto24.enumeration.TournamentMatchStatus;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityTournament;
import com.example.desporto24.model.community.CommunityTournamentMatch;
import com.example.desporto24.model.community.CommunityTournamentParticipant;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:tournament-foundation;MODE=LEGACY;NON_KEYWORDS=SESSION;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.hikari.schema=PUBLIC"
})
class CommunityTournamentPersistenceTest {

    @Autowired private EntityManager entityManager;
    @Autowired private CommunityTournamentRepository tournamentRepository;
    @Autowired private CommunityTournamentParticipantRepository participantRepository;
    @Autowired private CommunityTournamentMatchRepository matchRepository;

    @Test
    void persistsLeagueParticipantsAndScheduledMatch() {
        Profile owner = new Profile();
        owner.setUsername("owner-tournament-fixture");
        entityManager.persist(owner);

        Profile opponent = new Profile();
        opponent.setUsername("opponent-tournament-fixture");
        entityManager.persist(opponent);

        Community community = Community.builder()
                .name("Liga de amigos")
                .visibility(CommunityVisibility.PRIVATE)
                .owner(owner)
                .rankingsEnabled(true)
                .createdAt(Instant.now())
                .build();
        entityManager.persist(community);

        CommunityTournament tournament = tournamentRepository.saveAndFlush(
                CommunityTournament.builder()
                        .community(community)
                        .name("Liga 2026/27")
                        .createdAt(Instant.now())
                        .build());
        assertEquals(TournamentFormat.LEAGUE, tournament.getFormat());
        assertEquals(TournamentStatus.DRAFT, tournament.getStatus());
        assertEquals(3, tournament.getWinPoints());

        CommunityTournamentParticipant first = participantRepository.saveAndFlush(
                CommunityTournamentParticipant.builder()
                        .tournament(tournament)
                        .profile(owner)
                        .joinedAt(Instant.now())
                        .build());
        CommunityTournamentParticipant second = participantRepository.saveAndFlush(
                CommunityTournamentParticipant.builder()
                        .tournament(tournament)
                        .profile(opponent)
                        .joinedAt(Instant.now())
                        .build());

        CommunityTournamentMatch scheduled = matchRepository.saveAndFlush(
                CommunityTournamentMatch.builder()
                        .tournament(tournament)
                        .homeParticipant(first)
                        .awayParticipant(second)
                        .roundNumber(1)
                        .build());

        entityManager.clear();

        List<CommunityTournament> tournaments =
                tournamentRepository.findByCommunityIdOrderByCreatedAtDescIdDesc(community.getId());
        assertEquals(List.of(tournament.getId()), tournaments.stream().map(CommunityTournament::getId).toList());
        assertTrue(participantRepository.existsByTournamentIdAndProfileId(tournament.getId(), owner.getId()));
        assertEquals(2, participantRepository.findByTournamentIdOrderByIdAsc(tournament.getId()).size());

        var loaded = matchRepository.findByTournamentIdOrderByRoundNumberAscIdAsc(tournament.getId());
        assertEquals(List.of(scheduled.getId()), loaded.stream().map(CommunityTournamentMatch::getId).toList());
        assertEquals(TournamentMatchStatus.SCHEDULED, loaded.get(0).getStatus());
        assertNull(loaded.get(0).getHomeScore());
        assertNull(loaded.get(0).getAwayScore());
        assertNotEquals(loaded.get(0).getHomeParticipant().getId(), loaded.get(0).getAwayParticipant().getId());
    }


    @Test
    void scheduledMatchAndRosterCanBeDeletedInForeignKeyOrder() {
        Profile first = new Profile();
        first.setUsername("delete-test-owner");
        entityManager.persist(first);
        Profile second = new Profile();
        second.setUsername("delete-test-opponent");
        entityManager.persist(second);
        var community = Community.builder().name("Grupo teste")
                .visibility(CommunityVisibility.PRIVATE).owner(first)
                .createdAt(Instant.now()).build();
        entityManager.persist(community);
        var tournament = tournamentRepository.saveAndFlush(CommunityTournament.builder()
                .community(community).name("Torneio de teste").createdAt(Instant.now()).build());
        var home = participantRepository.saveAndFlush(CommunityTournamentParticipant.builder()
                .tournament(tournament).profile(first).joinedAt(Instant.now()).build());
        var away = participantRepository.saveAndFlush(CommunityTournamentParticipant.builder()
                .tournament(tournament).profile(second).joinedAt(Instant.now()).build());
        matchRepository.saveAndFlush(CommunityTournamentMatch.builder().tournament(tournament)
                .homeParticipant(home).awayParticipant(away).roundNumber(1).build());
        entityManager.flush();
        entityManager.clear();

        matchRepository.deleteAll(matchRepository.findByTournamentIdOrderByRoundNumberAscIdAsc(tournament.getId()));
        matchRepository.flush();
        participantRepository.deleteAll(participantRepository.findByTournamentIdOrderByIdAsc(tournament.getId()));
        participantRepository.flush();
        tournamentRepository.deleteById(tournament.getId());
        tournamentRepository.flush();
        entityManager.clear();

        assertFalse(tournamentRepository.existsById(tournament.getId()));
        assertFalse(matchRepository.existsByTournamentId(tournament.getId()));
        assertTrue(participantRepository.findByTournamentIdOrderByIdAsc(tournament.getId()).isEmpty());
    }

    @Test
    void allowsRecordingAndReopeningACompletedMatchFromTheRepository() {
        Profile owner = new Profile();
        owner.setUsername("owner-results-fixture");
        entityManager.persist(owner);
        Profile rival = new Profile();
        rival.setUsername("rival-results-fixture");
        entityManager.persist(rival);
        Community community = Community.builder()
                .name("Amigos").visibility(CommunityVisibility.PUBLIC).owner(owner)
                .createdAt(Instant.now()).build();
        entityManager.persist(community);

        var tournament = tournamentRepository.saveAndFlush(CommunityTournament.builder()
                .community(community).name("Liga").createdAt(Instant.now()).build());
        var home = participantRepository.saveAndFlush(CommunityTournamentParticipant.builder()
                .tournament(tournament).profile(owner).joinedAt(Instant.now()).build());
        var away = participantRepository.saveAndFlush(CommunityTournamentParticipant.builder()
                .tournament(tournament).profile(rival).joinedAt(Instant.now()).build());
        var match = matchRepository.saveAndFlush(CommunityTournamentMatch.builder()
                .tournament(tournament).homeParticipant(home).awayParticipant(away)
                .roundNumber(1).status(TournamentMatchStatus.COMPLETED)
                .homeScore(2).awayScore(1).completedAt(Instant.now()).build());

        entityManager.clear();
        var saved = matchRepository.findById(match.getId()).orElseThrow();
        assertEquals(TournamentMatchStatus.COMPLETED, saved.getStatus());
        assertEquals(2, saved.getHomeScore());
        assertEquals(1, saved.getAwayScore());
    }
}
