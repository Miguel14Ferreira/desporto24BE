package com.example.desporto24.registo.FriendRequest;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;
    public void saveFriendRequest(FriendRequest token) {
        friendRequestRepository.save(token);
    }
    public Optional<FriendRequest> getToken(String token){
        return friendRequestRepository.findByToken(token);
        }
    public int setConfirmedAt(String token){
        return friendRequestRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
