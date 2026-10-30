package com.example.desporto24.websocket;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityChatRecipientResolverTest {
    @Mock CommunityRepository communities;
    @Mock CommunityMemberRepository members;
    @InjectMocks CommunityChatRecipientResolver resolver;

    private static Profile profile(Long id, String name) {
        Profile result = new Profile();
        result.setId(id);
        result.setUsername(name);
        return result;
    }

    @Test
    void resolvesOwnerAndOnlyCurrentlyPersistedMembers() {
        Profile owner = profile(1L, "owner");
        Profile active = profile(2L, "active");
        when(communities.findById(7L))
                .thenReturn(Optional.of(Community.builder().id(7L).owner(owner).build()));
        when(members.findByCommunityIdOrderByJoinedAtAsc(7L))
                .thenReturn(List.of(CommunityMember.builder().profile(active).build(),
                        CommunityMember.builder().profile(owner).build()));

        assertEquals(Set.of("owner", "active"), resolver.currentRecipients(7L));
        verify(members).findByCommunityIdOrderByJoinedAtAsc(7L);
    }

    @Test
    void missingCommunityYieldsNoRecipientsAndNoMembershipScan() {
        when(communities.findById(9L)).thenReturn(Optional.empty());

        assertTrue(resolver.currentRecipients(9L).isEmpty());
        verifyNoInteractions(members);
    }

    @Test
    void usesAFreshTransactionRatherThanOriginalPostingTransaction() throws Exception {
        Transactional tx = CommunityChatRecipientResolver.class
                .getMethod("currentRecipients", Long.class).getAnnotation(Transactional.class);
        assertNotNull(tx);
        assertTrue(tx.readOnly());
        assertEquals(Propagation.REQUIRES_NEW, tx.propagation());
    }
}
