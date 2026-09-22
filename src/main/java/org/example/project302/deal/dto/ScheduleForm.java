package org.example.project302.deal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleForm {
    private Long pdId;
    private LocalDateTime scheduleDate;
    private Float longitude;
    private Float latitude;
    private String detailAddr;
}
