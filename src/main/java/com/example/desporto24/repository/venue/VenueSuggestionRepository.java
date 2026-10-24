package com.example.desporto24.repository.venue;

import com.example.desporto24.model.venue.VenueSuggestion;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VenueSuggestionRepository extends JpaRepository<VenueSuggestion, Long> {
    Slice<VenueSuggestion> findByStatusOrderBySubmittedAtAsc(
        VenueSuggestion.Status status, Pageable pageable);

    boolean existsByStatusAndSubmittedByUsernameIgnoreCaseAndNameIgnoreCaseAndCityIgnoreCase(
        VenueSuggestion.Status status, String submittedByUsername, String name, String city);

    /** Serialize competing moderation clicks. An approval can publish only once. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM VenueSuggestion s WHERE s.id = :id")
    Optional<VenueSuggestion> findForReview(@Param("id") Long id);
}
