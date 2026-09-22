package org.example.project302.user.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project302.config.JwtProperties;
import org.example.project302.user.dto.CreateAccessTokenRequest;
import org.example.project302.user.dto.CreateAccessTokenResponse;
import org.example.project302.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenService {
    private final JWTokenProvider tokenProvider;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final JwtProperties jwtProperties;

    public CreateAccessTokenResponse createAccessToken(HttpServletResponse response, CreateAccessTokenRequest request) {

        log.info("refresh Token={}",request.getRefreshToken());
        User user = userService.findById(String.valueOf(request.getUserId()));
        // Create Access Token and refresh token by username and password
        if (request.getUserId() != null && request.getPassword() != null) {
            if (bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
                CreateAccessTokenResponse tokenResponse = new CreateAccessTokenResponse(
                        tokenProvider.createToken(user, Duration.ofMinutes(jwtProperties.getDuration())),
                        createRefreshToken(user));

                response.setHeader("Authorization", "Bearer "+ tokenResponse.getAccessToken());

                Cookie jwtCookie = new Cookie("jwt", tokenResponse.getAccessToken());
                Cookie refreshjwtCookie = new Cookie("refreshJwt", tokenResponse.getRefreshToken());
                    jwtCookie.setHttpOnly(true); // 쿠키에 접근 할수 있도록
                    jwtCookie.setPath("/"); // 모든 경로에서 쿠키에 접근 가능 하도록
                    response.addCookie(jwtCookie); // 응답에 쿠키 추가
                    refreshjwtCookie.setHttpOnly(true);
                    refreshjwtCookie.setPath("/");
                    response.addCookie(refreshjwtCookie);


                return tokenResponse;
            }else{
                throw new IllegalArgumentException("Invalid password");
            }
        } else if (request.getRefreshToken() != null) {

            if (tokenProvider.isValidToken((request.getRefreshToken()))) {

                return new CreateAccessTokenResponse(
                        tokenProvider.createToken(user, Duration.ofMinutes(jwtProperties.getDuration())),
                        null);
            }
        }

        throw new IllegalArgumentException("Invalid password ");
    }


    public String createRefreshToken(User user) throws IllegalArgumentException{ // refreshToken 생성

        String token = tokenProvider.createToken(user, Duration.ofHours(jwtProperties.getRefreshDuration()));
        user.setToken(token); // set refreshToken  (null,username,token)
        log.info("user refresh Token={}",token);
        refreshTokenService.saveToken(user);
        return token;
    }

    // 로그아웃시 해당 cookie를 삭제하도록 하자
    public void resetToken(HttpServletResponse response){
        Cookie jwtCookie = deleteCookie("jwt");
        Cookie refreshCookie = deleteCookie("refreshJwt");
        response.addCookie(refreshCookie);
        response.addCookie(jwtCookie);
    }

    public Cookie deleteCookie(String cookieName){
        Cookie token = new Cookie(cookieName,"");
        token.setHttpOnly(true);
        token.setMaxAge(0);
        token.setPath("/");
        return token;
    }


}
