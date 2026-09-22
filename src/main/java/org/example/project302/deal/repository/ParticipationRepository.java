package org.example.project302.deal.repository;

import org.example.project302.deal.entity.Participation;
import org.example.project302.deal.entity.ParticipationId;
import org.example.project302.product.entity.Product;
import org.example.project302.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, ParticipationId> {

    List<Participation> findAllByProduct(Product product);

    List<Participation> findAllByProduct_PdId(Long pdId);

    List<Participation> findAllByUser(User user);

    List<Participation> findAllByUserAndProduct_ScheduleDateIsNotNull(User user);

    Integer countAllByProduct(Product product);

    Integer countAllByProductAndDepositDateIsNotNull(Product product);

    Integer countAllByProductAndTakedDateIsNotNull(Product product);

    long countByProductAndUserIsNot(Product product, User user);

    void deleteAllByProduct(Product product);

    Optional<Participation> findByProductAndUser(Product product, User user);
    Optional<Participation> findByProductAndUserIsNot(Product product, User user);

    List<Participation> findAllByProductAndUserIsNot(Product product, User user);

}
