package org.example.project302.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.chat.dto.AddMessageRequest;
import org.example.project302.user.entity.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="messages")
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long msgId;

    @CreationTimestamp
    private LocalDateTime creatDate;

    @Column(length = 3000)
    private String content;

    private Integer readNot;

    // 회원번호 외래키
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chatroom chatroom;

    public static Message createMessage(AddMessageRequest messageRequest, User user, Chatroom chatroom) {
        return new Message(
                null,
                null,
                messageRequest.getContent(),
                null,
                user,
                chatroom
        );
    }
}
