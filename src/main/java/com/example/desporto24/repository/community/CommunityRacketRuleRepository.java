package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityRacketRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CommunityRacketRuleRepository extends JpaRepository<CommunityRacketRule, Long> {
    List<CommunityRacketRule> findBySeasonId(Long seasonId);
    Optional<CommunityRacketRule> findBySeasonIdAndSportIgnoreCaseAndMatchFormat(
            Long seasonId, String sport, String matchFormat);
}
