package org.example.project302.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.chat.dto.AddMessageRequest;
import org.example.project302.user.entity.User;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "chat_files")
@AllArgsConstructor
@NoArgsConstructor
public class ChatFile {

    @Id
    private String chatFileId;

    private String chatFilePath;

    @CreationTimestamp
    private LocalDateTime creatDate;

    private Integer readNot;

    // 회원번호 외래키
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chatroom chatroom;

    public static ChatFile createChatFile(String chatFileId, User user, Chatroom chatroom, String bucket) {

        return new ChatFile(
                chatFileId,
                bucket,
                null,
                null,
                user,
                chatroom
        );
    }
}
