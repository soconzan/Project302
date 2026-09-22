package org.example.project302.group.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.file.entity.File;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetGroupOneResponse {
    // 닉네임
    private String nickname;
    // 대학 이름
    private Long univ_id;
    // 상품 아이디
    private Long pdId;
    // 상품 이름
    private String pdName;
    // 상품 상태
    private String dealStatus;
    // 상품 내용
    private String pdContent;
    // 가격
    private Integer pdPrice;
    // 생성날짜
    private LocalDateTime creatDate;
    // 끌어올린 날짜
    private LocalDateTime pullUpDate;
    // 조회수
    private Integer views;
    // 위도
    private Float latitude;
    // 경도
    private Float longitude;
    // 상세 주소
    private String detailAddr;
    // 카테고리 이름
    private String ctgrName;
    // 최소 인원
    private Integer minPeople;
    // 최대 인원
    private Integer maxPeople;
    // 가격 올림 여부
    private Boolean roundYn;
    private String dDay;
    // 마감 날짜
    private String closDate;
    // 파일
    private String fileId;
    // 대학 주소
    private String univAddr;
    // 찜
    private Integer likeCount;
    // 현재 참여자
    private Integer chatCount;
    private Long userId;

}
