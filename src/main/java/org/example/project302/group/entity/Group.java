package org.example.project302.group.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.project302.product.entity.Product;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import java.io.Serializable;
import java.text.NumberFormat;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Table(name = "GROUP_DETAILS")
public class Group {

    @Id
    private Long pdId;

    //    @Id
    @OneToOne()
    @JoinColumn(name = "pd_id")
    private Product product;

    // 최저 인원
    @Column(name = "MIN_PEOPLE", nullable = false)
    private int minPeople;

    // 최대 인원
    @Column(name = "MAX_PEOPLE", nullable = false)
    private int maxPeople;

    // 가격 올림 여부
    @Column(name = "ROUND_YN", nullable = false)
    private boolean roundYn;

    // 마감일
    @Column(name = "CLOS_DATE", nullable = false)
    private LocalDateTime closDate;

    @Transient
    private Integer chatCount;

    @Transient
    private String fileId;

    public String getPriceRange() {
        int minPrice = product.getPdPrice() / maxPeople;
        int maxPrice = product.getPdPrice() / minPeople;
        if (roundYn) {
            minPrice = (int) (Math.ceil(minPrice / 100.0) * 100);
            maxPrice = (int) (Math.ceil(maxPrice / 100.0) * 100);
        }
        String formattedMinPrice = NumberFormat.getInstance().format(minPrice);
        String formattedMaxPrice = NumberFormat.getInstance().format(maxPrice);
        return formattedMinPrice + "원 ~ " + formattedMaxPrice + "원";
    }

    public void update(int minPeople, int maxPeople, boolean roundYn, LocalDateTime closDate){
        this.minPeople = minPeople;
        this.maxPeople = maxPeople;
        this.roundYn = roundYn;
        this.closDate = closDate;
    }
}
