package com.example.desporto24.repository.session;

import com.example.desporto24.model.session.SessionExperienceRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionExperienceRatingRepository extends JpaRepository<SessionExperienceRating, Long> {

    Optional<SessionExperienceRating> findBySessionIdAndProfileId(Long sessionId, Long profileId);
}
