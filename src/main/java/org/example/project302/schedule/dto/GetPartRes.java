package org.example.project302.schedule.dto;

import lombok.Data;
import org.example.project302.deal.entity.Participation;

import java.time.LocalDateTime;

@Data
public class GetPartRes {
    private Long userId;
    private String nickname;
    private String userFileLink;
    private LocalDateTime takedDate;
    private Float latitudeNow;
    private Float longitudeNow;

    public GetPartRes(Participation participation, String userFileLink) {
        this.userId = participation.getUser().getUserId();
        this.nickname = participation.getUser().getNickname();
        this.userFileLink = userFileLink;
        this.takedDate = participation.getTakedDate();
        this.latitudeNow = participation.getLatitudeNow();
        this.longitudeNow = participation.getLongitudeNow();
    }
}
