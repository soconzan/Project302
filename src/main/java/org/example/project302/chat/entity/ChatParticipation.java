package org.example.project302.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.user.entity.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="chat_participations")
@IdClass(ChatParticipationId.class)
@AllArgsConstructor
@NoArgsConstructor
public class ChatParticipation {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chatroom chatroom;

    @CreationTimestamp
    private LocalDateTime joinedDate;

    private LocalDateTime lastDate;
}
