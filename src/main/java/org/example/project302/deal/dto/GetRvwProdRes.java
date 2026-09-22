package org.example.project302.deal.dto;

import lombok.Data;
import org.example.project302.group.entity.Group;
import org.example.project302.product.entity.Product;
import org.example.project302.product.entity.ProductStatus;

import java.time.LocalDateTime;

@Data
public class GetRvwProdRes {
    private Long pdId;
    private String pdName;
    private int pdPrice;
    private String priceRange;
    private ProductStatus pdStatus;
    private LocalDateTime completionDate;
    private String fileLink;

    public GetRvwProdRes(Product product, Group group, String fileLink) {
        this.pdId = product.getPdId();
        this.pdName = product.getPdName();
        this.pdPrice = product.getPdPrice();
        this.priceRange = group != null ? group.getPriceRange() : null;
        this.pdStatus = product.getPdStatus();
        this.completionDate = product.getCompletionDate();
        this.fileLink = fileLink;
    }
}
