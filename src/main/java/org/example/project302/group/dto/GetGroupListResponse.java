package org.example.project302.group.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.product.entity.DealStatus;
import org.example.project302.product.entity.ProductStatus;

import java.text.NumberFormat;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetGroupListResponse {
    private Long univ_id;
    private Long pdId;
    private String pdName;
    private String pdContent;
    private Integer views;
    private Integer pdPrice;
    private LocalDateTime creatDate;
    private String pullUpDate;
    private Float latitude;
    private Float longitude;
    private String detailAddr;
    private String ctgrName;
    private Integer minPeople;
    private Integer maxPeople;
    private Boolean roundYn;
    private String closDate;
    private String fileId;
    private Boolean thumbnail_yn;
    private Float univLatitude;
    private Float univLongitude;
    private Integer chatCount;
    private DealStatus dealStatus;
    private String priceRange;

    public String getPriceRange() {
        int minPrice = pdPrice / maxPeople;
        int maxPrice = pdPrice / minPeople;
        if (roundYn) {
            minPrice = (int) (Math.ceil(minPrice / 100.0) * 100);
            maxPrice = (int) (Math.ceil(maxPrice / 100.0) * 100);
        }
        String formattedMinPrice = NumberFormat.getInstance().format(minPrice);
        String formattedMaxPrice = NumberFormat.getInstance().format(maxPrice);
        return "최소 " + formattedMinPrice + "원 ~ 최대 " + formattedMaxPrice + "원";
    }
}
