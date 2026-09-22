package org.example.project302.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "chat_notices")
@AllArgsConstructor
@NoArgsConstructor
public class ChatNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatNoticeId;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chatroom chatroom;

    @CreationTimestamp
    private LocalDateTime creatDate;

    private String content;
}
