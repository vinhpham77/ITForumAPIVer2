package com.caykhe.itforum.services;

import com.caykhe.itforum.models.Notification;
import com.caykhe.itforum.models.User;
import com.caykhe.itforum.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }


    public List<Notification> getAllNotifications(String username) {
        return notificationRepository.findByUsernameOrderByCreatedAtDesc(username);
    }
}