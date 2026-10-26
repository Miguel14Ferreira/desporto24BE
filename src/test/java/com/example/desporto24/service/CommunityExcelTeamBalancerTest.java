package com.example.desporto24.service;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityPlayer;
import com.example.desporto24.model.community.CommunityStandingsArchive;
import com.example.desporto24.model.community.CommunityStandingsArchiveRow;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.community.CommunityPlayerRepository;
import com.example.desporto24.repository.community.CommunityStandingsArchiveRepository;
import com.example.desporto24.repository.community.CommunityStandingsArchiveRowRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommunityExcelTeamBalancerTest {

    @Test
    void tenConfirmedPlayersAreDividedFiveAgainstFiveWithOptimalExcelPoints() {
        var f = new Fixtures();
        List<SessionParticipant> registered = new ArrayList<>();
        List<CommunityStandingsArchiveRow> ranks = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            registered.add(f.player(i, "player" + i, null, null));
            ranks.add(f.rank(i, "player" + i, i, i));
        }
        f.ranking(ranks);
        var result = f.balancer.suggest(f.session, registered);
        assertEquals(5, result.teamA().size());
        assertEquals(5, result.teamB().size());
        assertEquals(10, result.playersWithStats());
        assertEquals(10, result.totalPlayers());
        assertEquals(1.0, Math.abs(result.teamAScore() - result.teamBScore()), 0.0001);
        assertEquals(55.0, result.teamAScore() + result.teamBScore(), 0.0001);
        assertTrue(result.teamA().stream().allMatch(p -> p.statsAvailable()));
        assertEquals(result, f.balancer.suggest(f.session, registered),
                "same active roster and ranking must produce deterministic teams");
    }

    @Test
    void linkedExcelNameAndAccentedFullNameResolveWithoutInventingPoints() {
        var f = new Fixtures();
        var first = f.player(1, "first-user", "João Silva", null);
        var second = f.player(2, "second-user", null, null);
        var third = f.player(3, "third-user", null, null);
        CommunityPlayer linked = mock(CommunityPlayer.class);
        when(linked.getProfile()).thenReturn(third.getProfile());
        when(linked.getNormalizedName()).thenReturn("historical nick");
        when(f.linked.findByCommunityIdOrderByDisplayNameAsc(7L)).thenReturn(List.of(linked));
        f.ranking(List.of(
                f.rank(1, "joao silva", 90, 3),
                f.rank(2, "Historical Nick", 70, 2)));
        var result = f.balancer.suggest(f.session, List.of(first, second, third));
        assertEquals(2, result.playersWithStats());
        assertEquals(3, result.totalPlayers());
        var all = new ArrayList<>(result.teamA());
        all.addAll(result.teamB());
        assertEquals(80.0, all.stream().filter(p -> p.username().equals("second-user"))
                .findFirst().orElseThrow().strengthScore());
        assertFalse(all.stream().filter(p -> p.username().equals("second-user"))
                .findFirst().orElseThrow().statsAvailable());
        assertEquals(90.0, all.stream().filter(p -> p.username().equals("first-user"))
                .findFirst().orElseThrow().strengthScore());
        assertEquals(70.0, all.stream().filter(p -> p.username().equals("third-user"))
                .findFirst().orElseThrow().strengthScore());
    }

    @Test
    void ambiguousSharedNicknameCannotClaimAnotherPlayersRanking() {
        var f = new Fixtures();
        var first = f.player(1, "player1", null, "Same nickname");
        var second = f.player(2, "player2", null, "Same nickname");
        f.ranking(List.of(f.rank(1, "Same nickname", 100, 1)));
        var error = assertThrows(ResponseStatusException.class,
                () -> f.balancer.suggest(f.session, List.of(first, second)));
        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
    }

    @Test
    void missingIndividualExcelArchiveFailsInsteadOfReportingFakeBalance() {
        var f = new Fixtures();
        var players = List.of(f.player(1, "first", null, null),
                f.player(2, "second", null, null));
        var error = assertThrows(ResponseStatusException.class,
                () -> f.balancer.suggest(f.session, players));
        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verifyNoInteractions(f.rows);
    }

    @Test
    void largerRostersHaveBoundedAndStableBalancedTeamSizes() {
        List<Double> points = new ArrayList<>();
        for (int i = 0; i < 50; i++) points.add((double) (i + 1));
        boolean[] solution = CommunityExcelTeamBalancer.balanceScores(points);
        assertEquals(25, countA(solution));
        assertArrayEquals(solution, CommunityExcelTeamBalancer.balanceScores(points));
        assertEquals(3, countA(CommunityExcelTeamBalancer.balanceScores(List.of(1d, 3d, 4d, 5d, 9d))));
    }

    private static int countA(boolean[] slots) {
        int result = 0;
        for (boolean a : slots) if (a) result++;
        return result;
    }

    private static class Fixtures {
        private final CommunityStandingsArchiveRepository archive = mock(CommunityStandingsArchiveRepository.class);
        private final CommunityStandingsArchiveRowRepository rows = mock(CommunityStandingsArchiveRowRepository.class);
        private final CommunityPlayerRepository linked = mock(CommunityPlayerRepository.class);
        private final CommunityExcelTeamBalancer balancer =
                new CommunityExcelTeamBalancer(archive, rows, linked);
        private final Session session = new Session();

        Fixtures() {
            Community community = mock(Community.class);
            when(community.getId()).thenReturn(7L);
            session.setCommunity(community);
            when(linked.findByCommunityIdOrderByDisplayNameAsc(7L)).thenReturn(List.of());
        }

        SessionParticipant player(int id, String username, String fullName, String nickname) {
            Profile profile = new Profile();
            profile.setId((long) id);
            profile.setUsername(username);
            profile.setFullName(fullName);
            profile.setNickname(nickname);
            SessionParticipant registered = new SessionParticipant();
            registered.setProfile(profile);
            return registered;
        }

        CommunityStandingsArchiveRow rank(long id, String name, int points, int games) {
            CommunityStandingsArchiveRow row = new CommunityStandingsArchiveRow();
            row.setId(id);
            // The real Excel importer persists canonical lower-case names.
            row.setNormalizedName(name.toLowerCase(java.util.Locale.ROOT));
            row.setPoints(points);
            row.setPlayed(games);
            return row;
        }

        void ranking(List<CommunityStandingsArchiveRow> ranking) {
            CommunityStandingsArchive snapshot = new CommunityStandingsArchive();
            snapshot.setId(90L);
            snapshot.setEntryType("INDIVIDUAL");
            when(archive.findFirstByCommunityIdAndEntryTypeOrderByCreatedAtDescIdDesc(7L, "INDIVIDUAL"))
                    .thenReturn(Optional.of(snapshot));
            when(rows.findByArchiveIdOrderByPositionAscIdAsc(90L)).thenReturn(ranking);
        }
    }
}
