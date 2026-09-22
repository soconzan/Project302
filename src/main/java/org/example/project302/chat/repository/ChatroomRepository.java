package org.example.project302.chat.repository;

import org.example.project302.chat.dto.ChatroomWithUserId;
import org.example.project302.chat.entity.Chatroom;
import org.example.project302.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

    @Query(value = "select c.chat_id, m.content, m.creat_date, p.pd_id, cp.headcount\n" +
            "from chatrooms c\n" +
            "         inner join products p on c.pd_id = p.pd_id\n" +
            "         inner join (select chat_id, user_id, content, creat_date\n" +
            "                     from (select chat_id, user_id, content, creat_date\n" +
            "                           from messages\n" +
            "                           union all\n" +
            "                           select chat_id, user_id, '(사진)' as content, creat_date\n" +
            "                           from chat_files\n" +
            "                           order by creat_date desc) as msgs\n" +
            "                     group by chat_id) m on c.chat_id = m.chat_id\n" +
            "         inner join (select chat_id, count(*) as headcount\n" +
            "                     from chat_participations\n" +
            "                     group by chat_id) cp on c.chat_id = cp.chat_id\n" +
            "where c.chat_id in (select chat_id\n" +
            "                    from chat_participations\n" +
            "                    where user_id = :userId)\n" +
            "order by m.creat_date desc;",
            nativeQuery = true)
    List<ChatroomWithUserId> findAllByUser_UserId(@Param("userId") Long userId);

    List<Chatroom> findByChatIdIsIn(List<Long> chatIds);

    @Query(value = "select c.chat_id, m.content, m.creat_date, p.pd_name, cp.headcount, f.file_id\n" +
            "from chatrooms c\n" +
            "         inner join products p on c.pd_id = p.pd_id\n" +
            "         inner join (select chat_id, user_id, content, creat_date\n" +
            "                     from (select chat_id, user_id, content, creat_date\n" +
            "                           from messages\n" +
            "                           union all\n" +
            "                           select chat_id, user_id, '(사진)' as content, creat_date\n" +
            "                           from chat_files\n" +
            "                           order by creat_date desc) as msgs\n" +
            "                     group by chat_id) m on c.chat_id = m.chat_id\n" +
            "         inner join (select chat_id, count(*) as headcount\n" +
            "                     from chat_participations\n" +
            "                     group by chat_id) cp on c.chat_id = cp.chat_id\n" +
            "         left join (select pd_id, file_id\n" +
            "                     from files\n" +
            "                     where thumbnail_yn = 1) f on c.pd_id = f.pd_id\n" +
            "where c.chat_id in (select chat_id\n" +
            "                    from chat_participations\n" +
            "                    where user_id = :userId)\n" +
            "and p.pd_status != 'GROUP'\n" +
            "order by m.creat_date desc",
            nativeQuery = true)
    List<ChatroomWithUserId> findUsedByUser_UserId(@Param("userId") Long userId);

    @Query(value = "select c.chat_id, m.content, m.creat_date, p.pd_name, cp.headcount, f.file_id\n" +
            "from chatrooms c\n" +
            "         inner join products p on c.pd_id = p.pd_id\n" +
            "         inner join (select chat_id, user_id, content, creat_date\n" +
            "                     from (select chat_id, user_id, content, creat_date\n" +
            "                           from messages\n" +
            "                           union all\n" +
            "                           select chat_id, user_id, '(사진)' as content, creat_date\n" +
            "                           from chat_files\n" +
            "                           order by creat_date desc) as msgs\n" +
            "                     group by chat_id) m on c.chat_id = m.chat_id\n" +
            "         inner join (select chat_id, count(*) as headcount\n" +
            "                     from chat_participations\n" +
            "                     group by chat_id) cp on c.chat_id = cp.chat_id\n" +
            "         left join (select pd_id, file_id\n" +
            "                     from files\n" +
            "                     where thumbnail_yn = 1) f on c.pd_id = f.pd_id\n" +
            "where c.chat_id in (select chat_id\n" +
            "                    from chat_participations\n" +
            "                    where user_id = :userId)\n" +
            "and p.pd_status = 'GROUP'\n" +
            "order by m.creat_date desc",
            nativeQuery = true)
    List<ChatroomWithUserId> findGroupByUser_UserId(@Param("userId") Long userId);

    List<Chatroom> findAllByProduct(Product pd);

    Chatroom findByProduct(Product product);

}
