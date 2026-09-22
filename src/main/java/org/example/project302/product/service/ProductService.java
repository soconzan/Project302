package org.example.project302.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project302.file.entity.File;
import org.example.project302.file.service.FileService;
import org.example.project302.group.dto.ProductSaveForm;
import org.example.project302.file.repository.FileRepository;
import org.example.project302.product.dto.GetProductListResponse;
import org.example.project302.product.dto.GetProductResponse;
import org.example.project302.product.dto.ProductManagementResponse;
import org.example.project302.product.dto.ProductSearchCond;
import org.example.project302.product.entity.DealStatus;
import org.example.project302.product.entity.Product;
import org.example.project302.product.entity.ProductStatus;
import org.example.project302.product.repository.GetProduct;
import org.example.project302.product.repository.GetProductList;
import org.example.project302.product.repository.ProductRepository;
import org.example.project302.user.entity.User;
import org.example.project302.user.repository.UserRepository;
import org.example.project302.user.service.LikeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final LikeService likeService;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    private GetProductResponse productToEntity(GetProduct product) {
        return new GetProductResponse(
                product.getNickname(),
                product.getPd_id(),
                product.getPd_name(),
                product.getPd_content(),
                product.getPd_price(),
                product.getCreat_date(),
                formatRelativeDate(product.getPull_up_date()),
                product.getViews(),
                product.getCtgr_name(),
                fileService.getFileLink(product.getFile_id()),
                product.getLike_count(),
                product.getPd_status(),
                product.getLatitude(),
                product.getLongitude(),
                product.getDetail_addr(),
                product.getDeal_satus(),
                product.getUser_id()
        );
    }

    // 실례
    private GetProductResponse productToEntity(Product product) {
        return new GetProductResponse(
                product.getUser().getNickname(),
                product.getPdId(),
                product.getPdName(),
                product.getPdContent(),
                product.getPdPrice(),
                product.getCreatDate(),
                formatRelativeDate(product.getPullUpDate()),
                product.getViews(),
                product.getCategory().getCtgrName(),
                fileService.getFileLink(product),
                0, // 수정 필요
                product.getPdStatus(),
                product.getLatitude(),
                product.getLongitude(),
                product.getDetailAddr(),
                product.getDealStatus(),
                product.getUser().getUserId()
        );
    }

    // 상품 관리 목록 가져오기
    public ProductManagementResponse manageProductToEntity(Product product) {
        return new ProductManagementResponse(
                product.getUser().getUserId(),
                product.getPdId(),
                formatLocalDateTime(product.getPullUpDate()),
                product.getPullUpCnt(),
                fileService.getFileLink(product),
                product.getDealStatus(),
                product.getPdName(),
                product.getPdPrice(),
                likeService.cntLike(product.getPdId())
        );
    }

    // Product 저장 DTO
    private Product prodToEntity(ProductSaveForm productForm) {
        return new Product(
                null,
                productForm.getPdName(),
                productForm.getPdContent(),
                productForm.getPdPrice(),
                LocalDateTime.now(),
                null,
                0,
                productForm.getLatitude(),
                productForm.getLongitude(),
                productForm.getDetailAddr(),
                productForm.getProductStatus(),
                null,
                null,
                null,
                0,
                productForm.getCategory(),
                productForm.getUser(),
                null,
                null
        );
    }

    // Product 등록
    @Transactional
    public Long addProduct(ProductSaveForm productSaveForm, List<String> imgPaths) {
        Product product = prodToEntity(productSaveForm);
        Product result = productRepository.save(product);
        List<String> imgs = new ArrayList<>();
        File file = new File();
        for (String i : imgPaths) {
            if (imgPaths.get(0).equals(i)) {
                file = new File(i, null, true, result);
            } else {
                file = new File(i, null, false, result);
            }
            imgs.add(file.getFilePath());
            fileRepository.save(file);
        }
        return result.getPdId();
    }

    // Product 한 건 조회(상품) (클릭 시 조회수 업데이트)
    @Transactional
    public GetProductResponse getOneProduct(Long id) {
        int updated = productRepository.updateView(id);
        if (updated > 0) {
            return productToEntity(productRepository.findByProductId(id));
        } else return null;
    }

    // Product 한 건 조회(사진)
    public List<String> getProductFiles(Long pdId) {
        List<File> files = fileRepository.findByProduct_PdId(pdId);

        // File 객체를 FileSaveForm으로 변환하여 리스트에 추가
        List<String> fileSaveUrl = files.stream()
                .sorted(Comparator.comparing(File::isThumbnailYn).reversed())
                .map(file -> fileService.getFileLink(file.getFileId()))
                .collect(Collectors.toList());
        return fileSaveUrl;
    }

    // 상품 수정 페이지
    public GetProductResponse getOneProductUpdate(Long pdId) {
        return productToEntity(productRepository.findByProductId(pdId));
    }

    // 상품 수정
    @Transactional
    public void updateProduct(ProductSaveForm productSaveForm, Long pdId, List<String> imgPaths) {
        Product target = productRepository.findById(pdId).orElse(null);
        // 기존값 update
        target.pdUpdate(
                productSaveForm.getPdName(),
                productSaveForm.getPdContent(),
                productSaveForm.getPdPrice(),
                productSaveForm.getLongitude(),
                productSaveForm.getLatitude(),
                productSaveForm.getDetailAddr(),
                productSaveForm.getCategory(),
                productSaveForm.getViews()
        );
        Product pd = productRepository.save(target);

        List<File> file = fileRepository.findByProduct_PdId(target.getPdId());

        for (String i : imgPaths) {
            boolean thumb = i.equals(imgPaths.get(0));
            boolean exist = false;
            for (File existFile : file) {
                if (existFile.getFileId().equals(i)) {
                    exist = true;
                    existFile.setThumbnailYn(thumb);
                    fileRepository.save(existFile);
                    break;
                }
            }
            if (!exist) {
                File newFile = new File(i, null, thumb, pd);
                fileRepository.save(newFile);
            }
        }

        for (File f : file) {
            if (!imgPaths.contains(f.getFileId())) {
                fileRepository.delete(f);
                fileService.fileDelete(f.getFileId());
            }
        }

    }

    // product 삭제
    public Product deleted(Long pdId) {
        Product target = productRepository.findById(pdId).orElse(null);
        if (target == null) {
            return null;
        }
        productRepository.delete(target);
        return target;
    }

    // 끌어올리기 반영(3회까지)
    public Product pullUp(Long pdId) {
        Product product = productRepository.findById(pdId).orElse(null);
        if (product != null) {
            // 현재 pull_up_cnt 값 확인
            int pullUpCnt = product.getPullUpCnt();
            // pull_up_cnt가 3보다 작을 때만 업데이트 수행
            if (pullUpCnt < 3) {
                // pull_up_date를 현재 시간으로 업데이트
                product.setPullUpDate(LocalDateTime.now());
                // pull_up_cnt를 1 증가
                product.setPullUpCnt(pullUpCnt + 1);

                // 상품 엔티티 저장
                productRepository.save(product);
            }
        }
        return product;
    }

    // 상품 등록자 여부(등록자면 true, 아니면 false)
    public boolean isProductOwner(GetProductResponse product, User user) {
        return product.getUserId() == user.getUserId() ? true : false;
    }


    // 상품 수 가져오기
    public int getCntProduct(Long userId) {
        return productRepository.countProductByUserId(userId);
    }

    // 팔로워 수 가져오기
    public int getFollower(Long userId) {
        return productRepository.countByUserId(userId);
    }

    // 판매자 상품 전체 조회(상품관리)
    public List<ProductManagementResponse> getProductListByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저 없음"));
        return productRepository.findAllByUserAndPdStatusNot(user, ProductStatus.GROUP).stream()
                .map(this::manageProductToEntity)
                .collect(Collectors.toList());
    }


    // 판매 상태 반영(판매중, 예약중, 거래완료)
    public void updateDealStatus(Long productId, DealStatus newStatus) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품아이디: " + productId));
        product.setDealStatus(newStatus);
        productRepository.save(product);
    }

    // 시간 표기 메서드
    private String formatRelativeDate(LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        long seconds = ChronoUnit.SECONDS.between(date, now);
        if (seconds < 60) {
            return seconds + "초 전";
        }
        long minutes = ChronoUnit.MINUTES.between(date, now);
        if (minutes < 60) {
            return minutes + "분 전";
        }
        long hours = ChronoUnit.HOURS.between(date, now);
        if (hours < 24) {
            return hours + "시간 전";
        }
        long days = ChronoUnit.DAYS.between(date, now);
        return days + "일 전";
    }

    // 상품 관리 화면에서 보여지는 날짜 포맷팅
    private String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    // 상품 전체 조회 필터링
    public Page<Product> findProducts(ProductSearchCond searchConditions, Pageable pageable) {
        Page<Product> products = productRepository.findProducts(searchConditions, pageable);
        products.forEach(product -> {
            String formattedDate = formatRelativeDate(product.getPullUpDate());
            product.setFormattedPullUpDate(formattedDate); // 포매팅된 날짜를 설정

            // 썸네일 이미지 경로 설정
            product.setFileId(fileService.getFileLink(product)); // DTO로 설정
        });
        return products;
    }

    // 상품 검색어 조회 필터링
    public Page<Product> searchProducts(String keyword, Long univId, Pageable pageable) {
        Page<Product> products = productRepository.searchProductsByKeyword(keyword, univId, pageable);
        products.forEach(product -> {
            String formattedDate = formatRelativeDate(product.getPullUpDate());
            product.setFormattedPullUpDate(formattedDate);

            product.setFileId(fileService.getFileLink(product));
        });
        return products;
    }

    // 실례합니다

    public List<GetProductResponse> products(User user) {
        List<Product> products = productRepository.findAllByUser_UniversityAndAndPdStatusIsNot(user.getUniversity(), ProductStatus.GROUP);
        return products.stream()
                .map(product -> productToEntity(product))
                .collect(Collectors.toList());
    }
}












