package com.example.desporto24.repository.session;

import com.example.desporto24.model.session.SessionOrganizerExclusion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionOrganizerExclusionRepository
        extends JpaRepository<SessionOrganizerExclusion, Long> {

    boolean existsByOrganizerIdAndExcludedId(Long organizerId, Long excludedId);

    long deleteByOrganizerIdAndExcludedId(Long organizerId, Long excludedId);
}
