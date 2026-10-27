package com.example.desporto24.service;

import com.example.desporto24.model.*;
import com.example.desporto24.repository.*;
import com.example.desporto24.service.interfaces.ChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrivateGroupServiceTest {
 @Mock PrivateGroupRepository groups;
 @Mock PrivateGroupMemberRepository members;
 @Mock PrivateGroupMessageRepository messages;
 @Mock ProfileRepository profiles;
 @Mock ChatService chats;
 @Mock ApplicationEventPublisher publisher;
 @InjectMocks PrivateGroupService service;

 private Profile profile(String user){
   Profile p=new Profile();p.setId(user.equals("ana")?1L:2L);
   p.setUsername(user);p.setEnabled(true);p.setNotLocked(true);return p;
 }
 private PrivateGroup group(){
   PrivateGroup g=new PrivateGroup();g.setId(42L);g.setTitle("Futebol");
   g.setCreatorUsername("ana");g.setCreatedAt(Instant.parse("2026-10-27T10:00:00Z"));return g;
 }
 private PrivateGroupMember member(String u){
   PrivateGroupMember m=new PrivateGroupMember();m.setGroupId(42L);
   m.setUsername(u);m.setJoinedAt(Instant.parse("2026-10-27T10:10:00Z"));return m;
 }
 @Test void outsiderCannotReadPrivateGroupMessages(){
   when(profiles.findUserByUsernameIgnoreCase("eve")).thenReturn(Optional.of(profile("eve")));
   assertThrows(AccessDeniedException.class,()->service.history("eve",42L,null));
   verifyNoInteractions(messages);
 }
 @Test void newlyInvitedMemberOnlyReadsMessagesCreatedAfterJoining(){
   when(profiles.findUserByUsernameIgnoreCase("bea")).thenReturn(Optional.of(profile("bea")));
   when(members.findByGroupIdAndUsernameIgnoreCase(42L,"bea"))
     .thenReturn(Optional.of(member("bea")));
   when(messages.findByGroupIdAndCreatedAtGreaterThanEqualOrderByIdDesc(
      eq(42L),eq(Instant.parse("2026-10-27T10:10:00Z")),any(Pageable.class)))
     .thenReturn(List.of());
   assertEquals(List.of(),service.history("bea",42L,null));
   verify(messages).findByGroupIdAndCreatedAtGreaterThanEqualOrderByIdDesc(
      eq(42L),eq(Instant.parse("2026-10-27T10:10:00Z")),any(Pageable.class));
 }
 @Test void onlyCreatorCanAddMembers(){
   when(profiles.findUserByUsernameIgnoreCase("bea")).thenReturn(Optional.of(profile("bea")));
   when(members.findByGroupIdAndUsernameIgnoreCase(42L,"bea"))
     .thenReturn(Optional.of(member("bea")));
   when(groups.findById(42L)).thenReturn(Optional.of(group()));
   assertThrows(AccessDeniedException.class,
     ()->service.add("bea",42L,new PrivateGroupService.AddRequest(List.of("carla"))));
   verifyNoInteractions(chats);
 }
 @Test void creatingGroupDoesNotReadOrCopyDirectMessageHistory(){
   var ana=profile("ana");var bea=profile("bea");
   when(profiles.findUserByUsernameIgnoreCase("ana")).thenReturn(Optional.of(ana));
   when(profiles.findUserByUsernameIgnoreCase("bea")).thenReturn(Optional.of(bea));
   when(groups.saveAndFlush(any())).thenAnswer(a->{PrivateGroup g=a.getArgument(0);g.setId(42L);return g;});
   when(members.findByGroupIdOrderByJoinedAtAsc(42L)).thenReturn(List.of(member("ana"),member("bea")));
   var created=service.create("ana",new PrivateGroupService.CreateRequest("Futebol",List.of("bea")));
   assertEquals(2,created.members().size());
   verify(chats).getChatInfo("ana","bea");
   verifyNoInteractions(messages);
   verify(publisher).publishEvent(any(PrivateGroupService.GroupEvent.class));
 }
 @Test void sendingGroupMessageRequiresMembership(){
   when(profiles.findUserByUsernameIgnoreCase("eve")).thenReturn(Optional.of(profile("eve")));
   assertThrows(AccessDeniedException.class,
     ()->service.send("eve",42L,new PrivateGroupService.SendRequest("Olá")));
   verifyNoInteractions(messages,publisher);
 }
}
