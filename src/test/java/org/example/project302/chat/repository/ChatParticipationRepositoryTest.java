package org.example.project302.chat.repository;

import org.example.project302.chat.entity.ChatParticipation;
import org.example.project302.chat.entity.ChatParticipationId;
import org.example.project302.chat.entity.Chatroom;
import org.example.project302.user.entity.User;
import org.example.project302.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ChatParticipationRepositoryTest {

    @Autowired
    ChatParticipationRepository chatPartRepository;
    @Autowired ChatroomRepository chatroomRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("조회되냐")
    void findById() {
        Chatroom chat = chatroomRepository.findById(3L).orElse(null);
        User user = userRepository.findById(2L).orElse(null);
        ChatParticipationId chatpartId = new ChatParticipationId(chat, user);
        ChatParticipation target = chatPartRepository.findById(chatpartId).orElse(null);

        assertEquals(target.getChatroom().getChatId(), 3L);
    }

}