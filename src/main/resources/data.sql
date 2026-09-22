DELETE
FROM CHAT_FILES;
DELETE
FROM FILES;
DELETE
FROM REVIEWS;
DELETE
FROM KEYWORDS;
DELETE
FROM NOTIFICATIONS;
DELETE
FROM MESSAGES;
DELETE
FROM GROUP_DETAILS;
DELETE
FROM CHAT_PARTICIPATIONS;
DELETE
FROM CHATROOMS;
DELETE
FROM PARTICIPATIONS;
DELETE
FROM LIKES;
DELETE
FROM PRODUCTS;
DELETE
FROM CATEGORIES;
DELETE
FROM USERS;

ALTER TABLE CATEGORIES AUTO_INCREMENT = 0;
ALTER TABLE CHAT_FILES AUTO_INCREMENT = 0;
ALTER TABLE CHATROOMS AUTO_INCREMENT = 0;
ALTER TABLE KEYWORDS AUTO_INCREMENT = 0;
ALTER TABLE LIKES AUTO_INCREMENT = 0;
ALTER TABLE MESSAGES AUTO_INCREMENT = 0;
ALTER TABLE NOTIFICATIONS AUTO_INCREMENT = 0;
ALTER TABLE PRODUCTS AUTO_INCREMENT = 0;
ALTER TABLE REVIEWS AUTO_INCREMENT = 0;
ALTER TABLE USERS AUTO_INCREMENT = 0;

-- 카테고리
INSERT INTO CATEGORIES (ctgr_name)
VALUES ('디지털기기'),
       ('생활/주방'),
       ('생활가전'),
       ('가구/인테리어'),
       ('스포츠/헬스'),
       ('뷰티/미용'),
       ('여성의류/잡화'),
       ('남성의류/잡화'),
       ('취미/게임/음반'),
       ('도서'),
       ('식품'),
       ('OTT'),
       ('티켓/교환권'),
       ('기타'),
       ('삽니다');

-- 영진대 학생
INSERT INTO USERS (local_id, password, phone_no, nickname, birth, gender, email, email_yn, last_conect_time,
                   subscribe_date, univ_id, token, user_file_id, user_file_path)
VALUES ('test1', '$2a$10$upI6h4n0A7WiD08baqPIf./XzRXA2OchUBZhtHKFV.Y3iPqInePeG', '01012345678', '닉네임1', '19900101', 'M',
        'user1@example.com', TRUE, NOW(), NOW(), 517,
        'token1', 'uuid1.jpg', 'C:\project302\files'),
       ('test2', '$2a$10$upI6h4n0A7WiD08baqPIf./XzRXA2OchUBZhtHKFV.Y3iPqInePeG', '01023456789', '닉네임2', '19910202', 'F',
        'user2@example.com', TRUE, NOW(), NOW(), 517,
        'token2', 'uuid2.jpg', 'C:\project302\files'),
       ('test3', '$2a$10$upI6h4n0A7WiD08baqPIf./XzRXA2OchUBZhtHKFV.Y3iPqInePeG', '01034567890', '닉네임3', '19920303', 'M',
        'user3@example.com', TRUE, NOW(), NOW(), 517,
        'token3', 'uuid3.jpg', 'C:\project302\files'),
       ('test4', '$2a$10$upI6h4n0A7WiD08baqPIf./XzRXA2OchUBZhtHKFV.Y3iPqInePeG', '01045678901', '닉네임4', '19930404', 'F',
        'user4@example.com', TRUE, NOW(), NOW(), 517,
        'token4', 'uuid4.jpg', 'C:\project302\files'),
       ('test5', '$2a$10$upI6h4n0A7WiD08baqPIf./XzRXA2OchUBZhtHKFV.Y3iPqInePeG', '01056789012', '닉네임5', '19940505', 'M',
        'user5@example.com', TRUE, NOW(), NOW(), 517,
        'token5', 'uuid5.jpg', 'C:\project302\files'),
       ('test6', '$2a$10$upI6h4n0A7WiD08baqPIf./XzRXA2OchUBZhtHKFV.Y3iPqInePeG', '01067890123', '닉네임6', '19950606', 'F',
        'user6@example.com', TRUE, NOW(), NOW(), 517,
        'token6', 'uuid6.jpg', 'C:\project302\files'),
       ('test7', '$2a$10$upI6h4n0A7WiD08baqPIf./XzRXA2OchUBZhtHKFV.Y3iPqInePeG', '01078901234', '닉네임7', '19960707', 'M',
        'user7@example.com', TRUE, NOW(), NOW(), 517,
        'token7', 'uuid7.jpg', 'C:\project302\files'),
       ('test8', '$2a$10$upI6h4n0A7WiD08baqPIf./XzRXA2OchUBZhtHKFV.Y3iPqInePeG', '01089012345', '닉네임8', '19970808', 'F',
        'user8@example.com', TRUE, NOW(), NOW(), 517,
        'token8', 'uuid8.jpg', 'C:\project302\files'),
       ('test9', '$2a$10$upI6h4n0A7WiD08baqPIf./XzRXA2OchUBZhtHKFV.Y3iPqInePeG', '01090123456', '닉네임9', '19980909', 'M',
        'user9@example.com', TRUE, NOW(), NOW(), 517,
        'token9', 'uuid9.jpg', 'C:\project302\files'),
       ('test10', '$2a$10$upI6h4n0A7WiD08baqPIf./XzRXA2OchUBZhtHKFV.Y3iPqInePeG', '01001234567', '닉네임10', '19991010',
        'F', 'user10@example.com', TRUE, NOW(), NOW(),
        517,
        'token10', 'uuid10.jpg', 'C:\project302\files');

-- 영남대 학생
INSERT INTO USERS (local_id, password, phone_no, nickname, birth, gender, email, email_yn, last_conect_time,
                   subscribe_date, univ_id, token, user_file_id, user_file_path)
VALUES ('test11', '$2a$10$upI6h4n0A7WiD08baqPIf./XzRXA2OchUBZhtHKFV.Y3iPqInePeG', '01022223333', '닉네임11', '19901111',
        'M', 'user11@example.com', TRUE, NOW(), NOW(), 151,
        'token11', 'uuid11.jpg', 'C:\project302\files'),
       ('test12', '$2a$10$upI6h4n0A7WiD08baqPIf./XzRXA2OchUBZhtHKFV.Y3iPqInePeG', '01033334444', '닉네임12', '19912222',
        'F', 'user12@example.com', TRUE, NOW(), NOW(), 151,
        'token12', 'uuid12.jpg', 'C:\project302\files'),
       ('test13', '$2a$10$upI6h4n0A7WiD08baqPIf./XzRXA2OchUBZhtHKFV.Y3iPqInePeG', '01044445555', '닉네임13', '19923333',
        'M', 'user13@example.com', TRUE, NOW(), NOW(), 151,
        'token13', 'uuid13.jpg', 'C:\project302\files'),
       ('test14', '$2a$10$upI6h4n0A7WiD08baqPIf./XzRXA2OchUBZhtHKFV.Y3iPqInePeG', '01055556666', '닉네임14', '19934444',
        'F', 'user14@example.com', TRUE, NOW(), NOW(), 151,
        'token14', 'uuid14.jpg', 'C:\project302\files'),
       ('test15', '$2a$10$upI6h4n0A7WiD08baqPIf./XzRXA2OchUBZhtHKFV.Y3iPqInePeG', '01066667777', '닉네임15', '19945555',
        'M', 'user15@example.com', TRUE, NOW(), NOW(), 151,
        'token15', 'uuid15.jpg', 'C:\project302\files');

-- 공동구매
INSERT INTO PRODUCTS (pd_name, pd_content, pd_price, creat_date, pull_up_date, pull_up_cnt, latitude, longitude,
                      detail_addr, ctgr_id, user_id)
VALUES ('공구상품1', '공구상품2 내용', 22222, now(), now(), 0, 37.123456, 127.123456, '공구상품1 상세주소', 1, 1),
       ('공구상품2', '공구상품2 내용', 20000, now(), now(), 0, 37.234567, 127.234567, '공구상품2 상세주소', 2, 1),
       ('공구상품3', '공구상품3 내용', 30000, now(), now(), 0, 37.345678, 127.345678, '공구상품3 상세주소', 3, 2),
       ('공구상품4', '공구상품4 내용', 40000, now(), now(), 0, 37.456789, 127.456789, '공구상품4 상세주소', 2, 2),
       ('공구상품5', '공구상품5 내용', 50000, now(), now(), 0, 37.567890, 127.567890, '공구상품5 상세주소', 5, 4),
       ('공구상품6', '공구상품6 내용', 60000, now(), now(), 0, 37.678901, 127.678901, '공구상품6 상세주소', 5, 4),
       ('공구상품7', '공구상품7 내용', 70000, now(), now(), 0, 37.789012, 127.789012, '공구상품7 상세주소', 2, 2),
       ('공구상품8', '공구상품8 내용', 80000, now(), now(), 0, 37.890123, 127.890123, '공구상품8 상세주소', 1, 3),
       ('공구상품9', '공구상품9 내용', 90000, now(), now(), 0, 37.901234, 127.901234, '공구상품9 상세주소', 1, 3),
       ('공구상품10', '공구상품10 내용', 100000, now(), now(), 0, 37.012345, 127.012345, '공구상품10 상세주소', 10, 3);

-- 공동구매 상세
INSERT INTO GROUP_DETAILS (min_people, max_people, round_yn, clos_date, pd_id)
VALUES (2, 5, 1, DATE_ADD(NOW(), INTERVAL 7 DAY), 1),
       (3, 4, 1, DATE_ADD(NOW(), INTERVAL 6 DAY), 2),
       (4, 4, 0, DATE_ADD(NOW(), INTERVAL 5 DAY), 3),
       (2, 4, 0, DATE_ADD(NOW(), INTERVAL 4 DAY), 4),
       (3, 10, 1, DATE_ADD(NOW(), INTERVAL 4 DAY), 5),
       (4, 8, 1, DATE_ADD(NOW(), INTERVAL 5 DAY), 6),
       (3, 7, 0, DATE_ADD(NOW(), INTERVAL 6 DAY), 7),
       (2, 5, 1, DATE_ADD(NOW(), INTERVAL 3 DAY), 8),
       (3, 7, 0, DATE_ADD(NOW(), INTERVAL 3 DAY), 9),
       (4, 6, 1, DATE_ADD(NOW(), INTERVAL 5 DAY), 10);


-- 중고상품
INSERT INTO PRODUCTS (user_id, ctgr_id, pd_status, pd_name, pd_content, pd_price, latitude, longitude)
VALUES (1, 1, 'used', '중고상품1', '중고물건팔아요', 5000, 37.123456, 127.123456),
       (2, 2, 'used', '중고상품2', '중고물건팔아요', 7000, 37.123456, 127.123456),
       (3, 3, 'used', '중고상품3', '중고물건팔아요', 12000, 37.123456, 127.123456),
       (4, 4, 'used', '중고상품4', '중고물건팔아요', 5000, 37.123456, 127.123456),
       (5, 5, 'used', '중고상품5', '중고물건팔아요', 7000, 37.123456, 127.123456),
       (6, 6, 'used', '중고상품6', '중고물건팔아요', 12000, 37.123456, 127.123456),
       (7, 7, 'used', '중고상품7', '중고물건팔아요', 5000, 37.123456, 127.123456),
       (8, 8, 'used', '중고상품8', '중고물건팔아요', 7000, 37.123456, 127.123456),
       (9, 9, 'used', '중고상품9', '중고물건팔아요', 12000, 37.123456, 127.123456),
       (10, 10, 'used', '중고상품10', '중고물건팔아요', 5000, 37.123456, 127.123456),
       (1, 11, 'used', '중고상품11', '중고물건팔아요', 7000, 37.123456, 127.123456),
       (2, 12, 'used', '중고상품12', '중고물건팔아요', 12000, 37.123456, 127.123456),
       (3, 13, 'used', '중고상품13', '중고물건팔아요', 5000, 37.123456, 127.123456),
       (4, 14, 'used', '중고상품14', '중고물건팔아요', 7000, 37.123456, 127.123456),
       (5, 15, 'used', '중고상품15', '중고물건팔아요', 12000, 37.123456, 127.123456),
       (6, 1, 'used', '중고상품16', '중고물건팔아요', 5000, 37.123456, 127.123456),
       (7, 2, 'used', '중고상품17', '중고물건팔아요', 7000, 37.123456, 127.123456),
       (8, 3, 'used', '중고상품18', '중고물건팔아요', 12000, 37.123456, 127.123456),
       (9, 4, 'used', '중고상품19', '중고물건팔아요', 5000, 37.123456, 127.123456),
       (10, 5, 'used', '중고상품20', '중고물건팔아요', 7000, 37.123456, 127.123456);

-- 미개봉상품
INSERT INTO PRODUCTS (user_id, ctgr_id, pd_status, pd_name, pd_content, pd_price, latitude, longitude)
VALUES (1, 1, 'new', '미개봉1', '미개봉팔아요', 5000, 37.123456, 127.123456),
       (2, 2, 'new', '미개봉2', '미개봉팔아요', 7000, 37.123456, 127.123456),
       (3, 3, 'new', '미개봉3', '미개봉팔아요', 12000, 37.123456, 127.123456),
       (4, 4, 'new', '미개봉4', '미개봉팔아요', 5000, 37.123456, 127.123456),
       (5, 5, 'new', '미개봉5', '미개봉팔아요', 7000, 37.123456, 127.123456),
       (6, 6, 'new', '미개봉6', '미개봉팔아요', 12000, 37.123456, 127.123456),
       (7, 7, 'new', '미개봉7', '미개봉팔아요', 5000, 37.123456, 127.123456),
       (8, 8, 'new', '미개봉8', '미개봉팔아요', 7000, 37.123456, 127.123456),
       (9, 9, 'new', '미개봉9', '미개봉팔아요', 12000, 37.123456, 127.123456),
       (10, 10, 'new', '미개봉10', '미개봉팔아요', 5000, 37.123456, 127.123456),
       (1, 11, 'new', '미개봉11', '미개봉팔아요', 7000, 37.123456, 127.123456),
       (2, 12, 'new', '미개봉12', '미개봉팔아요', 12000, 37.123456, 127.123456),
       (3, 13, 'new', '미개봉13', '미개봉팔아요', 5000, 37.123456, 127.123456),
       (4, 14, 'new', '미개봉14', '미개봉팔아요', 7000, 37.123456, 127.123456),
       (5, 15, 'new', '미개봉15', '미개봉팔아요', 12000, 37.123456, 127.123456),
       (6, 1, 'new', '미개봉16', '미개봉팔아요', 5000, 37.123456, 127.123456),
       (7, 2, 'new', '미개봉17', '미개봉팔아요', 7000, 37.123456, 127.123456),
       (8, 3, 'new', '미개봉18', '미개봉팔아요', 12000, 37.123456, 127.123456),
       (9, 4, 'new', '미개봉19', '미개봉팔아요', 5000, 37.123456, 127.123456),
       (10, 5, 'new', '미개봉20', '미개봉팔아요', 7000, 37.123456, 127.123456);

-- 공동구매 파일
INSERT INTO FILES (file_id, pd_id, file_path, thumbnail_yn)
VALUES ('file1.jpg', 1, 'C:\project302\files', 1),
       ('file2.jpg', 1, 'C:\project302\files', 0),
       ('file3.jpg', 2, 'C:\project302\files', 1),
       ('file4.jpg', 2, 'C:\project302\files', 0),
       ('file5.jpg', 3, 'C:\project302\files', 1),
       ('file6.jpg', 3, 'C:\project302\files', 0),
       ('file7.jpg', 4, 'C:\project302\files', 1),
       ('file8.jpg', 4, 'C:\project302\files', 0),
       ('file9.jpg', 5, 'C:\project302\files', 1),
       ('file10.jpg', 5, 'C:\project302\files', 0),
       ('file11.jpg', 11, 'C:\project302\files', 1),
       ('file12.jpg', 11, 'C:\project302\files', 0),
       ('file13.jpg', 12, 'C:\project302\files', 1),
       ('file14.jpg', 12, 'C:\project302\files', 0),
       ('file15.jpg', 13, 'C:\project302\files', 1),
       ('file16.jpg', 13, 'C:\project302\files', 0),
       ('file17.jpg', 14, 'C:\project302\files', 1),
       ('file18.jpg', 14, 'C:\project302\files', 0),
       ('file19.jpg', 15, 'C:\project302\files', 1),
       ('file20.jpg', 15, 'C:\project302\files', 0),
       ('file21.jpg', 31, 'C:\project302\files', 1),
       ('file22.jpg', 31, 'C:\project302\files', 0),
       ('file23.jpg', 32, 'C:\project302\files', 1),
       ('file24.jpg', 32, 'C:\project302\files', 0),
       ('file25.jpg', 33, 'C:\project302\files', 1),
       ('file26.jpg', 33, 'C:\project302\files', 0),
       ('file27.jpg', 34, 'C:\project302\files', 1),
       ('file28.jpg', 34, 'C:\project302\files', 0),
       ('file29.jpg', 35, 'C:\project302\files', 1),
       ('file30.jpg', 35, 'C:\project302\files', 0);


-- 공동구매 거래참여자 (상품 1~6번)
INSERT INTO PARTICIPATIONS (pd_id, user_id)
VALUES (1, 5),
       (1, 6),
       (1, 7),
       (1, 8),
       (2, 2),
       (2, 3),
       (2, 4),
       (3, 7),
       (3, 8),
       (3, 9),
       (4, 3),
       (5, 1),
       (5, 2),
       (6, 1),
       (6, 2),
       (6, 3);

-- 중고상품 거래참가자 (상품 11~15번, 31~35번)
INSERT INTO PARTICIPATIONS (pd_id, user_id)
VALUES (11, 2),
       (12, 3),
       (13, 4),
       (14, 5),
       (15, 6),
       (31, 6),
       (32, 5),
       (33, 4),
       (34, 3),
       (35, 2);

-- 예약상품 업데이트 (참가자가 있는 공동구매 -> 'dep', 중고거래 -> 'resv'
UPDATE products
SET deal_status='DEP'
WHERE pd_id BETWEEN 1 AND 6;
UPDATE products
SET deal_status='RESV'
WHERE pd_id BETWEEN 11 AND 15;
UPDATE products
SET deal_status='RESV'
WHERE pd_id BETWEEN 31 AND 35;

-- 채팅방
INSERT INTO chatrooms (pd_id)
VALUES (2),
       (3),
       (11),
       (12),
       (12);

-- 채팅참가
INSERT INTO chat_participations (chat_id, user_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (2, 2),
       (2, 7),
       (2, 8),
       (2, 9),
       (3, 1),
       (3, 2),
       (4, 2),
       (4, 3),
       (5, 2),
       (5, 4);

-- 채팅메세지
INSERT INTO messages (user_id, chat_id, content, read_not)
VALUES (1, 1, '어쩔티비', 1);
INSERT INTO messages (user_id, chat_id, content, read_not)
VALUES (2, 1, '저쩔티비', 1);
INSERT INTO messages (user_id, chat_id, content, read_not)
VALUES (3, 1, '어쩔티비', 1);
INSERT INTO messages (user_id, chat_id, content, read_not)
VALUES (3, 1, '어쩔티비', 1);
INSERT INTO messages (user_id, chat_id, content, read_not)
VALUES (2, 2, '저쩔티비', 1);
INSERT INTO messages (user_id, chat_id, content, read_not)
VALUES (2, 2, '저쩔티비', 1);
INSERT INTO messages (user_id, chat_id, content, read_not)
VALUES (7, 2, '어쩔티비', 1);
INSERT INTO messages (user_id, chat_id, content, read_not)
VALUES (1, 3, '어쩔티비', 1);
INSERT INTO messages (user_id, chat_id, content, read_not)
VALUES (1, 3, '어쩔티비', 1);
INSERT INTO messages (user_id, chat_id, content, read_not)
VALUES (2, 3, '저쩔티비', 1);
INSERT INTO messages (user_id, chat_id, content, read_not)
VALUES (2, 4, '저쩔티비', 1);
INSERT INTO messages (user_id, chat_id, content, read_not)
VALUES (3, 4, '어쩔티비', 1);
INSERT INTO messages (user_id, chat_id, content, read_not)
VALUES (4, 5, '어쩔티비', 1);
INSERT INTO messages (user_id, chat_id, content, read_not)
VALUES (4, 5, '어쩔티비', 1);
INSERT INTO messages (user_id, chat_id, content, read_not)
VALUES (2, 5, '저쩔티비', 1);


-- 채팅 파일 메세지
INSERT INTO project302.chat_files (chat_file_id, user_id, chat_id, chat_file_path, read_not)
VALUES ('chat_file_1.jpg', 2, 1, 'C:project302files', 1);
INSERT INTO project302.chat_files (chat_file_id, user_id, chat_id, chat_file_path, read_not)
VALUES ('chat_file_2.jpg', 2, 1, 'C:project302files', 1);
INSERT INTO project302.chat_files (chat_file_id, user_id, chat_id, chat_file_path, read_not)
VALUES ('chat_file_3.jpg', 1, 3, 'C:project302files', 1);
INSERT INTO project302.chat_files (chat_file_id, user_id, chat_id, chat_file_path, read_not)
VALUES ('chat_file_4.jpg', 4, 5, 'C:project302files', 1);


-- 관심 상품
insert into likes values(1, 1, 3, 11);
insert into likes values(2, 1, 4, 12);
insert into likes values(3, 1, 5, 12);
insert into likes values(4, 2, 6, 13);
insert into likes values(5, 2, 1, 11);
insert into likes values(6, 2, 1, 12);
insert into likes values(7, 3, 2, 13);
insert into likes values(8, 4, 3, 14);
insert into likes values(9, 5, 4, 15);
insert into likes values(10, 6, 5, 16);