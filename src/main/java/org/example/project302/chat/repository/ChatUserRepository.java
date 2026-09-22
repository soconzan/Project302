package org.example.project302.chat.repository;

import org.example.project302.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChatUserRepository extends JpaRepository<User, Long> {

    @Query(value = "select * from users where user_id in (select user_id from chat_participations where chat_id = :chatId)",
            nativeQuery = true)
    public List<User> findAllByChatId(@Param("chatId") Long chatId);

    @Query(value = "select * from users where user_id in (select user_id from chat_participations where chat_id = :chatId) " +
            "and user_id != :userId", nativeQuery = true)
    public List<User> findAllByChatIdNotUserId(@Param("chatId") Long chatId, @Param("userId") Long userId);

}
