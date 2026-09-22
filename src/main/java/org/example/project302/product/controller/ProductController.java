package org.example.project302.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project302.category.entity.Category;
import org.example.project302.category.repository.CategoryRepository;
import org.example.project302.deal.dto.GetDetailRvwRes;
import org.example.project302.deal.service.ReviewService;
import org.example.project302.file.service.FileService;
import org.example.project302.group.dto.GetGroupListResponse;
import org.example.project302.group.dto.GetGroupOttListResponse;
import org.example.project302.group.dto.ProductSaveForm;
import org.example.project302.group.service.GroupService;
import org.example.project302.product.dto.GetProductResponse;
import org.example.project302.product.dto.ProductManagementResponse;
import org.example.project302.product.dto.ProductSearchCond;
import org.example.project302.product.entity.DealStatus;
import org.example.project302.product.entity.Product;
import org.example.project302.product.entity.ProductStatus;
import org.example.project302.product.service.ProductService;
import org.example.project302.recommendation.dto.RecommendationResponse;
import org.example.project302.recommendation.service.RecommendationService;
import org.example.project302.user.entity.User;
import org.example.project302.user.service.LikeService;
import org.example.project302.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final LikeService likeService;
    private final FileService fileService;
    private final ReviewService reviewService;
    private final GroupService groupService;
    private final RecommendationService recommendationService;

    // 상품 전체 조회
    @GetMapping("/products")
    public String findAllProducts(Principal user, Model model) {
        User logUser = userService.findById(user.getName());
        Long univId = logUser.getUniversity().getUnivId();

        ProductSearchCond searchConditions = new ProductSearchCond();
        searchConditions.setUnivId(univId);
        searchConditions.setSorting("date");
        searchConditions.setPdStatus(null); // null로 설정하여 모든 상품 상태를 포함
        searchConditions.setTotalKeyword(null);
        searchConditions.setPdKeyword(null);
        // 페이지 요청 생성, 한 페이지당 40개 항목
        Pageable pageable = PageRequest.ofSize(40);


        Page<Product> products = productService.findProducts(searchConditions, pageable);
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("currentPage", products.getNumber());
        model.addAttribute("totalPages", products.getTotalPages());


        return "product/products"; // 전체 페이지 반환
    }


    // 상품 필터링 조회
    @GetMapping("/products/filter")
    public String findProducts(
            @RequestParam(required = false) Long ctgrId,
            @RequestParam(required = false, defaultValue = "date") String sorting,
            @RequestParam(required = false) String pdStatus,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "") String totalKeyword,
            @RequestParam(required = false, defaultValue = "") String pdKeyword,
            Principal user,
            Model model,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {

        User logUser = userService.findById(user.getName());
        Long univId = logUser.getUniversity().getUnivId();

        ProductSearchCond searchConditions = new ProductSearchCond();
        searchConditions.setUnivId(univId);
        searchConditions.setCtgrId(ctgrId);
        searchConditions.setSorting(sorting);
        searchConditions.setPdStatus(pdStatus != null ? pdStatus.toUpperCase() : null);
        searchConditions.setTotalKeyword(totalKeyword);
        searchConditions.setPdKeyword(pdKeyword);

        // 페이지 인덱스가 0보다 작을 경우 0으로 설정
        page = page > 0 ? page - 1 : 0;

        // 페이지 요청 생성, 한 페이지당 40개 항목
        Pageable pageable = PageRequest.of(page, 40);
        Page<Product> productPage = productService.findProducts(searchConditions, pageable);

        Page<Product> products = productService.findProducts(searchConditions, pageable);
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalKeyword", totalKeyword);
        model.addAttribute("pdKeyword", pdKeyword);

        if ("XMLHttpRequest".equals(requestedWith)) {
            // AJAX 요청일 경우 프래그먼트만 반환
            return "product/_productList";
        } else {
            // 일반 요청일 경우 전체 페이지 반환
            return "product/products";
        }
    }

    @ModelAttribute("pdStatus")
    public ProductStatus[] productStatus() {
        return ProductStatus.values();
    }

    // product 등록 폼 화면
    @GetMapping("/products/create")
    public String newArticleForm(Model model, Principal user) {
        User logUser = userService.findById(user.getName());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("product", new Product());   // th:object 사용 위해 빈 product 객체 전달
        model.addAttribute("univ", logUser.getUniversity());
        return "product/create";
    }

    // product 등록
    @PostMapping("/products/save")
    public String createProduct(
            @RequestParam("ctgrId") Long ctgrId,
            ProductSaveForm productSaveForm,
            @RequestParam("imgList[]") List<MultipartFile> files,
            @RequestParam("pdStatus") String pdStatusString,
            Principal user
    ) throws IOException {
        log.info(productSaveForm.toString() + "productController");
        Category category = categoryRepository.findById(ctgrId).orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다"));
        ProductStatus pdStatus = ProductStatus.valueOf(pdStatusString);
        User logUser = userService.findById(user.getName());
        productSaveForm.setProductStatus(pdStatus);
        productSaveForm.setCategory(category);
        productSaveForm.setUser(logUser);
        List<String> imgPaths = fileService.s3Upload(files);
        productService.addProduct(productSaveForm, imgPaths);
        return "redirect:/products";
    }

    // 거래금지품목 페이지
    @GetMapping("/products/prohibitedItems")
    public String prohibitItems() {
        return "product/prohibitedItems";
    }

    // product 한 건 조회
    @GetMapping("/products/{pdId}")
    public String getProdOne(@PathVariable Long pdId, Principal user, Model model) {
        GetProductResponse product = productService.getOneProduct(pdId);
        List<String> files = productService.getProductFiles(pdId);
        User logUser = userService.findById(user.getName());    // 로그인 유저
        User selUser = userService.findById(String.valueOf(product.getUserId()));
        boolean isOwner = productService.isProductOwner(product, logUser);  // 상품 등록자인지 확인
        boolean isLiked = likeService.isLikedProduct(product, logUser); // 찜하기 여부 확인
        int follower = productService.getFollower(product.getUserId());
        int cntProduct = productService.getCntProduct(product.getUserId());
        List<GetDetailRvwRes> detailReviews = reviewService.getTop2DetailReviews(selUser.getUserId());
        Long cntReview = reviewService.getTotalReviewCount(selUser);
        List<RecommendationResponse> recommend = recommendationService.recommendItems(product.getUserId());


        model.addAttribute("product", product);
        model.addAttribute("files", files);
        model.addAttribute("isOwner", isOwner); // 판매자 여부
        model.addAttribute("user", logUser);    // 현재 로그인된 사용자
        model.addAttribute("liked", isLiked);   // 찜하기 여부
        model.addAttribute("selUser", selUser); // 판매자 정보
        model.addAttribute("follower", follower); // 판매자 팔로워 수
        model.addAttribute("cntProduct", cntProduct);   // 판매자 판매 상품 수
        model.addAttribute("detailReviews", detailReviews); // 거래 상세 후기 2개
        model.addAttribute("hasReviews", !detailReviews.isEmpty()); // 후기 여부 추가
        model.addAttribute("cntReview", cntReview); // 후기 총 갯수
        model.addAttribute("recommend", recommend); // 추천 상품 5개

        log.info("Product details for product {}: {}", pdId, product);
        log.info("Recommendations for product {}: {}", pdId, recommend);

        return "product/oneProduct";
    }

    // 상품 삭제
    @DeleteMapping("/products/{pdId}/delete")
    public ResponseEntity<Product> delProduct(@PathVariable Long pdId) {
        Product target = productService.deleted(pdId);
        return ResponseEntity.ok(target);
    }

    // 상품관리 페이지
    @GetMapping("/products/manage")
    public String manage(Principal user, Model model) {
        User logUser = userService.findById(user.getName());    // 로그인 유저
        Long userId = logUser.getUserId();
        List<ProductManagementResponse> products = productService.getProductListByUser(userId);
        model.addAttribute("products", products);
        model.addAttribute("userId", userId);
        return "product/manage";
    }

    // 중고 상품 프래그먼트 로드
    @GetMapping("/products/manage/{userId}/usedProducts")
    public String loadUsedProductsFragment(@PathVariable Long userId, Model model) {
        List<ProductManagementResponse> usedProducts = productService.getProductListByUser(userId);
        model.addAttribute("products", usedProducts);
        return "product/_productManage";
    }

    // 끌어올리기 반영
    @PatchMapping("/products/{pdId}/pullUp")
    public ResponseEntity<Product> pullUpProduct(@PathVariable Long pdId) {
        Product target = productService.pullUp(pdId);
        return ResponseEntity.ok(target);
    }

    // 판매 상태 반영
    @PostMapping("/products/{pdId}/dealStatus")
    public ResponseEntity<Product> dealStatus(@PathVariable Long pdId, @RequestBody Map<String, String> requestBody) {
        String status = requestBody.get("dealStatus");
        DealStatus dealStatus = DealStatus.valueOf(status);
        productService.updateDealStatus(pdId, dealStatus);
        return ResponseEntity.ok().build();
    }

    // 상품 수정 페이지
    @GetMapping("/products/updateForm/{pdId}")
    public String update(@PathVariable("pdId") Long pdId, Model model, Principal user) {
        User logUser = userService.findById(user.getName());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("product", productService.getOneProductUpdate(pdId));
        log.info(productService.getOneProductUpdate(pdId).getCtgrName());
        model.addAttribute("files", productService.getProductFiles(pdId));
        model.addAttribute("pdStatus", productStatus());

        log.info("pdStatus: " + Arrays.toString(productStatus()));
        log.info("이게 뭐냐면: " + productService.getOneProductUpdate(pdId));
        return "product/updateProduct";
    }

    // 상품 수정
    @PostMapping("/products/update/{pdId}")
    public String update(@PathVariable("pdId") Long pdId,
                         @RequestParam("ctgrId") Long ctgrId,
                         ProductSaveForm product,
                         @RequestParam("imgList[]") List<MultipartFile> files) throws IOException {
        List<String> imgPaths = fileService.s3Upload(files);
        Category category = categoryRepository.findById(ctgrId).orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다"));
        product.setCategory(category);
        productService.updateProduct(product, pdId, imgPaths);
        return "redirect:/products";
    }

    // 상품 검색 페이지(중고 / 공동구매 / OTT)
    @GetMapping("/products/search")
    public String searchProducts(@RequestParam("keyword") String keyword, Model model, Principal user) {
        // 중고
        User logUser = userService.findById(user.getName());
        Long userId = logUser.getUserId();
        Long univId = logUser.getUniversity().getUnivId();
        Pageable pageable = PageRequest.of(0, 20); // 부분 결과를 위해 5개만 가져오기(중고)
        Page<Product> products = productService.searchProducts(keyword, univId, pageable);
        // 공구
        Page<GetGroupListResponse> group = groupService.getGroupMainSearch(univId, pageable, keyword);
        // OTT
        Page<GetGroupOttListResponse> ott = groupService.getGroupOttList(univId, pageable, keyword);

        log.info(group.toString() + "머임");
        // 중고 model
        model.addAttribute("products", products.getContent());
        model.addAttribute("totalProducts", products.getTotalElements());
        model.addAttribute("productsEmpty", products.isEmpty());
        model.addAttribute("keyword", keyword);
        // 공구 model
        if (!group.isEmpty()) {
            GetGroupListResponse firstGroup = group.getContent().get(0);
            model.addAttribute("firstLati", firstGroup.getLatitude());
            model.addAttribute("firstLong", firstGroup.getLongitude());
        }
        model.addAttribute("groups", group);
        model.addAttribute("groupsEmpty", group.isEmpty());
        model.addAttribute("totalGroups", group.getTotalElements());
        // ott model
        model.addAttribute("otts", ott);
        model.addAttribute("ottsEmpty", ott.isEmpty());
        model.addAttribute("totalOtts", ott.getTotalElements());
        return "search/search";
    }

    // 상품 검색 전체 페이지(필터링 포함)
   /* @GetMapping("/products/fullSearch")
    public String fullSearch(@RequestParam(required = false) Long ctgrId,
                             @RequestParam(required = false, defaultValue = "date") String sorting,
                             @RequestParam(required = false) String pdStatus,
                             @RequestParam(required = false, defaultValue = "1") int page,
                             Principal user,
                             @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
                             @RequestParam("keyword") String keyword,
                             Pageable pageable,
                             Model model) {
        User logUser = userService.findById(user.getName());
        Long userId = logUser.getUserId();
        Long univId = logUser.getUniversity().getUnivId();
        Page<Product> products = productService.searchProducts(keyword, pageable, userId);

        ProductSearchCond searchConditions = new ProductSearchCond();
        searchConditions.setUnivId(univId);
        searchConditions.setCtgrId(ctgrId);
        searchConditions.setSorting(sorting);
        searchConditions.setPdStatus(pdStatus != null ? pdStatus.toUpperCase() : null);

        model.addAttribute("totalProducts", products.getTotalElements());
        model.addAttribute("products", products.getContent());
        model.addAttribute("keyword", keyword);
        return "productFullSearch";

    }*/


}