package org.example.project302.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.product.entity.Product;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetChatListResponse {
    private Long chatId;
    private String content;
    private LocalDateTime creatDate;
    private String pdName;
    private Integer headcount;
    private String fileLink;
    private List<GetPartUserResponse> partUsers;

    public GetChatListResponse(ChatroomWithUserId chat) {
        this.chatId = chat.getChat_id();
        this.content = chat.getContent();
        this.creatDate = chat.getCreat_date();
        this.headcount = chat.getHeadcount();
    }

}
