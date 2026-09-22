package org.example.project302.user.oauth2;

public interface OAuth2UserInfo {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
    String getPhoneNumber();
    String getBirthyear();
    String getBirthday();
    String getGender();
    String getProfileImageUrl();
}
