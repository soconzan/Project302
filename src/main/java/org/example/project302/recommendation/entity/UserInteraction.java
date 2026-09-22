package org.example.project302.recommendation.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_interaction")
public class UserInteraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long pdId;
    private String interactionType; // 찜하기, 상품클릭, 검색

    // interactionType 별 가중치 정의
    public static double getInteractionWeight(String interactionType) {
        switch (interactionType) {
            case "찜하기":
                return 1.5;
            case "상품클릭":
                return 1.0;
            case "검색":
                return 0.5;
            default:
                return 1.0;
        }
    }
}
