package com.example.desporto24.register.FriendRequest;

import com.example.desporto24.repository.FriendRequestRepository;
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
    public void setConfirmedAt(String token){
        friendRequestRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }

    public void deleteFriendRequest(FriendRequest friendRequest) {
        friendRequestRepository.delete(friendRequest);
    }

    public boolean hasPendingRequestBetween(String firstUsername, String secondUsername) {
        return friendRequestRepository
                .existsPendingBetween(firstUsername, secondUsername);
    }
}
