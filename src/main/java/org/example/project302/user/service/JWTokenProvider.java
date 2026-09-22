package org.example.project302.user.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.project302.config.JwtProperties;
import org.example.project302.user.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.*;

@RequiredArgsConstructor
@Service
// 토큰 생성, 검증 해줄 Service
public class JWTokenProvider {
    private final JwtProperties jwtProperties;
    private SecretKey key;
    private JwtParser parser;

    public String createToken(User user, Duration expiredAt) { // 토큰 생성
        Date now = new Date();
        Date exp = new Date(now.getTime()+expiredAt.toMillis());

        return Jwts.builder()
                .header().add(getHeader()).and()          //Header 설정
                .claims()                                 // Payload에 claims 설정
                    .issuedAt(now)                            // 토큰 발급시간 iat
                    .issuer(jwtProperties.getIssuer())        //토큰 발급자 iss
                    .subject(String.valueOf(user.getUserId()))              // 토큰 제목 sub userId로
                    .expiration(exp)                          // 토큰 만료시간 exp
                    .add("role", user.getRole() )           // 'role' Claim 추가
                    .add("nick",user.getNickname()).and()     // "nick" Claim 추가 닉네임
                .signWith(getKey(), Jwts.SIG.HS256)       // Signature 에 Secret Key
                .compact();
    }

    // 토큰 유요한지
    public boolean isValidToken(String token){
        if(parser == null) {
            parser = Jwts.parser().verifyWith(getKey()).build();
        }
        try {
            parser.parseSignedClaims(token);
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    // 토큰 정보 가져옴
    public Claims getClaims(String token) {
        if(parser == null) {
            parser = Jwts.parser().verifyWith(getKey()).build();
        }
        try {
            Jws<Claims> jws = parser.parseSignedClaims(token);
            return jws.getPayload();
        }catch(Exception e) {
            return null;
        }
    }


    public Authentication getAuthentication(String token) { // 토큰 주고 인증 받기

        Claims claims = getClaims(token);
        String role = claims.get("role", String.class);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_"+role));

        UserDetails userDetails =  org.springframework.security.core.userdetails.User
                .withUsername(claims.getSubject())
                .password(claims.getSubject())
                .roles(role)
                .build();

        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    // SecretKey
    private SecretKey getKey() {
        if(key==null) {
            key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtProperties.getSecretKey()));
        }
        return key;
    }

    // Header
    private Map<String, Object> getHeader(){
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");   // 토큰 타입
        header.put("alg", "HS256"); // 전자서명시 사용 알고리즘
        return header;
    }





}
