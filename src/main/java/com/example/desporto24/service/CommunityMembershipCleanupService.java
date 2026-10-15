package com.example.desporto24.service;

import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Session;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionWaitlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CommunityMembershipCleanupService {
    private final SessionRepository sessionRepository;
    private final SessionParticipantRepository participantRepository;
    private final SessionWaitlistRepository waitlistRepository;
    private final WaitlistPromotionService promotionService;

    @EventListener
    @Transactional
    public void onMembershipEnded(CommunityMembershipEndedEvent event) {
        Instant now = Instant.now();
        for (Session candidate : sessionRepository.findByCommunityIdOrderByGameDateDesc(event.communityId())) {
            if (candidate.getAudience() != SessionAudience.COMMUNITY_ONLY
                    || candidate.getGameDate() == null
                    || !candidate.getGameDate().toInstant().isAfter(now)
                    || candidate.getStatus() == SessionStatus.COMPLETED
                    || candidate.getStatus() == SessionStatus.CANCELLED) {
                continue;
            }
            Session session = sessionRepository.findByIdForUpdate(candidate.getId()).orElseThrow();
            if (session.getAudience() != SessionAudience.COMMUNITY_ONLY
                    || session.getGameDate() == null
                    || !session.getGameDate().toInstant().isAfter(now)
                    || session.getStatus() == SessionStatus.COMPLETED
                    || session.getStatus() == SessionStatus.CANCELLED) {
                continue;
            }

            waitlistRepository.findBySessionIdAndProfileId(session.getId(), event.profileId())
                    .ifPresent(waitlistRepository::delete);

            participantRepository.findBySessionIdAndProfileIdAndState(
                    session.getId(), event.profileId(), ParticipantState.ACTIVE)
                    .ifPresent(participant -> {
                        participant.setState(ParticipantState.REMOVED);
                        participant.setExitedAt(now);
                        participant.setTeamCode(null);
                        participantRepository.save(participant);
                        promotionService.promoteNext(session, now);
                        var activeParticipants = participantRepository
                                .findBySessionIdAndStateOrderByJoinedAtAsc(
                                        session.getId(), ParticipantState.ACTIVE);
                        boolean teamsChanged = false;
                        for (var active : activeParticipants) {
                            if (active.getTeamCode() != null) {
                                active.setTeamCode(null);
                                teamsChanged = true;
                            }
                        }
                        if (teamsChanged) {
                            participantRepository.saveAll(activeParticipants);
                        }
                    });
        }
    }
}
