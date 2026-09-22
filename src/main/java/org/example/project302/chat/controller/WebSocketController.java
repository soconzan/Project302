package org.example.project302.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.project302.chat.service.ChatService;
import org.example.project302.user.entity.User;
import org.example.project302.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/socket")
public class WebSocketController {

    private final UserService userService;
    private final ChatService chatService;

    @GetMapping("")
    public ResponseEntity<List<Long>> loadWebSocket(Principal principal) {
        User user = userService.findById(principal.getName());
        return ResponseEntity.ok(chatService.getChatIdList(user));
    }
}
