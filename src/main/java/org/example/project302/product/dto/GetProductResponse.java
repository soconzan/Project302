package org.example.project302.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.product.entity.DealStatus;
import org.example.project302.product.entity.ProductStatus;
import org.example.project302.user.entity.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProductResponse {
    private String nickname;
    private Long pdId;
    private String pdName;
    private String pdContent;
    private Integer pdPrice;
    private LocalDateTime creatDate;
    private String pullUpDate;
    private Integer views;
    private String ctgrName;
    private String fileId;
    private Integer likeCount;
    private ProductStatus pdStatus;
    private Float latitude;
    private Float longitude;
    private String detailAddr;
    private DealStatus dealStatus;
    private Long userId;
}
