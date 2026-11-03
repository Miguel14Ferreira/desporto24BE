package com.example.desporto24.repository.community;
import com.example.desporto24.model.community.CommunityTournamentRelayFinalist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;
public interface CommunityTournamentRelayFinalistRepository extends JpaRepository<CommunityTournamentRelayFinalist,Long> {
    @EntityGraph(attributePaths={"team"})
    List<CommunityTournamentRelayFinalist> findByTournamentIdOrderBySeedAsc(Long tournamentId);
    boolean existsByTournamentIdAndTeamId(Long tournamentId,Long teamId);
}
