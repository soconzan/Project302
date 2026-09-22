package org.example.project302.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.category.entity.Category;
import org.example.project302.file.entity.File;
import org.example.project302.user.entity.User;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "PRODUCTS")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert // null 값을 쿼리문에서 제외되도록 해서 default 값이 들어가게 한다
public class Product {

    // 상품 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pdId;

    // 상품 이름
    @Column(nullable = false)
    private String pdName;

    // 상품 내용
    @Column(nullable = false, length = 3000)
    private String pdContent;

    // 상품 가격
    @Column(nullable = false)
    private int pdPrice;

    // 등록 날짜
    @CreationTimestamp
    private LocalDateTime creatDate;

    // 끌어올리기 날짜
    @CreationTimestamp
    private LocalDateTime pullUpDate;

    // 끌어올리기 횟수
    @Column(nullable = false)
    @ColumnDefault("0")
    private int pullUpCnt;

    // 위도
    @Column(columnDefinition = "DECIMAL(9,6)", nullable = true)
    private Float latitude;

    // 경도
    @Column(columnDefinition = "DECIMAL(9,6)", nullable = true)
    private Float longitude;

    // 상세주소
    @Column(length = 255)
    private String detailAddr;

    // 상품 상태
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'GROUP'")
    private ProductStatus pdStatus;

    // 거래 완료 날짜
    private LocalDateTime completionDate;

    // 거래 상태
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'SELL'")
    private DealStatus dealStatus;

    // 거래 일정 날짜
    private LocalDateTime scheduleDate;

    // 조회수
    @Column(nullable = false)
    @ColumnDefault("0")
    private int views;

    // 카테고리 FK
    @ManyToOne
    @JoinColumn(name = "ctgr_id")
    private Category category;

    // 사용자 FK
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 데이터베이스에 저장되지 않는 형식 변환된 날짜
    @Transient
    private String formattedPullUpDate;

    @Transient
    private String fileId;

    public void pdUpdate(String pdName, String pdContent, int pdPrice, float longitude, float latitude, String detailAddr, Category category, int views) {
        this.pdName = pdName;
        this.pdContent = pdContent;
        this.pdPrice = pdPrice;
        this.longitude = longitude;
        this.latitude = latitude;
        this.detailAddr = detailAddr;
        this.category = category;
        this.views = views;
    }

    public void update(String pdName, String pdContent, int pdPrice, float longitude, float latitude, String detailAddr, DealStatus dealStatus, Category category, int views) {
        this.pdName = pdName;
        this.pdContent = pdContent;
        this.pdPrice = pdPrice;
        this.longitude = longitude;
        this.latitude = latitude;
        this.detailAddr = detailAddr;
        this.dealStatus = dealStatus;
        this.category = category;
        this.views = views;
    }

    public void ottUpdate(String pdName, String pdContent, int pdPrice, String detailAddr, DealStatus dealStatus, Category category, int views) {
        this.pdName = pdName;
        this.pdContent = pdContent;
        this.pdPrice = pdPrice;
        this.detailAddr = detailAddr;
        this.dealStatus = dealStatus;
        this.category = category;
        this.views = views;
    }

    public String getDealStatusKr() {
        switch (dealStatus) {
            case SELL:
                return "판매중";
            case RESV:
                return "예약중";
            case DEP:
                return "입금중";
            case BUY:
                return "구매중";
            case SEND:
                return "전달중";
            case SOLD:
                return "거래완료";
            case CANC:
                return "거래취소";
            default:
                return "숨김";
        }
    }
}
