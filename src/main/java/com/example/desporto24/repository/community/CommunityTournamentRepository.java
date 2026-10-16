package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityTournament;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;

import java.util.List;

public interface CommunityTournamentRepository extends JpaRepository<CommunityTournament, Long> {

    List<CommunityTournament> findByCommunityIdOrderByCreatedAtDescIdDesc(Long communityId);

    Slice<CommunityTournament> findByCommunityId(Long communityId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select tournament from CommunityTournament tournament where tournament.id = :id")
    Optional<CommunityTournament> findByIdForUpdate(@Param("id") Long id);
}
