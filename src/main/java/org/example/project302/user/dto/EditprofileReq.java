package org.example.project302.user.dto;

import lombok.Data;

@Data
public class EditprofileReq {
    private String phoneNo;  // 전화 번호
    private String nickname; // 닉네임
    private String birth; // 생년월일
    private String email; // 이메일
    private String bank; // 은행명
    private String accountNo; // 계좌번호
    private String userName; // 입금자명
}
