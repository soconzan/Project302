package org.example.project302.deal.dto;

import lombok.Data;
import org.example.project302.user.entity.User;

@Data
public class GetUserRvwChckRes {
    private Long userId;
    private String nickname;
    private String userFileLink;
    private boolean isWritten;

    public GetUserRvwChckRes(User user, String userFileLink, boolean isWritten) {
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.userFileLink = userFileLink;
        this.isWritten = isWritten;
    }
}