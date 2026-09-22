package org.example.project302.notification.entity;

import jakarta.persistence.*;
import lombok.Cleanup;
import lombok.Data;
import org.example.project302.user.entity.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "NOTIFICATIONS")
public class Notification {
    @Id
    private Long ntcnId;    // 알림 번호

    @CreationTimestamp
    private LocalDateTime creationDate; // 생성일자

    @Column
    private boolean readYn; // 읽기여부

    @Enumerated(EnumType.STRING)
    private NotificationType ntcnType;  // 알림 종류

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
