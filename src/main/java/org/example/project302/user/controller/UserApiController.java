package org.example.project302.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project302.user.service.CalendarService;
import org.example.project302.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@Slf4j
//@RequestMapping("/api/users")
public class UserApiController {
    private final UserService userService;
    private final CalendarService calendarService;

    /*
     * 아이디 중복 확인
     * */
    @GetMapping("/join/check-id")
    public boolean checkId(@RequestParam String localId) {
        return !userService.hasUserId(localId);
    }

    /*
    * 비밀번호 변경전 id 확인
    * */
    @GetMapping("/help/check-id")
    public boolean  checkLocalId(@RequestParam String localId,
                                Principal principal) {
        log.info("localId check = {},principalId check = {}",localId,userService.findById(principal.getName()).getLocalId());
       return !Objects.equals(userService.findById(principal.getName()).getLocalId(), localId);

    }



    /*
     * 닉네임 중복 확인
     * */
    @GetMapping("/join/check-nickname")
    public boolean checkNickname(@RequestParam String nickname) {
        return !userService.hasUserNickname(nickname);
    }

    // 일정 테스트
    @GetMapping("/api/mypage/{userId}")
    public ResponseEntity<List<Map<String, Object>>> check(@PathVariable("userId") Long userId, Principal principal) {
        List<Map<String, Object>> res = calendarService.getEvents(principal);
        return ResponseEntity.ok(res);
    }
}
