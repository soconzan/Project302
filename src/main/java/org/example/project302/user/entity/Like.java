package org.example.project302.user.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.project302.product.entity.Product;

@Entity
@Data
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "like_user")
    private User likeUser;

    @ManyToOne
    @JoinColumn(name = "like_pd")
    private Product likePd;
}