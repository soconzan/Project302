package org.example.project302.user.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

@Data
@Entity
@Table(name = "keywords")
public class Keywords {

    // 키워드 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kwrdId;

    // 키워드 내용
    @Column(nullable = false)
    private String kwrd;

    // 타입
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KeywordType type;

    // 사용자 FK
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
