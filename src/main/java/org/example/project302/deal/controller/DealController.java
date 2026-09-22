package org.example.project302.deal.controller;

import lombok.RequiredArgsConstructor;
import org.example.project302.chat.service.ChatService;
import org.example.project302.deal.dto.ScheduleForm;
import org.example.project302.deal.service.DealService;
import org.example.project302.user.entity.User;
import org.example.project302.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {
    private final UserService userService;
    private final DealService dealService;
    private final ChatService chatService;


    /*
     *  상품 거래 상태 수정 .-≡=★
     */
    @GetMapping("/{chatId}/{updatedStatus}")
    public String updateDealStatus(
            @PathVariable("chatId") Long chatId,
            @PathVariable("updatedStatus") String updatedStatus,
            Principal principal,
            Model model) {
        dealService.updateDealStatus(chatId, updatedStatus);
        return "redirect:/chats/" + chatId;
    }


    /*
     *  참여자 체크 여부 확인 .-≡=★
     */
    @GetMapping("/{pdId}/checkedlist")
    public String getPartUserChecked(
            Principal principal,
            @PathVariable("pdId") Long pdId,
            Model model) {
        User user = userService.findById(principal.getName());
        model.addAttribute("user", chatService.getSelfInfo(user));
        model.addAttribute("product", chatService.selectProdByPdId(pdId));
        model.addAttribute("dealUsers", dealService.getDealParts(pdId));
        return "chats/_users_checked";
    }


    /*
     *  거래 일정 조회 .-≡=★
     */
    @GetMapping("/schedule/{pdId}")
    public String viewSchedule(Model model,
                               Principal principal,
                               @PathVariable("pdId") Long pdId) {
        model.addAttribute("schedule", dealService.getScheduleRes(pdId));
        model.addAttribute("userId", Long.parseLong(principal.getName()));
        return "chats/_schedule";
    }


    /*
     *  거래 일정 등록 view .-≡=★
     */
    @GetMapping("/schedule/save/{pdId}")
    public String saveSchedule(Principal principal,
                               Model model,
                               @PathVariable("pdId") Long pdId) {
        User user = userService.findById(principal.getName());
        model.addAttribute("product", dealService.getScheduleRes(pdId, user));
        return "chats/_scheduleForm";
    }


    /*
     *  거래 일정 등록 & 수정 .-≡=★
     */
    @PostMapping("/schedule/save")
    public String createSchedule(ScheduleForm form,
                                 Model model) {
        model.addAttribute("schedule", dealService.saveSchedule(form));
        Long chatId = dealService.saveSchedule(form).getChatId();
        return "redirect:/chats/" + chatId;
    }


    /*
     *  일정 취소 .-≡=★
     */
    @GetMapping("/schedule/remove/{pdId}")
    public String removeSchedule(Principal principal,
                                 @PathVariable("pdId") Long pdId) {
        User user = userService.findById(principal.getName());
        Long chatId = dealService.deleteSchedule(pdId, user).getChatId();
        return "redirect:/chats/" + chatId;
    }


    /*
     *  참여자 후기 작성 확인 - 판매자 화면
     */
    @GetMapping("/{pdId}/reviewlist")
    public String getPartUserReview(Principal principal,
                                    @PathVariable("pdId") Long pdId,
                                    Model model) {

        User user = userService.findById(principal.getName());
        model.addAttribute("pdId", pdId);
        model.addAttribute("dealUsers", dealService.getUserRvwChckResList(user, pdId));
        return "chats/_users_review";
    }
}
