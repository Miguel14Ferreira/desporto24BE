package com.example.desporto24.Friend;

import com.example.desporto24.service.impl.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SendFriendService {

    private final ProjectServiceImpl perfilService;

    public com.example.desporto24.model.SendFriendRequest sendFriendRequest(SendFriendRequest friendRequest) throws Exception {
        return perfilService.sendFriendRequest(new com.example.desporto24.model.SendFriendRequest(
                friendRequest.getUsername1(),
                friendRequest.getUsername2()));
    }
}
