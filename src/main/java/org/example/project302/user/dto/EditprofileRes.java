package org.example.project302.user.dto;

import lombok.Data;
import org.example.project302.user.entity.User;

@Data
public class EditprofileRes {
    private Long userId; // 회원번호
    private String localId; // 아이디
    private String phoneNo;  // 전화 번호
    private String nickname; // 닉네임
    private String birth; // 생년월일
    private String email; // 이메일
    private boolean emailYn; // 대학 이메일 인증 여부
    private String bank; // 은행명
    private String accountNo; // 계좌번호
    private String userName; // 입금자명
    // 프로필 파일 명
    private String userFileId;
    // 파일 링크
    private String fileLink;

    public EditprofileRes(User user, String fileLink ){
        this.userId= user.getUserId();
        this.localId=user.getLocalId();
        this.phoneNo = user.getPhoneNo();
        this.nickname = user.getNickname();
        this.birth = user.getBirth();
        this.email = user.getEmail();
        this.emailYn = user.isEmailYn();
        this.bank = user.getBank();
        this.accountNo = user.getAccountNo();
        this.userName = user.getUserName();
        this.userFileId = user.getUserFileId();
        this.fileLink = fileLink;
    }

}
