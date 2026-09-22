package org.example.project302.user.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project302.user.dto.AppLoginUser;
import org.example.project302.user.dto.CreateAccessTokenRequest;
import org.example.project302.user.dto.CreateAccessTokenResponse;
import org.example.project302.user.entity.User;
import org.example.project302.user.service.TokenService;
import org.example.project302.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Slf4j
public class AppUserController {
    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/signin")
    public ResponseEntity<CreateAccessTokenResponse> appUserSignin(
            @RequestBody AppLoginUser appLoginUser,
            HttpServletResponse httpServletResponse,
            HttpServletRequest httpServletRequest,
            RedirectAttributes redirectAttributes
    ) {

        User userId = null;
        try{
            userId = userService.findByLocalId(appLoginUser.getLocalId());
        }catch (IllegalArgumentException e){
            redirectAttributes.addAttribute("message","아이디나 비밀번호가 올바르지 않습니다. 다시 시도해 주세요.");
            return ResponseEntity.status(403).body(null);
        }

        CreateAccessTokenRequest request = new CreateAccessTokenRequest(userId.getUserId(), appLoginUser.getPassword(), userId.getToken());
        try {

            CreateAccessTokenResponse accessToken = tokenService.createAccessToken(httpServletResponse, request);

//            Cookie jwtCookie = new Cookie("jwt", accessToken.getAccessToken());
//            jwtCookie.setHttpOnly(true); // 쿠키에 접근 할수 있도록
//            jwtCookie.setPath("/"); // 모든 경로에서 쿠키에 접근 가능 하도록
//            httpServletResponse.addCookie(jwtCookie); // 응답에 쿠키 추가

//            return ResponseEntity.ok(response);

//            return "/users/success";
            return ResponseEntity.ok(accessToken);

        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(403).body(null);
            redirectAttributes.addAttribute("message","아이디나 비밀번호가 올바르지 않습니다. 다시 시도해 주세요.");
            return ResponseEntity.status(403).body(null);
        }
    }


    public void principalCheck(Principal principal) {

        log.info("Principal = {}",principal.getName());
    }
}
