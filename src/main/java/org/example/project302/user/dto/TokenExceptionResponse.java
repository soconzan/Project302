package org.example.project302.user.dto;

import lombok.Data;

// 예외 처리용 Dto
// 만료/비정상 토큰일떄 보낼 Response
@Data
public class TokenExceptionResponse {
    private String result;
}
