package com.example.desporto24.repository.venue;

import com.example.desporto24.model.venue.VenueEditSuggestion;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface VenueEditSuggestionRepository extends JpaRepository<VenueEditSuggestion, Long> {
    Slice<VenueEditSuggestion> findByStatusOrderBySubmittedAtAsc(
            VenueEditSuggestion.Status status, Pageable pageable);

    boolean existsByVenue_IdAndSubmittedByUsernameIgnoreCaseAndStatus(
            Long venueId, String username, VenueEditSuggestion.Status status);

    Optional<VenueEditSuggestion> findFirstByVenue_IdAndSubmittedByUsernameIgnoreCaseOrderBySubmittedAtDesc(
            Long venueId, String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM VenueEditSuggestion s WHERE s.id = :id")
    Optional<VenueEditSuggestion> findForReview(@Param("id") Long id);
}
