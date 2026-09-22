package org.example.project302.product.repository;

import org.example.project302.product.dto.ProductSearchCond;
import org.example.project302.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductCustomRepository {
    Page<Product> findProducts(ProductSearchCond searchConditions, Pageable pageable);
    Page<Product> searchProductsByKeyword(String keyword, Long univId, Pageable pageable);
}
