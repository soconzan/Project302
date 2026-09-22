package org.example.project302.schedule.dto;

import lombok.Data;
import org.example.project302.product.entity.Product;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class GetSchdlRes {
    private Long pdId;
    private Long userId;
    private String pdName;
    private LocalDateTime scheduleDate;
    private String scheduleDateString;
    private String date;
    private String time;
    private Float longitude;
    private Float latitude;
    private String detailAddr;
    private String fileLink;

    public GetSchdlRes(Product product, String fileLink) {
        this.pdId = product.getPdId();
        this.userId = product.getUser().getUserId();
        this.pdName = product.getPdName();
        this.scheduleDate = product.getScheduleDate();
        this.scheduleDateString = getScheduleDate(product.getScheduleDate());
        this.date = getDate(product.getScheduleDate());
        this.time = getTime(product.getScheduleDate());
        this.longitude = product.getLongitude();
        this.latitude = product.getLatitude();
        this.detailAddr = product.getDetailAddr();
        this.fileLink = fileLink;
    }

    private String getScheduleDate(LocalDateTime scheduleDate) {
        if (scheduleDate == null)
            return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return scheduleDate.format(formatter);
    }

    private String getDate(LocalDateTime scheduleDate) {
        if (scheduleDate == null)
            return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일");
        return scheduleDate.format(formatter);
    }

    private String getTime(LocalDateTime scheduleDate) {
        if (scheduleDate == null)
            return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h시 m분");
        String amPm = scheduleDate.getHour() < 12 ? "오전" : "오후";
        return amPm + " " + scheduleDate.format(formatter);
    }
}
