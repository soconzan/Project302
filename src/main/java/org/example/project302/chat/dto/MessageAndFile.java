package org.example.project302.chat.dto;

import lombok.Data;

import java.time.LocalDateTime;

public interface MessageAndFile {
    public Long getUser_id();
    public Long getChat_id();
    public String getContent();
    public String getChat_file_id();
    public LocalDateTime getCreat_date();
    public Integer getRead_not();
}
