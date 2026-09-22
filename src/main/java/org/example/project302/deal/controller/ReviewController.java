package org.example.project302.deal.controller;

import lombok.RequiredArgsConstructor;
import org.example.project302.chat.service.ChatService;
import org.example.project302.deal.dto.ReviewForm;
import org.example.project302.deal.service.ReviewService;
import org.example.project302.user.entity.User;
import org.example.project302.user.service.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;


    /*
     *  후기 작성 폼
     */
    @GetMapping("")
    public String createReview(Model model,
                               @RequestParam(name = "pdId") Long pdId,
                               @RequestParam(name = "userId", required = false) Long userId,
                               Principal principal) {
        User user = userService.findById(principal.getName());
        model.addAttribute("product", reviewService.getProduct(pdId));
        model.addAttribute("receiver", userId == null
                ? reviewService.getReceiver(user, pdId)
                : reviewService.getReceiver(user, pdId, userId));
        return "reviews/form";
    }


    /*
    *  후기 등록
    */
    @PostMapping("")
    public String createReview(Principal principal,
                               ReviewForm reviewForm,
                               Model model) {
        User user = userService.findById(principal.getName());
        model.addAttribute("review", reviewService.saveReview(user, reviewForm));
        return "reviews/review";
    }
}
