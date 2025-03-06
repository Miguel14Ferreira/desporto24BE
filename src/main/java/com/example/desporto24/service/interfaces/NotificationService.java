package com.example.desporto24.service.interfaces;

import com.example.desporto24.register.Notifications.Notifications;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationService {
    List<Notifications> getNotificationsFromProfile(String username);
    void disableProfileAndSendEmail(String email);
    void acceptFriendRequest(Long id, String token);
    void save(Notifications notifications);
    void deleteNotification(Notifications notifications);
}