package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Session;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.dto.session.SessionSummaryResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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
    private final com.example.desporto24.repository.session.SessionCommunityShareRepository sessionCommunityShareRepository;
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

        // Include cross-posted public sessions in the group's session list.
        // Existing community-only sessions keep their independent ACL and ranking.
        java.util.Map<Object, Session> visible = new java.util.LinkedHashMap<>();
        sessionRepository.findByCommunityIdOrderByGameDateDesc(communityId)
                .stream()
                .filter(session -> member || session.getAudience() == SessionAudience.PUBLIC)
                .forEach(session -> visible.put(session.getId() != null ? session.getId() : session, session));
        sessionCommunityShareRepository.sessionsSharedWithCommunity(communityId)
                .stream()
                .filter(session -> session.getAudience() == SessionAudience.PUBLIC
                        && !Boolean.TRUE.equals(session.getPrivateGame()))
                .forEach(session -> visible.putIfAbsent(session.getId() != null ? session.getId() : session, session));
        return visible.values().stream()
                .sorted(java.util.Comparator.comparing(Session::getGameDate,
                        java.util.Comparator.nullsLast(java.util.Comparator.reverseOrder())))
                .map(session -> sessionViewMapper.toSummary(session, null, viewer))
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

        if (session.getStatus() == SessionStatus.CANCELLED
                || session.getStatus() == SessionStatus.COMPLETED
                || session.getGameDate() == null
                || !session.getGameDate().toInstant().isAfter(Instant.now())) {
            throw new IllegalStateException("Esta sessão já não aceita novas inscrições");
        }

        session.setAudience(SessionAudience.PUBLIC);
        return sessionViewMapper.toSummary(sessionRepository.save(session), null);
    }

    /**
     * Reverse of openToPublic: remove public discovery and anonymous preview.
     * Already-confirmed places are not deleted; normal session ACLs still govern access.
     */
    @Transactional
    public SessionSummaryResponse restrictToMembers(Long communityId, Long sessionId, String username) {
        Profile actor = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        var membership = memberRepository.findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));
        if (membership.getRole() != CommunityRole.OWNER && membership.getRole() != CommunityRole.ADMIN) {
            throw new AccessDeniedException("Só owner ou admin pode restringir a sessão");
        }
        // Lock the session while changing its audience; new joins also lock this row.
        Session session = sessionRepository.findByIdForUpdate(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada"));
        if (session.getCommunity() == null || !communityId.equals(session.getCommunity().getId())) {
            throw new IllegalArgumentException("A sessão não pertence a esta comunidade");
        }
        if (session.getStatus() == SessionStatus.CANCELLED
                || session.getStatus() == SessionStatus.COMPLETED
                || session.getGameDate() == null
                || !session.getGameDate().toInstant().isAfter(Instant.now())) {
            throw new IllegalStateException("Esta sessão já não permite alterar a visibilidade");
        }
        session.setAudience(SessionAudience.COMMUNITY_ONLY);
        session.setUpdatedAt(Instant.now());
        return sessionViewMapper.toSummary(sessionRepository.save(session), null);
    }

}
