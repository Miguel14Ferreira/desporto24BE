package com.example.desporto24.service;

import com.example.desporto24.model.*;
import com.example.desporto24.repository.*;
import com.example.desporto24.service.interfaces.ChatService;
import com.example.desporto24.storage.MediaStorage;
import org.springframework.mock.web.MockMultipartFile;
import java.net.URI;
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
 @Mock MediaStorage mediaStorage;
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

 @Test void creatorCanRenameButOtherMembersCannot(){
   when(profiles.findUserByUsernameIgnoreCase("ana")).thenReturn(Optional.of(profile("ana")));
   when(profiles.findUserByUsernameIgnoreCase("bea")).thenReturn(Optional.of(profile("bea")));
   when(members.findByGroupIdAndUsernameIgnoreCase(42L,"ana")).thenReturn(Optional.of(member("ana")));
   when(members.findByGroupIdAndUsernameIgnoreCase(42L,"bea")).thenReturn(Optional.of(member("bea")));
   var saved=group();
   when(groups.findById(42L)).thenReturn(Optional.of(saved));
   assertThrows(AccessDeniedException.class,()->service.rename("bea",42L,
       new PrivateGroupService.RenameRequest("Novo nome")));
   assertEquals("Novo nome",service.rename("ana",42L,
       new PrivateGroupService.RenameRequest(" Novo nome ")).title());
   verify(groups).save(saved);
 }
 @Test void normalMemberCannotRemoveSomeoneElse(){
   when(profiles.findUserByUsernameIgnoreCase("bea")).thenReturn(Optional.of(profile("bea")));
   when(members.findByGroupIdAndUsernameIgnoreCase(42L,"bea")).thenReturn(Optional.of(member("bea")));
   when(members.findByGroupIdAndUsernameIgnoreCase(42L,"carla")).thenReturn(Optional.of(member("carla")));
   when(groups.findById(42L)).thenReturn(Optional.of(group()));
   assertThrows(AccessDeniedException.class,()->service.removeMember("bea",42L,"carla"));
   verify(members,never()).delete(any());
 }
 @Test void creatorRemovalRevokesMembershipAndNotifiesRemovedUser(){
   when(profiles.findUserByUsernameIgnoreCase("ana")).thenReturn(Optional.of(profile("ana")));
   when(members.findByGroupIdAndUsernameIgnoreCase(42L,"ana")).thenReturn(Optional.of(member("ana")));
   when(members.findByGroupIdAndUsernameIgnoreCase(42L,"bea")).thenReturn(Optional.of(member("bea")));
   when(groups.findById(42L)).thenReturn(Optional.of(group()));
   when(members.findByGroupIdOrderByJoinedAtAsc(42L)).thenReturn(List.of(member("ana")));
   var result=service.removeMember("ana",42L,"bea");
   assertEquals(1,result.members().size());
   verify(members).delete(any(PrivateGroupMember.class));
   verify(members).flush();
   ArgumentCaptor<PrivateGroupService.GroupEvent> event=ArgumentCaptor.forClass(PrivateGroupService.GroupEvent.class);
   verify(publisher).publishEvent(event.capture());
   assertTrue(event.getValue().usernames().containsAll(Set.of("ana","bea")));
   assertNull(event.getValue().message());
 }

 @Test void creatorCanUpdateGroupPhotoUsingValidatedSharedMediaStorage(){
   var ana=profile("ana");
   when(profiles.findUserByUsernameIgnoreCase("ana")).thenReturn(Optional.of(ana));
   when(members.findByGroupIdAndUsernameIgnoreCase(42L,"ana"))
     .thenReturn(Optional.of(member("ana")));
   var g=group();
   when(groups.findById(42L)).thenReturn(Optional.of(g));
   when(members.findByGroupIdOrderByJoinedAtAsc(42L)).thenReturn(List.of(member("ana")));
   when(mediaStorage.storeCommunityPhoto(anyString(),any()))
     .thenReturn(URI.create("https://cdn.example.test/groups/avatar.png"));
   var photo=new MockMultipartFile("photo","avatar.png","image/png",new byte[]{1,2,3});
   var updated=service.updatePhoto("ana",42L,photo);
   assertEquals("https://cdn.example.test/groups/avatar.png",updated.photoUrl());
   verify(mediaStorage).storeCommunityPhoto(startsWith("private-groups/42/"),same(photo));
   verify(groups).save(g);
   verify(publisher).publishEvent(any(PrivateGroupService.GroupEvent.class));
 }
 @Test void ordinaryMemberCannotChangeGroupPhoto(){
   when(profiles.findUserByUsernameIgnoreCase("bea"))
     .thenReturn(Optional.of(profile("bea")));
   when(members.findByGroupIdAndUsernameIgnoreCase(42L,"bea"))
     .thenReturn(Optional.of(member("bea")));
   when(groups.findById(42L)).thenReturn(Optional.of(group()));
   var photo=new MockMultipartFile("photo","image.png","image/png",new byte[]{1});
   assertThrows(AccessDeniedException.class,()->service.updatePhoto("bea",42L,photo));
   verifyNoInteractions(mediaStorage);
 }
 @Test void emptyOrOversizedPhotoIsRejectedBeforeMediaStorage(){
   when(profiles.findUserByUsernameIgnoreCase("ana"))
     .thenReturn(Optional.of(profile("ana")));
   when(members.findByGroupIdAndUsernameIgnoreCase(42L,"ana"))
     .thenReturn(Optional.of(member("ana")));
   when(groups.findById(42L)).thenReturn(Optional.of(group()));
   var empty=new MockMultipartFile("photo","x.png","image/png",new byte[0]);
   assertThrows(org.springframework.web.server.ResponseStatusException.class,
       ()->service.updatePhoto("ana",42L,empty));
   var oversized=new MockMultipartFile("photo","x.png","image/png",new byte[5*1024*1024+1]);
   assertThrows(org.springframework.web.server.ResponseStatusException.class,
       ()->service.updatePhoto("ana",42L,oversized));
   verifyNoInteractions(mediaStorage);
 }
 @Test void invalidImageTypeReturnsBadRequest(){
   when(profiles.findUserByUsernameIgnoreCase("ana"))
     .thenReturn(Optional.of(profile("ana")));
   when(members.findByGroupIdAndUsernameIgnoreCase(42L,"ana"))
     .thenReturn(Optional.of(member("ana")));
   when(groups.findById(42L)).thenReturn(Optional.of(group()));
   var photo=new MockMultipartFile("photo","x.svg","image/svg+xml",new byte[]{1});
   when(mediaStorage.storeCommunityPhoto(anyString(),same(photo)))
      .thenThrow(new IllegalArgumentException("Invalid image"));
   var failure=assertThrows(org.springframework.web.server.ResponseStatusException.class,
       ()->service.updatePhoto("ana",42L,photo));
   assertEquals(org.springframework.http.HttpStatus.BAD_REQUEST,failure.getStatusCode());
 }
 @Test void creatorCannotBeRemovedEvenByThemselves(){
   when(profiles.findUserByUsernameIgnoreCase("ana")).thenReturn(Optional.of(profile("ana")));
   when(members.findByGroupIdAndUsernameIgnoreCase(42L,"ana")).thenReturn(Optional.of(member("ana")));
   when(groups.findById(42L)).thenReturn(Optional.of(group()));
   assertThrows(org.springframework.web.server.ResponseStatusException.class,
       ()->service.removeMember("ana",42L,"ana"));
   verify(members,never()).delete(any());
 }
 @Test void removedMemberCannotReadOrSendAfterRevocation(){
   when(profiles.findUserByUsernameIgnoreCase("bea")).thenReturn(Optional.of(profile("bea")));
   assertThrows(AccessDeniedException.class,()->service.history("bea",42L,null));
   assertThrows(AccessDeniedException.class,
       ()->service.send("bea",42L,new PrivateGroupService.SendRequest("Ainda aqui?")));
   verifyNoInteractions(messages);
 }
 @Test void sendingGroupMessageRequiresMembership(){
   when(profiles.findUserByUsernameIgnoreCase("eve")).thenReturn(Optional.of(profile("eve")));
   assertThrows(AccessDeniedException.class,
     ()->service.send("eve",42L,new PrivateGroupService.SendRequest("Olá")));
   verifyNoInteractions(messages,publisher);
 }
}
