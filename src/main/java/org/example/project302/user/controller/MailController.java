package org.example.project302.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project302.univ.UniversityRepository;
import org.example.project302.user.dto.UnivInfo;
import org.example.project302.user.entity.User;
import org.example.project302.user.repository.UserRepository;
import org.example.project302.user.service.MailService;
import org.example.project302.user.service.MypageService;
import org.example.project302.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MailController {
    private final MailService mailService;
    private final UserService userService;
    private final MypageService mypageService;
    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;

    private String authCode; // 이메일 인증 숫자를 저장하는 변수
    private User user;

    // 인증 이메일 전송
    @PostMapping("/join/mailSend")
    public HashMap<String, Object> mailSend(@RequestParam("email") String mail) {
        HashMap<String, Object> map = new HashMap<>();

        try {
            authCode = mailService.sendMail(mail);
            String code = String.valueOf(authCode);
            map.put("success", Boolean.TRUE);
            map.put("number", code);
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }

        return map;
    }

    // 인증번호 일치여부 확인
    @GetMapping("/join/mailCheck")
    public boolean mailCheck(@RequestParam("emailAuth") String userNumber) {

        boolean isMatch = userNumber.equals(String.valueOf(authCode));

        return isMatch;
    }

    @PostMapping("/help/mailSend")
    public boolean helpMailSend(@RequestParam("email") String mail) {
        user = userService.findByEmail(mail);
        if(user == null){
            return false;
        }
            authCode = mailService.sendMail(mail);

        return true;
    }

    // 인증번호 일치여부 확인
    @GetMapping("/help/mailCheck")
    public User helpMailCheck(@RequestParam("emailAuth") String userNumber) {

        boolean isMatch = userNumber.equals(String.valueOf(authCode));
        if(isMatch){
            return user;
        }
        return new User();
    }

    // 대학 인증 이메일 전송
    @PostMapping("/mypage/univMailSend")
    @ResponseBody
    public boolean univEmailSend(@RequestParam("mail") String mail,
                                 @RequestBody  UnivInfo univInfo) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
//        log.info("UnivInfo = {}",univInfo);  // 잘 받아옴
        if(mypageService.univEmailCheck(univInfo)){

            try {
                authCode = mailService.sendMail(mail);
                String code = String.valueOf(authCode);
                map.put("success", Boolean.TRUE);
                map.put("number", code);
            } catch (Exception e) {
                map.put("success", Boolean.FALSE);
                map.put("error", e.getMessage());
            }

            return (boolean) map.get("success");
        }
        map.put("success", Boolean.FALSE);
        return (boolean) map.get("success");
    }

    // 인증번호 일치여부 확인
    @PostMapping("/mypage/univEmailAuth")
    @ResponseBody
    public boolean univEmail(@RequestParam("emailAuth") String userNumber,
                             @RequestBody UnivInfo univInfo,
                             Principal principal) {
        // 인증번호 확인 되면 user 이메일 인증 여부 확인 대학 id 넣어주기
        boolean isMatch = userNumber.equals(String.valueOf(authCode));
        if(isMatch){
            mypageService.univAuth(univInfo, principal);
            return true;
        }
        return false;
    }


}
