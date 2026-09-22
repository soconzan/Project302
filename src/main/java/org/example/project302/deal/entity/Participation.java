package org.example.project302.deal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.product.entity.Product;
import org.example.project302.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "participations")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(ParticipationId.class)
public class Participation {
    @Id
    @ManyToOne
    @JoinColumn(name = "pd_id")
    private Product product;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime depositDate;
    private LocalDateTime takedDate;
    private LocalDateTime startDate;

    private Float latitudeNow;
    private Float longitudeNow;
}
