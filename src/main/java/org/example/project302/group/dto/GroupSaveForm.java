package org.example.project302.group.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.product.entity.Product;
import org.example.project302.user.entity.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupSaveForm {
    private int maxPeople;
    private int minPeople;
    private boolean roundYn;
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime closDate;
    private Long pdId;
    private User userId;
}
