package org.example.project302.chat.repository;

import org.example.project302.chat.entity.ChatParticipation;
import org.example.project302.chat.entity.ChatParticipationId;
import org.example.project302.chat.entity.Chatroom;
import org.example.project302.product.entity.Product;
import org.example.project302.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatParticipationRepository extends JpaRepository<ChatParticipation, ChatParticipationId> {

    List<ChatParticipation> findAllByUser_UserId(Long userId);

    List<ChatParticipation> findByUser(User user);


    List<ChatParticipation> findAllByChatroom(Chatroom chatroom);

    List<ChatParticipation> findAllByChatroom_ChatId(Long chatId);

    void removeByChatroom_ChatIdAndUser_UserId(Long chatId, Long userId);

    ChatParticipation findByChatroomChatIdAndUserUserId(Long chatId, Long userId);

    Integer countAllByChatroom(Chatroom chatroom);

    List<ChatParticipation> findByUserAndChatroom_Product(User user, Product product);

    Optional<ChatParticipation> findByUserAndChatroomIsIn(User user, List<Chatroom> chatrooms);

}
