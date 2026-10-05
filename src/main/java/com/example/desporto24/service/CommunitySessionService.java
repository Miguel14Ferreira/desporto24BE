package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.model.Session;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.dto.session.SessionSummaryResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionWaitlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunitySessionService {

    private final CommunityRepository communityRepository;
    private final CommunityMemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private final SessionRepository sessionRepository;
    private final SessionParticipantRepository participantRepository;
    private final SessionWaitlistRepository waitlistRepository;
    private final SessionViewMapper sessionViewMapper;

    public List<SessionSummaryResponse> sessions(Long communityId, String username) {
        Profile viewer = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("Comunidade não encontrada"));

        boolean member = memberRepository.existsByCommunityIdAndProfileId(
                communityId, viewer.getId());

        if (!member && community.getVisibility() == CommunityVisibility.PRIVATE) {
            throw new AccessDeniedException("Não tens acesso a esta comunidade");
        }

        List<Session> sessions = sessionRepository
                .findByCommunityIdOrderByGameDateDesc(communityId)
                .stream()
                .filter(session -> member || session.getAudience() != SessionAudience.COMMUNITY_ONLY)
                .toList();

        if (sessions.isEmpty()) {
            return List.of();
        }

        List<Long> sessionIds = sessions.stream()
                .map(Session::getId)
                .toList();

        java.util.Map<Long, Integer> participantCounts = new java.util.HashMap<>();
        participantRepository.countBySessionIdsAndState(sessionIds, ParticipantState.ACTIVE)
                .forEach(row -> participantCounts.put(
                        ((Number) row[0]).longValue(),
                        ((Number) row[1]).intValue()
                ));

        java.util.Set<Long> joinedSessionIds = new java.util.HashSet<>(
                participantRepository.findSessionIdsBySessionIdsAndProfileIdAndState(
                        sessionIds,
                        viewer.getId(),
                        ParticipantState.ACTIVE
                )
        );

        java.util.Set<Long> waitlistedSessionIds = new java.util.HashSet<>(
                waitlistRepository.findSessionIdsBySessionIdsAndProfileId(
                        sessionIds,
                        viewer.getId()
                )
        );

        return sessions.stream()
                .map(session -> sessionViewMapper.toSummary(
                        session,
                        null,
                        participantCounts.getOrDefault(session.getId(), 0),
                        joinedSessionIds.contains(session.getId()),
                        waitlistedSessionIds.contains(session.getId())
                ))
                .toList();
    }
    @Transactional
    public SessionSummaryResponse openToPublic(Long communityId, Long sessionId, String username) {
        Profile actor = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        var membership = memberRepository.findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));

        if (membership.getRole() != CommunityRole.OWNER && membership.getRole() != CommunityRole.ADMIN) {
            throw new AccessDeniedException("Só owner ou admin pode abrir a sessão ao público");
        }

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada"));
        if (session.getCommunity() == null || !communityId.equals(session.getCommunity().getId())) {
            throw new IllegalArgumentException("A sessão não pertence a esta comunidade");
        }

        session.setAudience(SessionAudience.PUBLIC);
        return sessionViewMapper.toSummary(sessionRepository.save(session), null);
    }

}
