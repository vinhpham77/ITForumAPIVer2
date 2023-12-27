package com.caykhe.itforum.repositories;

import com.caykhe.itforum.models.Notification;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUsernameOrderByCreatedAtDesc(@NotNull String username);
}
