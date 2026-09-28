package com.example.desporto24.service.interfaces;

import com.example.desporto24.register.Notifications.Notifications;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationService {
    List<Notifications> getNotificationsFromProfile(String username);
    void disableProfileAndSendEmail(String email, String authenticatedUsername);
    void blockOwnAccount(String username);
    void acceptFriendRequest(Long id, String username);
    void rejectFriendRequest(Long id, String username);
    void acceptFriendRequestByToken(Long id, String token);
    void save(Notifications notifications);
    void deleteNotification(Notifications notifications);
    void deleteNotification(Long id, String username);
    void deleteAllNotifications(String username);
    void markRead(Long id, String username);
}