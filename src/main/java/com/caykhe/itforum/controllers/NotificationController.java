package com.caykhe.itforum.controllers;

import com.caykhe.itforum.models.Notification;
import com.caykhe.itforum.models.User;
import com.caykhe.itforum.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveNotification(@RequestBody Notification notification) {
        if (notification != null) {
            notificationService.saveNotification(notification);
            return ResponseEntity.ok("Notification saved successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid notification data");
        }
    }
    @GetMapping("/get")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        // Lấy thông tin User từ username
        List<Notification> allNotifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(allNotifications);
    }
}