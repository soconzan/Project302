package org.example.project302.notification.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FCMSendDto {   // 전달 받은 객체 매핑
    private String token;   // 디바이스 기기에서 발급받은 FCM 토큰값
    private String title;   // 푸시 메시지 제목
    private String body;    // 푸시 메시지 내용

    @Builder(toBuilder = true)
    public FCMSendDto(String token, String title, String body) {
        this.token = token;
        this.title = title;
        this.body = body;
    }
}
