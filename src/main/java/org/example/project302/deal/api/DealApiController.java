package org.example.project302.deal.api;

import lombok.RequiredArgsConstructor;
import org.example.project302.chat.dto.GetChatProdResponse;
import org.example.project302.deal.dto.ScheduleForm;
import org.example.project302.deal.service.DealService;
import org.example.project302.user.entity.User;
import org.example.project302.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/deal")
@RequiredArgsConstructor
public class DealApiController {
    private final UserService userService;
    private final DealService dealService;


    /*
     *  상품 거래 상태 수정 .-≡=★
     */
    @GetMapping("/{chatId}/{updatedStatus}")
    public ResponseEntity<GetChatProdResponse> updateDealStatus(
            @PathVariable("chatId") Long chatId,
            @PathVariable("updatedStatus") String updatedStatus) {
        GetChatProdResponse updated = dealService.updateDealStatus(chatId, updatedStatus);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }


    /*
     *  입금 체크 .-≡=★
     */
    @GetMapping("/check/{pdId}/{userId}")
    public ResponseEntity<Boolean> updatePartUserDep(@PathVariable("pdId") Long pdId,
                                                     @PathVariable("userId") Long userId) {
        User user = userService.findById(String.valueOf(userId));
        return dealService.checkPartUser(user, pdId);
    }


    /*
     *  전달 체크 .-≡=★
     */
    @GetMapping("/check/{pdId}")
    public ResponseEntity<Boolean> updatePartUserTake(@PathVariable("pdId") Long pdId,
                                                      Principal principal) {
        User user = userService.findById(principal.getName());
        return dealService.checkPartUser(user, pdId);
    }


    /*
     *  전달 체크 .-≡=★
     */
//    @GetMapping("/taked/{pdId}")
//    public void updatePartUserTake(Principal principal,
//                                   @PathVariable("pdId") Long pdId) {
//        User user = userService.findById(principal.getName());
//        dealService.countTakedUsers(pdId);
//    }


    /*
     *  출발 확인 - 중간발표 이후
     */
    @GetMapping("/start/{pdId}")
    public void updatePartUserStart(Principal principal,
                                    @PathVariable("pdId") Long pdId) {
        User user = userService.findById(principal.getName());
        dealService.updatePartUserStart(user, pdId);
    }


    /*
     *  거래 일정 등록 및 수정 .-≡=★
     */
    @GetMapping("/schedule/save")
    public void createSchedule(Principal principal,
                               ScheduleForm form) {
        User user = userService.findById(principal.getName());
        dealService.saveSchedule(form);
    }


    /*
     *  일정 삭제 및 취소
     */
    @GetMapping("/schedule/cancle/{pdId}")
    public void cancleSchedule(@PathVariable("pdId") Long pdId,
                               Principal principal) {
        User user = userService.findById(principal.getName());
        dealService.deleteSchedule(pdId, user);
    }

}
