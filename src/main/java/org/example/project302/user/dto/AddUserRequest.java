package org.example.project302.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddUserRequest {
    private String localId;   // 아이디
    private String password;  // 비밀번호
    private String email;     // 이메일
    private String phoneNo;   // 휴대전화
    private String nickname;  // 닉네임
    private String birth;     // 생년월일
    private Character gender; // 성별


    public AddUserRequest(String localId, String password) {
        this.localId = localId;
        this.password = password;
    }

}
