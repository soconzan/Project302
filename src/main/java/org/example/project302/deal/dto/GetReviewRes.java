package org.example.project302.deal.dto;

import lombok.Data;
import org.example.project302.deal.entity.Review;
import org.example.project302.deal.entity.ReviewType;

import java.time.LocalDateTime;

@Data
public class GetReviewRes {
    private Long pdId;
    private Long userId;
    private String nickname;
    private Long simpleReview;
    private String detailReview;
    private LocalDateTime createDate;
    private ReviewType reviewType;
    private String fileLink;
    private String userFileLink;

    public GetReviewRes(Review review, String fileLink, String userFileLink) {
        this.pdId = review.getProduct().getPdId();
        this.userId = review.getReceiver().getUserId();
        this.nickname = review.getReceiver().getNickname();
        this.simpleReview = review.getSimpleReview();
        this.detailReview = review.getDetailReview();
        this.createDate = review.getCreatDate();
        this.reviewType = review.getReviewType();
        this.fileLink = fileLink;
        this.userFileLink = userFileLink;
    }
}
