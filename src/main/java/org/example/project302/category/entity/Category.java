package org.example.project302.category.entity;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "CATEGORIES")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ctgrId;

    @Column(length = 30)
    private String ctgrName;
}
