package org.example.project302.notification.service;

import lombok.RequiredArgsConstructor;
import org.example.project302.notification.repository.NotificationRepository;
import org.example.project302.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveNotification(String token) {

    }
}
