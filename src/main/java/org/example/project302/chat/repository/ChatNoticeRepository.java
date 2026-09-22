package org.example.project302.chat.repository;

import org.example.project302.chat.entity.ChatNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatNoticeRepository extends JpaRepository<ChatNotice, Long> {
}
