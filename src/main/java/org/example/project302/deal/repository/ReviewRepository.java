package org.example.project302.deal.repository;

import org.example.project302.deal.entity.Review;
import org.example.project302.product.entity.Product;
import org.example.project302.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByProductAndSender(Product product, User user);
    boolean existsByProductAndReceiver(Product product, User user);

    long countByProductAndSender(Product product, User sender);

    Optional<Review> findByProductAndSenderAndReceiver(Product product, User sender, User receiver);

    Long countByReceiver(User user);

    List<Review> findAllByReceiver(User user);

    List<Review> findAllByReceiverOrderByCreatDateDesc(User user);
}
