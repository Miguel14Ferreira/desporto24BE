package com.example.desporto24.controllers;
import com.example.desporto24.service.PrivateGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping("/api/private-groups")
public class PrivateGroupController {
    private final PrivateGroupService service;
    @GetMapping public List<PrivateGroupService.GroupView> list(Authentication auth){
        return service.list(auth.getName());
    }
    @PostMapping public PrivateGroupService.GroupView create(
            @RequestBody PrivateGroupService.CreateRequest input,Authentication auth){
        return service.create(auth.getName(),input);
    }
    @GetMapping("/{id}") public PrivateGroupService.GroupView get(@PathVariable Long id,Authentication auth){
        return service.get(auth.getName(),id);
    }
    @PostMapping("/{id}/members") public PrivateGroupService.GroupView add(
            @PathVariable Long id,@RequestBody PrivateGroupService.AddRequest input,Authentication auth){
        return service.add(auth.getName(),id,input);
    }
    @PatchMapping("/{id}") public PrivateGroupService.GroupView rename(
            @PathVariable Long id,@RequestBody PrivateGroupService.RenameRequest input,Authentication auth){
        return service.rename(auth.getName(),id,input);
    }
    @PostMapping(value="/{id}/photo",consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public PrivateGroupService.GroupView updatePhoto(
            @PathVariable Long id,@RequestPart("photo") MultipartFile photo,Authentication auth){
        return service.updatePhoto(auth.getName(),id,photo);
    }
    @DeleteMapping("/{id}/members/{username}") public PrivateGroupService.GroupView removeMember(
            @PathVariable Long id,@PathVariable String username,Authentication auth){
        return service.removeMember(auth.getName(),id,username);
    }
    @GetMapping("/{id}/messages")
    public List<PrivateGroupService.MessageView> history(
            @PathVariable Long id,@RequestParam(required=false) Long beforeId,Authentication auth){
        return service.history(auth.getName(),id,beforeId);
    }
    @PostMapping("/{id}/messages") public PrivateGroupService.MessageView send(
            @PathVariable Long id,@RequestBody PrivateGroupService.SendRequest input,Authentication auth){
        return service.send(auth.getName(),id,input);
    }
}
