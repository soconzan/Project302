package org.example.project302.user.dto;

import lombok.Data;
import org.example.project302.product.entity.DealStatus;
import org.example.project302.product.entity.Product;
import org.example.project302.product.entity.ProductStatus;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Data
public class UserScheduleRes {
    private Long pdId; // 상품 아이디
    private Long userId; // 판매자 아이디
    private ProductStatus pdStatus; // 미개봉, 중고, 공구
    private String pdName; // 상품 이름
    private Float latitude; // 위도
    private Float longitude; // 경도
    private String detailAddr;  // 상세 주소
    private DealStatus dealStatus; // 거래 상태
    private LocalDateTime completionDate; // 거래 완료 날짜
    private LocalDateTime scheduleDate;  // 거래 일정 날짜
    private String fileLink; // 대표이미지 파일링크
    private String price; // 상품 가격


    public UserScheduleRes(Product product, String fileLink) {
        this.pdId= product.getPdId();
        this.userId = product.getUser().getUserId();
        this.pdStatus = product.getPdStatus();
        this.pdName = product.getPdName();
        this.latitude = product.getLatitude();
        this.longitude = product.getLongitude();
        this.detailAddr = product.getDetailAddr();
        this.dealStatus = product.getDealStatus();
        this.completionDate = product.getCompletionDate();
        this.scheduleDate = product.getScheduleDate();
        this.fileLink = fileLink;
        this.price = new DecimalFormat("#,###원").format(product.getPdPrice());
    }

}
