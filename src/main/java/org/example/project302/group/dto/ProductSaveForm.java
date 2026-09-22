package org.example.project302.group.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.category.entity.Category;
import org.example.project302.product.entity.DealStatus;
import org.example.project302.product.entity.ProductStatus;
import org.example.project302.user.entity.User;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ProductSaveForm {

    private Long pdId;
    private String pdName;
    private String pdContent;
    private LocalDateTime creatDate;
    private LocalDateTime pullUpDate;
    private int pullUpCnt;
    private int pdPrice;
    private ProductStatus productStatus;
    private Float longitude;
    private Float latitude;
    private String detailAddr;
    private LocalDateTime completionDate;
    private LocalDateTime scheduleDate;
    private DealStatus dealStatus;
    private Category category;
    private int views;
    private User user;

//    @Builder
//    public ProductSaveForm(Long pdId, String pdName, String pdContent, LocalDateTime creatDate, LocalDateTime pullUpDate, int pullUpCnt, int pdPrice, ProductStatus productStatus, float longitude, float latitude, String detailAddr, LocalDateTime completionDate, LocalDateTime scheduleDate, DealStatus dealStatus, Category category, int views, User user){
//        this.pdId = pdId;
//        this.pdName = pdName;
//        this.pdContent = pdContent;
//        this.creatDate = creatDate;
//        this.pullUpDate = pullUpDate;
//        this.pullUpCnt = pullUpCnt;
//        this.pdPrice = pdPrice;
//        this.productStatus = productStatus;
//        this.longitude = longitude;
//        this.latitude = latitude;
//        this.detailAddr = detailAddr;
//        this.completionDate = completionDate;
//        this.scheduleDate = scheduleDate;
//        this.dealStatus = dealStatus;
//        this.category = category;
//        this.views = views;
//        this.user = user;
//    }


}
