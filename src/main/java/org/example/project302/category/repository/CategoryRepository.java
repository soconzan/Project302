package org.example.project302.category.repository;

import org.example.project302.category.entity.Category;
import org.example.project302.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
