package org.example.project302.recommendation.controller;

import org.example.project302.recommendation.dto.RecommendationResponse;
import org.example.project302.recommendation.service.RecommendationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/recommend")
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    public List<RecommendationResponse> getRecommendations(@PathVariable Long userId) {
        return recommendationService.recommendItems(userId);
    }
}
