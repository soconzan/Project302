package org.example.project302.chat.api;

import lombok.RequiredArgsConstructor;
import org.example.project302.chat.dto.AddMessageRequest;
import org.example.project302.chat.service.ChatService;
import org.example.project302.chat.service.MessageService;
import org.example.project302.user.service.UserService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageApiController {
    private final SimpMessageSendingOperations messagingTemplate; // 메시지 송신 객체
    private final ChatService chatService;
    private final MessageService messageService;
    private final UserService userService;

    @MessageMapping("")
    public void message(AddMessageRequest message) {
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getChatId(), message);
    }
}

