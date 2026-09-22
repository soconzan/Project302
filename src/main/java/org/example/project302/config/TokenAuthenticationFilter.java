package org.example.project302.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project302.user.entity.User;
import org.example.project302.user.service.JWTokenProvider;
import org.example.project302.user.service.RefreshTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

// JWT 용 AuthenticationFilter
@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter { // OncePerRequestFilter 요청당 한번만 호출

    private final JWTokenProvider tokenProvider;
    private final RefreshTokenService tokenService;
    private final JwtProperties jwtProperties;


    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {



        String token = getToken(request);
        String refreshToken = getRefreshToken(request);


        if(token!=null) {
            if(tokenProvider.isValidToken(token)) {
                // SecurityContext에 Authentication 객체 반환
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else if (refreshToken !=null && tokenProvider.isValidToken(refreshToken)) { // refresh token으로 확인
                String userId = tokenProvider.getClaims(refreshToken).getSubject();
                User user = tokenService.findUser(userId);

                // accessToken 다시 발급
                String newToken = tokenProvider.createToken(user, Duration.ofMinutes(jwtProperties.getDuration()));

                Authentication authentication = tokenProvider.getAuthentication(newToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("new toke={}",newToken);
                setToken(response, newToken);

            }
        else {
                // 401
                request.setAttribute("TokenException", "Invalid Token 니토큰 이상함");
            }
        }
        else {
            request.setAttribute("TokenException", "Token Required 필요띠");
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTH_HEADER);

        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    String token = cookie.getValue();
                    return token;
                }
            }
        }else if(authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length()); // TOKEN_PREFIX.length() 인덱스에서부터 잘라옴
        }


        return null;
    }

    private void setToken(HttpServletResponse response, String token) {
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true); // 쿠키에 접근 할수 있도록
        jwtCookie.setPath("/"); // 모든 경로에서 쿠키에 접근 가능 하도록
        response.addCookie(jwtCookie); // 응답에 쿠키 추가
        log.info("newTokenCookie = {}",token);
    }

    private String getRefreshToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshJwt")) {
                    String refreshToken = cookie.getValue();
                    return refreshToken;
                }
            }
        }
        return null;
    }
}
