package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityTournamentSuspension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.util.List;

public interface CommunityTournamentSuspensionRepository extends JpaRepository<CommunityTournamentSuspension, Long> {
    @Query("""
            select count(s) from CommunityTournamentSuspension s
            where s.communityId = :communityId and s.targetProfileId = :profileId
              and s.startsAt <= :now and s.endsAt > :now and s.revokedAt is null
            """)
    long countActive(@Param("communityId") Long communityId,
                     @Param("profileId") Long profileId, @Param("now") Instant now);

    List<CommunityTournamentSuspension> findTop100ByCommunityIdOrderByStartsAtDescIdDesc(Long communityId);
    List<CommunityTournamentSuspension> findTop100ByCommunityIdAndTargetProfileIdOrderByStartsAtDescIdDesc(
            Long communityId, Long targetProfileId);
}
