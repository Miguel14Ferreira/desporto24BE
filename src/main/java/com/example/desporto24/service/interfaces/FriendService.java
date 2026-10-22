package com.example.desporto24.service.interfaces;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.SendFriendRequest;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.friend.FriendSummaryResponse;
import com.example.desporto24.model.dto.friend.FriendshipStatusResponse;

import java.util.List;

public interface FriendService {
    void sendFriendRequest(SendFriendRequest friendRequest);
    void blockFriend(String username1, String username2);
    void unblockFriend(String username1, String username2);
    void removeFriend(String username1, String username2);
    List<Profile> getFriends(String username);
    List<FriendSummaryResponse> getFriendSummaries(String username);
    FriendshipStatusResponse getFriendshipStatus(String requester, String recipient);
}