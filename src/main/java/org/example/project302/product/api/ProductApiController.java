package org.example.project302.product.api;

import lombok.RequiredArgsConstructor;
import org.example.project302.product.dto.GetProductResponse;
import org.example.project302.product.entity.Product;
import org.example.project302.product.service.ProductService;
import org.example.project302.user.entity.User;
import org.example.project302.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductApiController {
    private final UserService userService;
    private final ProductService productService;

    /*
     *  학교별 중고거래 상품 조회
     */
    @GetMapping("")
    public ResponseEntity<List<GetProductResponse>> products(Principal principal) {
        User user = userService.findById(principal.getName());
        return ResponseEntity.ok(productService.products(user));
    }
}
