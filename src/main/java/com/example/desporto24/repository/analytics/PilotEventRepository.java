package com.example.desporto24.repository.analytics;

import com.example.desporto24.enumeration.PilotEventType;
import com.example.desporto24.model.analytics.PilotEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PilotEventRepository extends JpaRepository<PilotEvent, Long> {

    boolean existsByEventTypeAndDeviceId(PilotEventType eventType, String deviceId);

    boolean existsByEventTypeAndProfileId(PilotEventType eventType, Long profileId);
}
