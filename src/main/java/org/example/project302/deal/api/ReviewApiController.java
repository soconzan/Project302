package org.example.project302.deal.api;

import lombok.RequiredArgsConstructor;
import org.example.project302.deal.dto.GetReviewRes;
import org.example.project302.deal.dto.ReviewForm;
import org.example.project302.deal.service.ReviewService;
import org.example.project302.user.entity.User;
import org.example.project302.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewApiController {
    private final ReviewService reviewService;
    private final UserService userService;



    /*
     *  후기 등록
     */
    @PostMapping("/save")
    public ResponseEntity<GetReviewRes> createReview(
            Principal principal,
            ReviewForm form) {
        User user = userService.findById(principal.getName());
        GetReviewRes reviewRes = reviewService.saveReview(user, form);
        return ResponseEntity.ok().build();
    }


}
