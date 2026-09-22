package org.example.project302.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccessTokenResponse {
    private String accessToken;
    private String refreshToken;
}
