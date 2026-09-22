package org.example.project302.chat.api;

import lombok.RequiredArgsConstructor;
import org.example.project302.chat.dto.GetChatListResponse;
import org.example.project302.chat.dto.GetMessageResponse;
import org.example.project302.chat.dto.GetPartUserResponse;
import org.example.project302.chat.service.ChatService;
import org.example.project302.chat.service.MessageService;
import org.example.project302.user.entity.User;
import org.example.project302.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatApiController {
    private final ChatService chatService;
    private final UserService userService;
    private final MessageService messageService;

    /*
     *  채팅방 전체 조회 .-≡=★
     */
    @GetMapping("")
    public List<GetChatListResponse> selectChats(Principal loginInfo) {
        User user = userService.findById(loginInfo.getName());
        return chatService.getChatList(user);
    }


    /*
     *  중고 채팅방 전체 조회 .-≡=★
     */
    @GetMapping("/used")
    public List<GetChatListResponse> selectUsedChats(Principal loginInfo) {
        User user = userService.findById(loginInfo.getName());
        return chatService.getUsedChatList(user);
    }


    /*
     *  공구 채팅방 전체 조회 .-≡=★
     */
    @GetMapping("/group")
    public List<GetChatListResponse> selectGroupsChats(Principal loginInfo) {
        User user = userService.findById(loginInfo.getName());
        return chatService.getGroupChatList(user);
    }


    /*
     *  채팅 참여자 조회 .-≡=★
     */
    @GetMapping("/{chatId}/users")
    public List<GetPartUserResponse> selectChatUsers(@PathVariable("chatId") Long chatId) {
        return chatService.getPartUser(chatId);
    }


    /*
     *  대화 내역 조회 .-≡=★
     */
    @GetMapping("/{chatId}")
    public List<GetMessageResponse> selectMessages(Principal principal,
                                                   @PathVariable("chatId") Long chatId) {
        User user = userService.findById(principal.getName());
        return messageService.getMsgList(chatId, user);
    }


    /*
     *  채팅 목록
     */
    @GetMapping("/reloadList")
    public ResponseEntity<List<GetChatListResponse>> chatList(
            Principal principal) {
        User user = userService.findById(principal.getName());
        return ResponseEntity.ok(chatService.getChatList(user));
    }
}
