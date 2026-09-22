package org.example.project302.user.dto;

import org.example.project302.user.oauth2.OAuth2UserInfo;

import java.util.HashMap;
import java.util.Map;

public class KakaoUserDetails implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    public KakaoUserDetails(Map<String, Object> attributes) {
        if (attributes == null) {
            throw new IllegalArgumentException("Attributes cannot be null");
        }
        this.attributes = new HashMap<>(attributes);
    }


    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return "k_"+attributes.get("id").toString();

    }

    // 이메일
    @Override
    public String getEmail() {
        return (String) ((Map) attributes.get("kakao_account")).get("email");
    }
    // 이름
    @Override
    public String getName() {
        return (String) ((Map) attributes.get("properties")).get("nickname");
    }
    // 전화번호
    @Override
    public String getPhoneNumber() {
        String phoneNumber = (String) ((Map<String, Object>) attributes.get("kakao_account")).get("phone_number");
        if (phoneNumber != null) {
            // 전화번호에서 모든 공백과 하이픈을 제거합니다.
            phoneNumber = phoneNumber.replaceAll("[ -]", "");

            // 국제 번호 '+82'를 '0'으로 대체합니다.
            if (phoneNumber.startsWith("+82")) {
                phoneNumber = "0" + phoneNumber.substring(3);
            }
        }
        return phoneNumber;
    }
    // 생년
    @Override
    public String getBirthyear() {
        return (String) ((Map) attributes.get("kakao_account")).get("birthyear");

    }
    // 월일
    @Override
    public String getBirthday() {
        return (String) ((Map) attributes.get("kakao_account")).get("birthday");
    }
    // 성별
    @Override
    public String getGender() {
        return (String)((Map) attributes.get("kakao_account")).get("gender");

    }
    // 프로필 사진
    @Override
    public String getProfileImageUrl() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount != null) {
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if (profile != null) {
                return (String) profile.get("profile_image_url");
            }
        }
        return null;  // Return null or default image URL if not available
    }
}
