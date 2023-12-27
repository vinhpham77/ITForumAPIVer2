package com.caykhe.itforum.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "username", nullable = false)
    private String username;  // Sửa lại thành username

    @NotNull
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "is_read", nullable = false)
    private boolean isRead;
    public boolean isRead() {
        return isRead;
    }
    @Size(max = 10)
    @NotNull
    @Column(name = "type", nullable = false, length = 10)
    private String type;
    
    @Lob
    @NotNull
    @Column(name = "target_id")
    private Integer targetId;
    public void setRead(boolean read) {
        isRead = read;
    }
}
