package org.example.project302.deal.dto;

import lombok.Data;
import org.example.project302.deal.entity.ReviewType;
import org.example.project302.user.entity.User;

@Data
public class GetReceiverRes {
    private Long userId;
    private String nickname;
    private String userFileLink;
    private ReviewType reviewType;

    public GetReceiverRes(User user, String userFileLink, ReviewType reviewType) {
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.userFileLink = userFileLink;
        this.reviewType = reviewType;
    }
}
