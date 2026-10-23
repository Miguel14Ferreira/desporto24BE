package com.example.desporto24.websocket;

import com.example.desporto24.model.community.Community;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Resolves recipients from committed membership data, not the membership
 * snapshot captured while the message was being saved.
 */
@Service
@RequiredArgsConstructor
public class CommunityChatRecipientResolver {
    private final CommunityRepository communityRepository;
    private final CommunityMemberRepository memberRepository;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Set<String> currentRecipients(Long communityId) {
        Community community = communityRepository.findById(communityId).orElse(null);
        if (community == null) {
            return Set.of();
        }

        Set<String> recipients = new LinkedHashSet<>();
        if (community.getOwner() != null) {
            recipients.add(community.getOwner().getUsername());
        }
        memberRepository.findByCommunityIdOrderByJoinedAtAsc(communityId)
                .forEach(member -> recipients.add(member.getProfile().getUsername()));
        return recipients;
    }
}
