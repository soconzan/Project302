package org.example.project302.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project302.user.dto.AddUserRequest;
import org.example.project302.user.dto.AddUserResponse;
import org.example.project302.user.dto.CreateAccessTokenRequest;
import org.example.project302.user.entity.User;
import org.example.project302.user.service.TokenService;
import org.example.project302.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;

//    @GetMapping("/failure")
//    public String failure() {
//        return "_이거_뭐임_failure";
//    }
//
//    @PostMapping("/failure")
//    public String postIndex(Model model) {
//        return "_이거_뭐임_failure";
//    }


    @GetMapping("/success")
    public String success(Model model, Principal user) {
//        log.info("user = {}",user);
        User loginUser = userService.findById(user.getName()); //UserDetailsService 확인
        System.out.println(loginUser.getNickname());
        model.addAttribute("user", loginUser);
        return "users/success";
    }

    @PostMapping("/success")
    public String postSuccess(Model model, Principal user) {
        log.info("user = {}", user);
        User loginUser = userService.findById(user.getName()); //UserDetailsService 확인
        System.out.println(loginUser.getNickname());
        model.addAttribute("user", loginUser);
        return "users/success";
    }

    /*
     * 로그인
     * */
    @GetMapping("/signin")
    public String signIn(@RequestParam(name = "message", required = false) String message,
                         HttpServletResponse response,
                         Model model) {

        model.addAttribute("message", message);
        return "users/signin";
    }

    @PostMapping("/signin")
    public String postSignin(
            @RequestParam String localId,
            @RequestParam String password,
            HttpServletResponse httpServletResponse,
            HttpServletRequest httpServletRequest,
            RedirectAttributes redirectAttributes
    ) {

        User userId = null;
        try{
            userId = userService.findByLocalId(localId);
        }catch (IllegalArgumentException e){
            redirectAttributes.addAttribute("message","아이디나 비밀번호가 올바르지 않습니다. 다시 시도해 주세요.");
            return "redirect:/signin";
        }

        CreateAccessTokenRequest request = new CreateAccessTokenRequest(userId.getUserId(), password, userId.getToken());
        try {
            tokenService.createAccessToken(httpServletResponse, request);

//            Cookie jwtCookie = new Cookie("jwt", accessToken.getAccessToken());
//            jwtCookie.setHttpOnly(true); // 쿠키에 접근 할수 있도록
//            jwtCookie.setPath("/"); // 모든 경로에서 쿠키에 접근 가능 하도록
//            httpServletResponse.addCookie(jwtCookie); // 응답에 쿠키 추가


//            return ResponseEntity.ok(response);

//            return "/users/success";

            return "redirect:/";

        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(403).body(null);
            redirectAttributes.addAttribute("message","아이디나 비밀번호가 올바르지 않습니다. 다시 시도해 주세요.");
            return "redirect:/signin";
        }
    }

    @GetMapping("/users/signout")
    public String logout(HttpServletResponse httpServletResponse,
                         HttpServletRequest httpServletRequest,
                         Authentication authentication) {
        // OAuth 로그아웃
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);
        }


        // 로컬 로그아웃 처리
        tokenService.resetToken(httpServletResponse);

        return "redirect:/";
    }

    @PostMapping("/users/signout")
    public String postLogout(HttpServletResponse httpServletResponse,
                             HttpServletRequest httpServletRequest,
                             Authentication authentication) {
        // OAuth 로그아웃
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);
        }


        // 로컬 로그아웃 처리
        tokenService.resetToken(httpServletResponse);

        return "redirect:/";
    }

    // 회원가입
    @GetMapping("/users/join")
    public String join() {
        return "users/join";
    }

    @PostMapping("/users/join")
    public String postJoin(@ModelAttribute("userData") AddUserRequest addUserRequest) {
        AddUserResponse request = userService.addUser(addUserRequest);
        log.info("AddUserRequest = {}", addUserRequest);
        return "redirect:/users/signin";

    }

    /*
     *  아이디/ 비번 찾기
     * */
    @GetMapping("/help/findUser")
    public String findUser() {
        return "users/help/findUser";
    }

    // 아이디 찾기
    @GetMapping("/help/findId")
    public String findId() {
        return "users/help/emailAuth";
    }

    // 아이디 확인
    @GetMapping("/help/idCheck")
    public String idCheck(@RequestParam("email") String email,
                          Model model) {
        String localId = userService.findByEmail(email).getLocalId();
        model.addAttribute("localId", localId);
        return "users/help/idCheck";
    }

    // 비밀번호 재설정
    @GetMapping("/help/passwordReset")
    public String passwordReset() {
        return "users/help/passwordReset";
    }

    @GetMapping("/help/mailCheckPW")
    public String mailCheckPW() {
        return "users/help/emailAuthPW";
    }

    // 비밀번호 변경
    @GetMapping("/help/passwordChange")
    public String pwChange(@RequestParam("localId") String localId,
                           Model model) {
        User user = userService.findByLocalId(localId);
        model.addAttribute("user", user);
        return "users/help/passwordChange";
    }

    @GetMapping("/help/userPWChange")
    public String passwordUpdate(@RequestParam("localId") String localId,
                                 @RequestParam("password") String password) {
        userService.passwordUpdate(localId, password);
        return "redirect:/signin";
    }


}
