package org.example.project302.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project302.user.dto.CustomOauth2UserDetails;
import org.example.project302.user.dto.GoogleUserDetails;
import org.example.project302.user.dto.KakaoUserDetails;
import org.example.project302.user.entity.User;
import org.example.project302.user.oauth2.OAuth2UserInfo;
import org.example.project302.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    private final UserOauthService userOauthService;
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}",oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = null;


        // kakao
        if (provider.equals("kakao")) {
            log.info("카카오 로그인");
            oAuth2UserInfo = new KakaoUserDetails(oAuth2User.getAttributes());
        }else if (provider.equals("google")) {
            log.info("구글 로그인");
            oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());
        }
        if (oAuth2UserInfo == null) {
            throw new OAuth2AuthenticationException("Provider ID not found");
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String phoneNumber = oAuth2UserInfo.getPhoneNumber();
        String birth = oAuth2UserInfo.getBirthyear()+oAuth2UserInfo.getBirthday();
        Character gender = (Objects.equals(oAuth2UserInfo.getGender(), "male"))?'M':'F';
        String profileImageUrl = oAuth2UserInfo.getProfileImageUrl();
        String snsType = oAuth2UserInfo.getProvider();
        String nickname = oAuth2UserInfo.getName();



        User findUser = userOauthService.findByLocalId(providerId);

        User user = null;
        if (findUser == null) {

            // 새로운 유저 생성
            user = new User(providerId,email,phoneNumber,birth,gender,snsType,nickname);

            // Add additional necessary properties initialization here

            userRepository.save(user);
        }else{
            user=findUser;
        }

        return new CustomOauth2UserDetails(user,oAuth2User.getAttributes());
    }
}
