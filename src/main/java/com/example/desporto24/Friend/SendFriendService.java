package com.example.desporto24.Friend;

import com.example.desporto24.exception.domain.RequestFriendException;
import com.example.desporto24.service.impl.ProjectServiceImpl;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SendFriendService {

    private final ProjectServiceImpl perfilService;

    public com.example.desporto24.model.SendFriendRequest sendFriendRequest(SendFriendRequest friendRequest) throws RequestFriendException, MessagingException {
        com.example.desporto24.model.SendFriendRequest f = perfilService.sendFriendRequest(new com.example.desporto24.model.SendFriendRequest(
                friendRequest.getUsername1(),
                friendRequest.getUsername2()));
        return f;
    }
}
