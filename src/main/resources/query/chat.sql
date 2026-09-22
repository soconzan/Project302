-- 채팅방 목록 조회
select c.chat_id, m.content, m.creat_date, p.pd_name, cp.headcount, f.file_name
from chatrooms c
         inner join products p on c.pd_id = p.pd_id
         inner join (select chat_id, content, creat_date
                     from messages
                     where (chat_id, msg_id) in (select chat_id, max(msg_id)
                                                 from messages
                                                 group by chat_id)) m on c.chat_id = m.chat_id
         inner join (select chat_id, count(*) as headcount
                     from chat_participations
                     group by chat_id) cp on c.chat_id = cp.chat_id
         left join (select pd_id, file_name
                    from files
                    where thumbnail_yn = 1) f on c.pd_id = f.pd_id
where c.chat_id in (select chat_id
                    from chat_participations
                    where user_id = :userId)
order by m.creat_date desc;

-- 채팅방 목록 조회 + 파일 메세지 같이 조회
select c.chat_id, m.content, m.creat_date, p.pd_name, cp.headcount, f.file_id
from chatrooms c
         inner join products p on c.pd_id = p.pd_id
         inner join (select chat_id, user_id, content, creat_date
                     from (select chat_id, user_id, content, creat_date
                           from messages
                           union all
                           select chat_id, user_id, '(사진)' as content, creat_date
                           from chat_files
                           order by creat_date desc) as msgs
                     group by chat_id) m on c.chat_id = m.chat_id
         inner join (select chat_id, count(*) as headcount
                     from chat_participations
                     group by chat_id) cp on c.chat_id = cp.chat_id
         left join (select pd_id, file_id
                    from files
                    where thumbnail_yn = 1) f on c.pd_id = f.pd_id
where c.chat_id in (select chat_id
                    from chat_participations
                    where user_id = :userId)
order by m.creat_date desc;

-- 채팅 목록 조회 v.24.06.07
select c.chat_id, m.content, m.creat_date, p.pd_id, cp.headcount
from chatrooms c
         inner join products p on c.pd_id = p.pd_id
         inner join (select chat_id, user_id, content, creat_date
                     from (select chat_id, user_id, content, creat_date
                           from messages
                           union all
                           select chat_id, user_id, '(사진)' as content, creat_date
                           from chat_files
                           order by creat_date desc) as msgs
                     group by chat_id) m on c.chat_id = m.chat_id
         inner join (select chat_id, count(*) as headcount
                     from chat_participations
                     group by chat_id) cp on c.chat_id = cp.chat_id
where c.chat_id in (select chat_id
                    from chat_participations
                    where user_id = :userId)
order by m.creat_date desc;

-- 채팅 목록.. 암튼 조회
select *
from chat_participations
where chat_id in (select chat_id
                  from chatrooms
                  where pd_id = :pdId)
  and user_id = :userId;


-- 대화 내역 조회 = 메시지 + 파일
SELECT user_id, content, null as chat_file_id, creat_date, read_not
FROM messages
where chat_id = 1
UNION ALL
SELECT user_id, null as content, chat_file_id, creat_date, read_not
FROM chat_files
where chat_id = 1
order by creat_date desc limit 100;

--- 대화 내역 조회 = 메시지 + 파일 채팅방 입성 날짜 이후
SELECT *
from combined_messages
where chat_id = :chatId
  and creat_date > (select joined_date
                    from chat_participations
                    where chat_id = :chatId
                      and user_id = :userId)
    limit 300;