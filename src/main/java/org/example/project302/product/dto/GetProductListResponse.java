package org.example.project302.product.dto;

import jakarta.persistence.Transient;
import lombok.*;
import org.example.project302.product.entity.DealStatus;
import org.example.project302.product.entity.ProductStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProductListResponse {
    private Long userId;
    private Long univ_id;
    private Long pdId;
    private String pdName;
    private String pdPrice;
    private LocalDateTime creatDate;
    private String pullUpDate;
    private Integer pullUpCnt;
    private Integer views;
    private String ctgrName;
    private String fileId;
    private Integer likeCount;
    private DealStatus dealStatus;
    private ProductStatus pdStatus;
}
