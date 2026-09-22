package org.example.project302.user.dto;

import lombok.AllArgsConstructor;
import org.example.project302.user.oauth2.OAuth2UserInfo;

import java.util.Map;

@AllArgsConstructor
public class GoogleUserDetails implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return "g_"+attributes.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getPhoneNumber() {
        return null;
    }

    @Override
    public String getBirthyear() {
        return null;
    }

    @Override
    public String getBirthday() {
        return null;
    }

    @Override
    public String getGender() {
        return null;
    }

    @Override
    public String getProfileImageUrl() {
        return null;
    }
}