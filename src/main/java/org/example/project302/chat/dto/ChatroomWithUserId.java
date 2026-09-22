package org.example.project302.chat.dto;

import java.time.LocalDateTime;

public interface ChatroomWithUserId {
    public Long getChat_id();

    public String getContent();

    public LocalDateTime getCreat_date();

    public Long getPd_id();

//    public String getPd_name();

    public Integer getHeadcount();

//    public String getFile_id();
}
