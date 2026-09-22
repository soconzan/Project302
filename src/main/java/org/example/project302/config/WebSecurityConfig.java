package org.example.project302.config;

import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.project302.user.dto.TokenExceptionResponse;
import org.example.project302.user.oauth2.handler.OAuth2LoginFailureHandler;
import org.example.project302.user.oauth2.handler.OAuth2LoginSuccessHandler;
import org.example.project302.user.service.CustomOauth2UserService;
import org.example.project302.user.service.JWTokenProvider;
import org.example.project302.user.service.RefreshTokenService;
import org.example.project302.user.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
    private final JWTokenProvider tokenProvider;
    private final RefreshTokenService refreshService;
    private final JwtProperties jwtProperties;
    private final CustomOauth2UserService customOauth2UserService;
    private final OAuth2LoginSuccessHandler successHandler;
    private final OAuth2LoginFailureHandler failureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
//                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(HttpMethod.GET,"/","/css/**", "/js/**", "/images/**","/uploads/**").permitAll()
                                .requestMatchers("/signin","/users/join","/users/signout","/join/**","/login/**","/error/**").permitAll()
                                .requestMatchers("/help/**").permitAll()
                                .requestMatchers("/api/**","/sqs/**").permitAll()
                                .requestMatchers("/admin").hasRole("ADMIN")
                                .anyRequest().authenticated()

                )
                .oauth2Login(oauth2 ->
                        oauth2
                                .userInfoEndpoint(userInfo -> userInfo.userService(customOauth2UserService))
                                .successHandler(successHandler)
                                .failureHandler(failureHandler)
                )
                .addFilterBefore(new TokenAuthenticationFilter(tokenProvider,refreshService,jwtProperties), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptions->
                        exceptions
                                .authenticationEntryPoint(jwtException()))
                ;
        return http.build();

    }


    @Bean
    AuthenticationManager authenticationManager(
            UserDetailsServiceImpl userDetailsService,
            PasswordEncoder bCryptPasswordEncoder
    ) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);

        return new ProviderManager(authenticationProvider);
    }

//     비밀번호 암호화
    @Bean
    PasswordEncoder passwordEncoder() {
        return new Md5PasswordEncoder();
    }

    // 401, 403 Error Handler
    private AuthenticationEntryPoint jwtException() {

        AuthenticationEntryPoint ap =  (request, response, authException)->{

            // 여기서 request.getAttribute를 확인하고, 토큰 만료 또는 유효하지 않은 경우 리다이렉트
            String tokenException = (String) request.getAttribute("TokenException");
            if (tokenException != null) {
                response.sendRedirect("/signin");  // 홈페이지로 리다이렉트
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                TokenExceptionResponse res = new TokenExceptionResponse();
                res.setResult(authException.getMessage());
                Gson gson = new Gson();
                response.getWriter().write(gson.toJson(res));
            }
        };
        return ap;
    }



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("X-Requested-with","Content-Type","Authorization","X-XSRF-token"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }
}

