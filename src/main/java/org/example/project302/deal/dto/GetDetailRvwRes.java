package org.example.project302.deal.dto;

import lombok.Data;
import org.example.project302.deal.entity.Review;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
public class GetDetailRvwRes {
    private Long userId;
    private String nickname;
    private String userFileLink;
    private Long pdId;
    private String pdName;
    private String detailReview;
    private String creatDate;

    public GetDetailRvwRes(Review review, String userFileLink) {
        this.userId = review.getSender().getUserId();
        this.nickname = review.getSender().getNickname();
        this.userFileLink = userFileLink;
        this.pdId = review.getProduct().getPdId();
        this.pdName = review.getProduct().getPdName();
        this.detailReview = review.getDetailReview();
//        this.creatDate = review.getCreatDate().toString();
        // 기본값 LocalDateTime
        this.creatDate = getCreateDate(review.getCreatDate());
    }

    private String getCreateDate(LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        long seconds = ChronoUnit.SECONDS.between(date, now);
        if (seconds < 60) {
            return seconds + "초 전";
        }
        long minutes = ChronoUnit.MINUTES.between(date, now);
        if (minutes < 60) {
            return minutes + "분 전";
        }
        long hours = ChronoUnit.HOURS.between(date, now);
        if (hours < 24) {
            return hours + "시간 전";
        }
        long days = ChronoUnit.DAYS.between(date, now);
        return days + "일 전";
    }
}
