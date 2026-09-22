package org.example.project302.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.project302.chat.dto.AddMessageRequest;
import org.example.project302.chat.dto.GetMessageResponse;
import org.example.project302.chat.service.ChatService;
import org.example.project302.chat.service.MessageService;
import org.example.project302.user.entity.User;
import org.example.project302.user.service.UserService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessageSendingOperations messagingTemplate; // 메시지 송신 객체
    private final ChatService chatService;
    private final MessageService messageService;
    private final UserService userService;

    /* 채팅 메시지 저장 */
    @MessageMapping("/{chatId}") // /chatroom 생략
    @SendTo("/chatroom/{chatId}") // 구독하고 있는 장소로 메시지 전송 (목적지)
    public List<GetMessageResponse> message(@DestinationVariable Long chatId,
                                            AddMessageRequest messageRequest) {
        List<GetMessageResponse> messages = messageService.saveMessage(messageRequest);
        return messages;
    }

    /* 채팅 파일 저장 */
    @PostMapping("/chats/{chatId}/file")
    public List<GetMessageResponse> files(@RequestParam("files") List<MultipartFile> files,
                                          @PathVariable("chatId") Long chatId,
                                          Principal login) {
        User user = userService.findById(login.getName());
        List<GetMessageResponse> messages = messageService.saveChatFiles(user, chatId, files);
        return messages;
    }

    /* 채팅 파일 저장 후 뿌리기 */
    @MessageMapping("/file/{chatId}")
    @SendTo("/chatroom/{chatId}")
    public List<GetMessageResponse> sendFiles(List<GetMessageResponse> messages) {
        return messages;
    }

}
