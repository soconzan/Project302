package org.example.project302.recommendation.controller;

import org.example.project302.recommendation.entity.UserInteraction;
import org.example.project302.recommendation.repository.UserInteractionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class InteractionController {
    private final UserInteractionRepository userInteractionRepository;

    public InteractionController(UserInteractionRepository userInteractionRepository) {
        this.userInteractionRepository = userInteractionRepository;
    }

    @PostMapping("/interaction/record")
    public ResponseEntity<Void> recordInteraction(@RequestBody UserInteraction userInteraction) {
        userInteractionRepository.save(userInteraction);
        return ResponseEntity.ok().build();
    }
}
