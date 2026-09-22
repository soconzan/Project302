package org.example.project302.file.repository;

import org.example.project302.file.entity.File;
import org.example.project302.group.dto.FileSaveForm;
import org.example.project302.group.dto.GetGroupListResponse;
import org.example.project302.group.entity.Group;
import org.example.project302.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, String> {

    // 실례합니다
    Optional<File> findByProduct_PdIdAndThumbnailYnTrue(Long pdId);
    Optional<File> findByProductAndThumbnailYnTrue(Product product);
    Optional<File> findByProductAndThumbnailYnTrue(Group group);

    // 저도 실례합니다
    List<File> findByProduct_PdId(Long pdId);
}
