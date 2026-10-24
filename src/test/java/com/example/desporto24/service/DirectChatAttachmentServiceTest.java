package com.example.desporto24.service;

import com.example.desporto24.Chat.ChatRepository;
import com.example.desporto24.model.Chat;
import com.example.desporto24.model.Friend;
import com.example.desporto24.service.interfaces.ChatService;
import com.example.desporto24.storage.PrivateChatFileStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DirectChatAttachmentServiceTest {
    @Mock ChatService chats;
    @Mock ChatRepository messages;
    @Mock PrivateChatFileStorage files;
    @Mock MultipartFile file;
    @InjectMocks DirectChatAttachmentService service;

    @Test
    void nonFriendsCannotStoreAttachments() {
        when(chats.getChatInfo("ana", "bea")).thenThrow(new AccessDeniedException("Not friends"));
        assertThrows(AccessDeniedException.class,
                () -> service.send("ana", "bea", file, null, null));
        verifyNoInteractions(files);
    }

    @Test
    void blockedChatsCannotUpload() {
        Friend friend = new Friend();
        friend.setChatBlocked(true);
        when(chats.getChatInfo("ana", "bea")).thenReturn(friend);
        assertThrows(AccessDeniedException.class,
                () -> service.send("ana", "bea", file, null, null));
        verifyNoInteractions(files);
    }

    @Test
    void sendsAuthorizedMessageWithServerOwnedAttachmentMetadata() {
        when(chats.getChatInfo("ana", "bea")).thenReturn(new Friend());
        when(files.store(file)).thenReturn(new PrivateChatFileStorage.StoredFile(
                "private-chat/11111111-1111-1111-1111-111111111111.pdf",
                "jogo.pdf", "application/pdf", 180L));
        when(chats.sendMessage(any(Chat.class))).thenAnswer(call -> call.getArgument(0));
        Chat result = service.send("ana", "bea", file, "", 7L);
        assertEquals("ana", result.getUsername1());
        assertEquals("bea", result.getUsername2());
        assertEquals("📎 jogo.pdf", result.getText());
        assertEquals(7L, result.getReplyToMessageId());
        assertEquals("application/pdf", result.getAttachmentMimeType());
        assertEquals(180L, result.getAttachmentSize());
        verify(files, never()).delete(any());
    }

    @Test
    void failedMessagePersistenceRemovesOrphanedFile() {
        when(chats.getChatInfo("ana", "bea")).thenReturn(new Friend());
        when(files.store(file)).thenReturn(new PrivateChatFileStorage.StoredFile(
                "private-chat/22222222-2222-2222-2222-222222222222.gif",
                "golo.gif", "image/gif", 100L));
        when(chats.sendMessage(any(Chat.class))).thenThrow(new IllegalStateException("DB unavailable"));
        assertThrows(IllegalStateException.class,
                () -> service.send("ana", "bea", file, "Golo!", null));
        verify(files).delete("private-chat/22222222-2222-2222-2222-222222222222.gif");
    }

    @Test
    void cannotReadFilesFromAnyOtherConversationEvenIfMessageIdIsKnown() {
        when(chats.getChatInfo("ana", "bea")).thenReturn(new Friend());
        Chat otherConversation = new Chat();
        otherConversation.setUsername1("ana");
        otherConversation.setUsername2("carlos");
        otherConversation.setAttachmentKey("private-chat/33333333-3333-3333-3333-333333333333.pdf");
        when(messages.findById(3L)).thenReturn(Optional.of(otherConversation));
        assertThrows(ResponseStatusException.class,
                () -> service.download("ana", "bea", 3L));
        verify(files, never()).read(any());
    }

    @Test
    void authorizedFriendCanDownloadMessageFromExactConversation() {
        when(chats.getChatInfo("bea", "ana")).thenReturn(new Friend());
        Chat chat = new Chat();
        chat.setUsername1("ana");
        chat.setUsername2("bea");
        chat.setAttachmentKey("private-chat/44444444-4444-4444-4444-444444444444.pdf");
        chat.setAttachmentMimeType("application/pdf");
        chat.setAttachmentName("jogo.pdf");
        when(messages.findById(4L)).thenReturn(Optional.of(chat));
        when(files.read(chat.getAttachmentKey())).thenReturn(new byte[]{1,2,3});
        var download = service.download("bea", "ana", 4L);
        assertArrayEquals(new byte[]{1,2,3}, download.content());
        assertEquals("application/pdf", download.mimeType());
        assertEquals("jogo.pdf", download.filename());
    }
}
