package org.example.project302.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.file.entity.File;
import org.example.project302.file.service.FileService;
import org.example.project302.group.entity.Group;
import org.example.project302.product.entity.DealStatus;
import org.example.project302.product.entity.Product;
import org.example.project302.product.entity.ProductStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetChatProdResponse {
    private Long pdId;
    private String pdName;
    private int pdPrice;
    private ProductStatus pdStatus;
    private DealStatus dealStatus;
    private String dealStatusKr;
    private Long userId;
    private String nickname;
    private Integer minPrice;
    private Integer maxPrice;
    private String priceRange;
    private String fileLink;
    private LocalDateTime scheduleDate;
    private String userFileLink;

    public GetChatProdResponse(Product product, Group group, String fileLink, String userFileLink) {
        this.pdId = product.getPdId();
        this.pdName = product.getPdName();
        this.pdPrice = product.getPdPrice();
        this.pdStatus = product.getPdStatus();
        this.dealStatus = product.getDealStatus();
        this.dealStatusKr = product.getDealStatusKr();
        this.userId = product.getUser().getUserId();
        this.nickname = product.getUser().getNickname();
        this.minPrice = group != null ? group.getMinPeople() : null;
        this.maxPrice = group != null ? group.getMaxPeople() : null;
        this.priceRange = group != null ? group.getPriceRange() : null;
        this.fileLink = fileLink;
        this.scheduleDate = product.getScheduleDate();
        this.userFileLink = userFileLink;
    }
}
