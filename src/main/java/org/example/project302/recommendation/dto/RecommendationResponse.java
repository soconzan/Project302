package org.example.project302.recommendation.dto;

import lombok.Data;

@Data
public class RecommendationResponse {
    private Long pdId;
    private String pdName;

    public RecommendationResponse(Long pdId, String pdName) {
        this.pdId = pdId;
        this.pdName = pdName;
    }
}
