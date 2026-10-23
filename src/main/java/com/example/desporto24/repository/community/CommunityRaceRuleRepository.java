package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityRaceRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CommunityRaceRuleRepository extends JpaRepository<CommunityRaceRule, Long> {
    List<CommunityRaceRule> findBySeasonId(Long seasonId);
    Optional<CommunityRaceRule> findBySeasonIdAndSportIgnoreCase(Long seasonId, String sport);
}
