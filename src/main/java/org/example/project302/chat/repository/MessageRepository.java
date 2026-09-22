package org.example.project302.chat.repository;

import org.example.project302.chat.dto.GetMessageResponse;
import org.example.project302.chat.dto.MessageAndFile;
import org.example.project302.chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // 가장 최근에 대화한 100개의 내용만 조회
    // 추가 조회하는 쿼리문 필요
//    @Query(value = "SELECT chat_id, user_id, content, null as chat_file_id, creat_date, read_not FROM messages where chat_id=:chatId " +
//            "UNION ALL " +
//            "SELECT chat_id, user_id, null as content, chat_file_id, creat_date, read_not FROM chat_files where chat_id=:chatId " +
//            "order by creat_date desc limit 100", nativeQuery = true)
//    public List<MessageAndFile> findMsgsFilesByChatId(Long chatId);

    // 참가 날짜 이후
    @Query(value = "SELECT *\n" +
            "from combined_messages\n" +
            "where chat_id = :chatId\n" +
            "  and creat_date > (select joined_date\n" +
            "                    from chat_participations\n" +
            "                    where chat_id = :chatId\n" +
            "                      and user_id = :userId)\n" +
            "    limit 300;", nativeQuery = true)
    public List<MessageAndFile> findMsgsFilesByChatId(Long chatId, Long userId);


}
