package org.example.project302.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.project302.product.entity.Product;

@Entity
@Data
@Table(name = "chatrooms")
@AllArgsConstructor
@NoArgsConstructor
public class Chatroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "pd_id")
    private Product product;
}
