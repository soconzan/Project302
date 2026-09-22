package org.example.project302.deal.dto;

import lombok.Data;
import org.example.project302.user.entity.User;

@Data
public class GetDealPartRes {
    private Long userId;
    private String nickname;
    private String userFileLink;
    private boolean checked;

    public GetDealPartRes(User user, String userFileLink, boolean checked) {
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.userFileLink = userFileLink;
        this.checked = checked;
    }
}
