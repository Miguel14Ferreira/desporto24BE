package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunitySportPointsRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CommunitySportPointsRuleRepository extends JpaRepository<CommunitySportPointsRule, Long> {
    List<CommunitySportPointsRule> findBySeasonId(Long seasonId);
    Optional<CommunitySportPointsRule> findBySeasonIdAndSportIgnoreCase(Long seasonId, String sport);
}
