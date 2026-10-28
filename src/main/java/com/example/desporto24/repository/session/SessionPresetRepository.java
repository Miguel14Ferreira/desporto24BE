package com.example.desporto24.repository.session;

import com.example.desporto24.model.session.SessionPreset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SessionPresetRepository extends JpaRepository<SessionPreset, Long> {
    List<SessionPreset> findAllByOwner_IdOrderByUpdatedAtDesc(Long ownerId);
    Optional<SessionPreset> findByIdAndOwner_Id(Long id, Long ownerId);
    long countByOwner_Id(Long ownerId);
}
