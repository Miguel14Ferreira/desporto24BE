package com.example.desporto24.controllers;
import com.example.desporto24.service.PrivateGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
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
