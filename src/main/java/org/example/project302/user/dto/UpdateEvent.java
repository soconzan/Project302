package org.example.project302.user.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class UpdateEvent {
    private String id;
    private String start;

    public LocalDateTime convertStringToLocalDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(this.start, formatter);
    }

}
