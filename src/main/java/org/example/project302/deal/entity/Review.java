package org.example.project302.deal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.product.entity.Product;
import org.example.project302.user.entity.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reviews")
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Enumerated(EnumType.STRING)
    private ReviewType reviewType;

    @Column
    private Long simpleReview;

    @Column(length = 3000)
    private String detailReview;

    @CreationTimestamp
    private LocalDateTime creatDate;

    @ManyToOne
    @JoinColumn(name = "pd_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
}
