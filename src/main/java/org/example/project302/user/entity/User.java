package org.example.project302.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.univ.University;
import org.example.project302.user.dto.AddUserRequest;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    // 회원번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // 아이디
    private String localId;

    // 소셜 아이디
    private String snsId;

    // 소셜 타입
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'LOCAL'")
    private SnsType snsType;

    // 비밀번호
    private String password;

    // 휴대전화
    @Column(length = 11)
    private String phoneNo;

    // 닉네임
    private String nickname;

    // 생년월일
    @Column(length = 8)
    private String birth;

    // 성별
    @ColumnDefault("0")
    private Character gender;

    // 이메일
    private String email;

    // 이메일 인증 여부
    @ColumnDefault("0")
    private boolean emailYn;

    // 마지막 접속 시간
    @CreationTimestamp
    private LocalDateTime lastConectTime;

    // 매너점수
    @Column(nullable = true)
    private Float score;

    // 가입일
    @CreationTimestamp
    private LocalDateTime subscribeDate;

    // 소속대학교
    @ManyToOne
    @JoinColumn(name="univ_id")
    private University university;

    // 은행명
    @Column(length = 100)
    private String bank;

    // 계좌번호
    @Column(length = 20)
    private String accountNo;

    // 입금자명
    @Column(length = 64)
    private String userName;

    // 리프레쉬 토큰
    private String token;

    // 권한
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'USER'")
    private Role role;

    // 프로필 파일 번호
    private String userFileId;
    // 프로필 파일 경로
    private String userFilePath;

    // fcm token
    private String fcmToken;

    public User(String localId, String password, Role role) {
        this.localId = localId;
        this.password = password;
        this.role = role;
    }


    // dto -> Entity
    public User(AddUserRequest dto) {
        this.localId = dto.getLocalId();
        this.password = dto.getPassword();
        this.role = Role.USER;
        this.birth = dto.getBirth();
        this.email = dto.getEmail();
        this.gender = dto.getGender();
        this.nickname = dto.getNickname();
        this.phoneNo = dto.getPhoneNo();
        this.snsType = SnsType.LOCAL;
//        this.univId = 517L;
        this.emailYn=false;
    }

    // Oauth 로그인 유저 생성
    public User(String providerId, String email, String phoneNumber, String birth, Character gender, String snsType, String nickname) {
        this.localId = providerId;;
        this.email = email;
        this.phoneNo = phoneNumber;
        if(!birth.equals("nullnull")){
            this.birth = birth;
        }
        this.gender = gender;
        this.nickname = nickname;

        this.snsType = (Objects.equals(snsType, "kakao"))?SnsType.KAKAO:SnsType.GOOGLE;

        this.role = Role.USER;
    }

}
