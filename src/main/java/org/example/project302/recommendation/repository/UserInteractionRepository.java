package org.example.project302.recommendation.repository;

import org.example.project302.recommendation.entity.UserInteraction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInteractionRepository extends JpaRepository<UserInteraction, Long> {
    List<UserInteraction> findByUserId(Long userId);
}
