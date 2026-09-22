package org.example.project302.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.chat.entity.ChatNotice;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMessageResponse {
    private Long userId;
    private String nickname;
    private String userFileLink;
    private String content;
    private String chatFileLink;
    private LocalDateTime creatDate;
    private Integer readNot;
    private Long chatId;

    public GetMessageResponse(ChatNotice chatNotice) {
        content = chatNotice.getContent();
        creatDate = chatNotice.getCreatDate();
        chatId = chatNotice.getChatroom().getChatId();
    }
}
