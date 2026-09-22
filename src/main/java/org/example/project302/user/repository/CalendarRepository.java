package org.example.project302.user.repository;

import org.example.project302.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Product, Long> {
    @Query(value = "select *\n" +
            "from products p \n" +
            "where pd_id in (select pd_id from participations p where user_id = :userId)\n" +
            "and schedule_date is not null;", nativeQuery = true)
    public List<Product> findByScheduleDateIsNotNull(@Param("userId") Long userId);


}
