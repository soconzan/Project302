package org.example.project302.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.category.entity.Category;
import org.example.project302.product.entity.Product;
import org.example.project302.product.entity.ProductStatus;

@Data
@AllArgsConstructor

@NoArgsConstructor
public class ProductSearchCond {
    private Long ctgrId;    // 카테고리
    private String sorting; // 정렬
    private String pdStatus;    // 상품 상태
    private String dealStatus;  // 거래 상태
    private Long univId;    // 대학별
    private String fileId;  // 사진
    private String totalKeyword; // 검색 키워드(전체)
    private String pdKeyword;   // 검색 키워드(중고)
}
