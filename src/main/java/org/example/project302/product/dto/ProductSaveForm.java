package org.example.project302.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.category.entity.Category;
import org.example.project302.product.entity.ProductStatus;
import org.example.project302.user.entity.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSaveForm {

    private Long pdId;
    private ProductStatus productStatus;    // USED, NEW
    private String pdName;
    private String pdContent;
    private int pdPrice;

    private Float longitude;
    private Float latitude;
    private String detailAddr;
    private Category category;
    private User user;
    private int views;
//    private List<File> file;
}
