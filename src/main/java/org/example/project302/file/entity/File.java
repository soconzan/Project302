package org.example.project302.file.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.product.entity.Product;
import org.hibernate.annotations.ColumnDefault;

@Data
@Entity
@Table(name = "files")
@NoArgsConstructor
@AllArgsConstructor
public class File {
    @Id
    private String fileId;    // 파일 번호

    @Column
    private String filePath;    // 경로

    @Column
    @ColumnDefault("'false'")
    private boolean thumbnailYn; // 대표이미지 여부

    // 상품 FK
    @ManyToOne
    @JoinColumn(name = "pd_id")
    private Product product;

}
