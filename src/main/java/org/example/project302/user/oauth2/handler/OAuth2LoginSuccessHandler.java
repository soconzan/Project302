package org.example.project302.user.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project302.config.JwtProperties;
import org.example.project302.user.dto.CustomOauth2UserDetails;
import org.example.project302.user.entity.User;
import org.example.project302.user.service.JWTokenProvider;
import org.example.project302.user.service.RefreshTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTokenProvider tokenProvider;
    private final JwtProperties jwtProperties;
    private final RefreshTokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공! = {}",authentication);

        // 로그인 성공 후 리디렉션
        User user = ((CustomOauth2UserDetails) authentication.getPrincipal()).getUser();

        String jwt = tokenProvider.createToken(user, Duration.ofMinutes(jwtProperties.getDuration()));
        String refreshJwt = tokenProvider.createToken(user, Duration.ofDays(jwtProperties.getRefreshDuration()));
        user.setToken(refreshJwt);
        tokenService.saveToken(user);

        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);
//        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        Cookie refreshJwtCookie = new Cookie("refreshJwt", refreshJwt);
        refreshJwtCookie.setHttpOnly(true);
//        refreshJwtCookie.setSecure(true);
        refreshJwtCookie.setPath("/");
        response.addCookie(refreshJwtCookie);

//        super.onAuthenticationSuccess(request, response, authentication);
        response.sendRedirect("/");

    }

}
