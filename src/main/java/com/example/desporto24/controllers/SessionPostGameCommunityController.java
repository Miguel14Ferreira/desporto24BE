package com.example.desporto24.controllers;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.CommunityInviteStatus;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityJoinRequestRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.session.SessionCommunityShareRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.service.CommunityManagementService;
import com.example.desporto24.service.interfaces.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A voluntary, post-game invitation. Unlike a session RSVP, this NEVER creates
 * membership automatically. The user must attend and explicitly tap to join.
 * All access checks are server-side; a query parameter cannot assert attendance.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions/{sessionId}/post-game-communities")
public class SessionPostGameCommunityController {
    private final SessionRepository sessions;
    private final SessionParticipantRepository participants;
    private final SessionCommunityShareRepository shares;
    private final ProfileRepository profiles;
    private final CommunityMemberRepository members;
    private final CommunityJoinRequestRepository joinRequests;
    private final CommunityManagementService communityService;
    private final SessionService sessionService;

    public record Suggestion(Long id, String name, boolean requiresApproval) {}
    public record JoinResult(Long communityId, String state) {}

    private record Eligible(Session session, Long profileId, String username) {}

    private Eligible attendee(Long sessionId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        String username = authentication.getName();
        var user = profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        // Use the normal session details authorization as well as the attendance proof.
        sessionService.getById(sessionId, username);
        var session = sessions.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (session.getStatus() != SessionStatus.COMPLETED
                || session.getGameDate() == null
                || !session.getGameDate().toInstant().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "O jogo ainda não terminou");
        }
        var person = participants.findBySessionIdAndProfileIdAndState(
                sessionId, user.getId(), ParticipantState.ACTIVE)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Só quem participou pode aderir através desta sessão"));
        if (person.getAttendanceStatus() != AttendanceStatus.ATTENDED
                || person.getAttendanceRecordedAt() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "A presença tem de ser confirmada pelo organizador");
        }
        return new Eligible(session, user.getId(), username);
    }

    private LinkedHashMap<Long, Community> communities(Session session) {
        var linked = new LinkedHashMap<Long, Community>();
        if (session.getCommunity() != null) {
            linked.put(session.getCommunity().getId(), session.getCommunity());
        }
        shares.findBySession_IdOrderByIdAsc(session.getId()).forEach(share -> {
            if (share.getCommunity() != null) {
                linked.putIfAbsent(share.getCommunity().getId(), share.getCommunity());
            }
        });
        return linked;
    }

    private boolean eligibleCommunity(Long communityId, Long profileId) {
        return !members.existsByCommunityIdAndProfileId(communityId, profileId)
                && joinRequests.findByCommunityIdAndRequesterIdAndStatus(
                        communityId, profileId, CommunityInviteStatus.PENDING).isEmpty()
                && joinRequests.findByCommunityIdAndRequesterIdAndStatus(
                        communityId, profileId, CommunityInviteStatus.REJECTED).isEmpty();
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<Suggestion> suggestions(@PathVariable Long sessionId, Authentication authentication) {
        Eligible current = attendee(sessionId, authentication);
        return communities(current.session()).values().stream()
                .filter(group -> eligibleCommunity(group.getId(), current.profileId()))
                .map(group -> new Suggestion(group.getId(), group.getName(),
                        group.getVisibility() == CommunityVisibility.PRIVATE))
                .toList();
    }

    @PostMapping("/{communityId}/join")
    public JoinResult join(@PathVariable Long sessionId, @PathVariable Long communityId,
                           Authentication authentication) {
        Eligible current = attendee(sessionId, authentication);
        Community group = communities(current.session()).get(communityId);
        if (group == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comunidade não associada ao jogo");
        }
        if (!eligibleCommunity(communityId, current.profileId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já és membro ou tens um pedido de adesão");
        }
        if (group.getVisibility() == CommunityVisibility.PRIVATE) {
            communityService.requestJoin(communityId, current.username());
            return new JoinResult(communityId, "REQUESTED");
        }
        communityService.joinPublic(communityId, current.username());
        return new JoinResult(communityId, "JOINED");
    }
}
