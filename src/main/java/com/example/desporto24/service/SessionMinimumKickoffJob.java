package com.example.desporto24.service;

import com.example.desporto24.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

/** Check due sessions in bounded batches; no changes before their scheduled kickoff. */
@Component
@RequiredArgsConstructor
public class SessionMinimumKickoffJob {
    private static final Logger log = LoggerFactory.getLogger(SessionMinimumKickoffJob.class);
    private final SessionRepository sessions;
    private final SessionMinimumClosureService closure;

    @Scheduled(fixedDelayString = "${jynlo.sessions.minimum-check-ms:60000}",
               initialDelayString = "${jynlo.sessions.minimum-initial-delay-ms:30000}")
    public void checkKickoffs() {
        Instant now = Instant.now();
        for (Long id : sessions.findDueUnderMinimum(Date.from(now), PageRequest.of(0, 100))) {
            try {
                closure.closeIfUnderMinimum(id, now);
            } catch (RuntimeException failed) {
                // A failed session must not stop other candidates; the next pass retries it.
                log.warn("Unable to reconcile minimum for session {}", id, failed);
            }
        }
    }
}
