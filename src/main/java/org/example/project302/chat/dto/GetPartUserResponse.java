package org.example.project302.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.chat.entity.ChatParticipation;
import org.example.project302.deal.entity.Participation;
import org.example.project302.user.entity.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPartUserResponse {
    private Long userId;
    private String nickname;
    private String userFileLink;
    private LocalDateTime lastConectTime;
//    private LocalDateTime depositDate;
//    private LocalDateTime takedDate;
//    private LocalDateTime startDate;

    public GetPartUserResponse(User user, String userFileLink) {
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.userFileLink = userFileLink;
        this.lastConectTime = user.getLastConectTime();
//        this.depositDate = null;
//        this.takedDate = null;
//        this.startDate = null;
    }

    public GetPartUserResponse(Participation part, String userFileLink) {
        this.userId = part.getUser().getUserId();
        this.nickname = part.getUser().getNickname();
        this.userFileLink = userFileLink;
        this.lastConectTime = part.getUser().getLastConectTime();
//        this.depositDate = part.getDepositDate();
//        this.takedDate = part.getTakedDate();
//        this.startDate = part.getStartDate();
    }

    public GetPartUserResponse(ChatParticipation chatPart, String userFileLink) {
        this.userId = chatPart.getUser().getUserId();
        this.nickname = chatPart.getUser().getNickname();
        this.userFileLink = userFileLink;
        this.lastConectTime = chatPart.getUser().getLastConectTime();
//        this.depositDate = null;
//        this.takedDate = null;
//        this.startDate = null;
    }

}
