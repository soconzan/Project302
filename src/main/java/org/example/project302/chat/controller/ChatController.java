package org.example.project302.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project302.chat.entity.Chatroom;
import org.example.project302.chat.service.ChatService;
import org.example.project302.chat.service.MessageService;
import org.example.project302.deal.service.DealService;
import org.example.project302.user.entity.User;
import org.example.project302.user.service.UserService;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/chats")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;
    private final MessageService messageService;
    private final DealService dealService;


    /*
     *  채팅 view .-≡=★
     */
    @GetMapping("")
    public String chats(Model model, Principal loginInfo) {
        User user = userService.findById(loginInfo.getName());
        model.addAttribute("chats", chatService.getChatList(user));
        return "chats/chats";
    }


    /*
     *  채팅 목록
     */
    @GetMapping("/list")
    public String chatList(Model model,
                           Principal principal) {
        User user = userService.findById(principal.getName());
        model.addAttribute("chats", chatService.getChatList(user));
        return "chats/_chatList";
    }


    /*
     *  채팅방 한 건 조회 .-≡=★
     */
    @GetMapping("/{chatId}")
    public String show(@PathVariable("chatId") Long chatId,
                       Model model,
                       Principal principal) {
        User user = userService.findById(principal.getName());
        model.addAttribute("msgs", messageService.getMsgList(chatId, user));
        model.addAttribute("isDealing", dealService.checkDealingChat(chatId));
        model.addAttribute("user", chatService.getSelfInfo(user));
        model.addAttribute("partUsers", chatService.getPartUserNoUser(chatId, user));
        model.addAttribute("product", chatService.selectProdByChatId(chatId));
        model.addAttribute("chatId", chatId);
        model.addAttribute("hasReview", dealService.checkWrittenReview(user, chatId));
        model.addAttribute("userDeal", dealService.getSelfDeal(user, chatId));
        return "chats/chatroom";
    }


    /*
     *  채팅방 생성 .-≡=★
     */
//    @GetMapping("/join")
//    public String addChat(@RequestParam Long pdId, Principal loginInfo) {
//        User user = userService.findById(loginInfo.getName());
//        Chatroom chatroom = chatService.joinChat(pdId, user);
//        Long chatId = chatroom.getChatId();
//        return "redirect:chats/" + chatId;
//    }


    /*
    *  채팅방 입장 .-≡=★
    */
    @GetMapping("/room/{chatId}")
    public String joinChatRoom(@PathVariable Long chatId,
                              Principal loginInfo,
                              Model model) {
        User user = userService.findById(loginInfo.getName());
        model.addAttribute("chatId", chatId);
        model.addAttribute("chats", chatService.getChatList(user));
        return "chats/chats";
    }


    /*
     *  실례합니다아 -지인- .-≡=★
     */
    @GetMapping("/join/{pdId}")
    public String addChatRoom(@PathVariable Long pdId, Principal loginInfo, Model model) {
        User user = userService.findById(loginInfo.getName());
        model.addAttribute("chatId", chatService.joinChat(pdId, user).getChatId());
        model.addAttribute("chats", chatService.getChatList(user));
        return "chats/chats";
    }


    /*
     *  채팅방 나가기 .-≡=★
     */
    @GetMapping("/quit/{chatId}")
    public String removeChatPart(@PathVariable Long chatId, Principal loginInfo) {
        User user = userService.findById(loginInfo.getName());
        chatService.deleteChatPart(chatId, user);
        return "redirect:/chats";
    }

}
