package com.example.desporto24.repository.analytics;

import com.example.desporto24.enumeration.PilotEventType;
import com.example.desporto24.model.analytics.PilotEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PilotEventRepository extends JpaRepository<PilotEvent, Long> {

    long countByEventType(PilotEventType eventType);

    List<PilotEvent> findByEventType(PilotEventType eventType);

    boolean existsByEventTypeAndDeviceId(PilotEventType eventType, String deviceId);

    boolean existsByEventTypeAndProfileId(PilotEventType eventType, Long profileId);

    boolean existsByEventTypeAndSubjectId(PilotEventType eventType, Long subjectId);

    boolean existsByEventTypeAndProfileIdAndSubjectId(
            PilotEventType eventType,
            Long profileId,
            Long subjectId);
}
