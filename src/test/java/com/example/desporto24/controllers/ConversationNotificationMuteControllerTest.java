package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.chat.InboxRequest;
import com.example.desporto24.model.dto.chat.InboxSummaryResponse;
import com.example.desporto24.service.ConversationNotificationMuteService;
import com.example.desporto24.service.InboxSummaryService;
import com.example.desporto24.service.PrivateGroupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversationNotificationMuteControllerTest {
    @Mock com.example.desporto24.service.interfaces.NotificationService notifications;
    @Mock com.example.desporto24.service.NotificationPreferencesService preferences;
    @Mock ConversationNotificationMuteService mutes;
    @Mock InboxSummaryService inboxSummaryService;
    @Mock PrivateGroupService privateGroupService;
    @Mock Authentication auth;
    @InjectMocks NotificationController controller;

    @Test void directAndCommunityMutingRequiresAuthenticatedInboxAccess() {
        when(auth.getName()).thenReturn("bea");
        var request = new ConversationNotificationMuteService.ChangeMute("direct", "ana", true);
        when(inboxSummaryService.summaries(eq("bea"), any(InboxRequest.class)))
            .thenReturn(List.of());
        assertThrows(AccessDeniedException.class, () -> controller.updateConversationMute(auth, request));
        verifyNoInteractions(mutes);
    }

    @Test void privateGroupMutingRechecksActiveMembership() {
        when(auth.getName()).thenReturn("bea");
        var request = new ConversationNotificationMuteService.ChangeMute("private", "7", true);
        controller.updateConversationMute(auth, request);
        verify(privateGroupService).get("bea", 7L);
        verify(mutes).update("bea", "private", "7", true);
        verifyNoInteractions(inboxSummaryService);
    }

    @Test void muteListAlwaysUsesAuthenticatedAccount() {
        when(auth.getName()).thenReturn("bea");
        controller.myConversationMutes(auth);
        verify(mutes).list("bea");
    }
}
