package org.example.project302.deal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.deal.entity.Review;
import org.example.project302.deal.entity.ReviewType;
import org.example.project302.product.entity.Product;
import org.example.project302.user.entity.User;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewForm {
    private Long pdId;
    private Long receiverId;
    private String reviewType;
    private List<Integer> simpleReviews;
    private String detailReview;

    public Review toEntity() {
        return new Review(
                null,
                this.getReviewType(),
                this.getSimpleReview(),
                this.getDetailReview(),
                null,
                null,
                null,
                null
        );
    }

    private Long getSimpleReview() {
        Long sum = 0L;
        for (Integer review : simpleReviews) {
            if (review != null) {
                sum += review;
            }
        }
        return sum;
    }

    private ReviewType getReviewType() {
        switch (this.reviewType) {
            case "BUY":
                return ReviewType.BUY;
            default:
                return ReviewType.SELL;
        }
    }
}
