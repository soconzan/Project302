package org.example.project302.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.project302.deal.service.ReviewService;
import org.example.project302.user.entity.User;
import org.example.project302.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;
    private final ReviewService reviewService;


    /*
     *  회원 프로필 view
     */
    @GetMapping("/{userId}")
    public String viewProfile(
            @PathVariable("userId") Long userId,
            Model model
    ) {
        // 후기 정보
        model.addAttribute("simpleReview", reviewService.getSimpleReview(userId));
        model.addAttribute("detailReviews", reviewService.getDetailReview(userId));
        return "users/profile/profile";
    }
}
