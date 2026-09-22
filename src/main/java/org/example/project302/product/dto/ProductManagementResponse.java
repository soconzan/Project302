package org.example.project302.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.product.entity.DealStatus;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductManagementResponse {
    private Long userId;
    private Long pdId;
    private String pullUpDate;
    private Integer pullUpCnt;
    private String fileId;
    private DealStatus dealStatus;
    private String pdName;
    private Integer pdPrice;
    private Integer likeCount;

}
