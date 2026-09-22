package org.example.project302.product.entity;


import lombok.Getter;

@Getter
public enum ProductStatus {
    USED("중고"), NEW("새상품"), GROUP("공동구매");
    private final String description;

    ProductStatus(String description) {
        this.description = description;
    }
}
