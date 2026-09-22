package org.example.project302.product.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.project302.product.dto.GetProductListResponse;
import org.example.project302.product.dto.ProductSearchCond;
import org.example.project302.product.entity.DealStatus;
import org.example.project302.product.entity.Product;
import org.example.project302.product.entity.ProductStatus;
import org.example.project302.product.entity.QProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    // 상품 필터링 조회 (검색, 필터, 정렬)
    @Override
    public Page<Product> findProducts(ProductSearchCond searchConditions, Pageable pageable) {
        QProduct product = QProduct.product;
        String pdKeyword = searchConditions.getPdKeyword();
        QueryResults<Product> results = jpaQueryFactory.selectFrom(product)
                .where(
                        product.dealStatus.ne(DealStatus.HIDE),
//                        containsKeyword(product, totalKeyword),
                        containsKeyword(product, pdKeyword),
                        eqUnivId(product, searchConditions.getUnivId()),
                        eqCtgrId(product, searchConditions.getCtgrId()),
                        eqPdStatus(product, searchConditions.getPdStatus())

                )
                .offset(pageable.getOffset())   // 페이지 번호
                .limit(pageable.getPageSize())  // 페이지 사이즈 (최대 40개)
                .orderBy(getSortingOrder(product, searchConditions.getSorting()))
                .fetchResults();

        List<Product> products = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(products, pageable, total);
    }

    // 상품 검색어 조회(중고/공동구매) 부분
    public Page<Product> searchProductsByKeyword(String keyword, Long univId, Pageable pageable) {
        QProduct product = QProduct.product;

        List<Product> products = jpaQueryFactory.selectFrom(product)
                .where(
                        containsKeyword(product, keyword).and(eqUnivId(product, univId))
                                .and(product.pdStatus.ne(ProductStatus.GROUP)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory.selectFrom(product)
                .where(product.pdName.containsIgnoreCase(keyword)
                        .or(product.pdContent.containsIgnoreCase(keyword))
                        .and(product.pdStatus.ne(ProductStatus.GROUP)))
                .fetchCount();

        return new PageImpl<>(products, pageable, total);
    }

    // 대학별 상품 목록
    private BooleanExpression eqUnivId(QProduct product, Long univId) {
        return univId != null ? product.user.university.univId.eq(univId) : null;
    }

    // 상품 카테고리별
    private BooleanExpression eqCtgrId(QProduct product, Long ctgrId) {
        return ctgrId != null ? product.category.ctgrId.eq(ctgrId) : null;
    }

    // 상품 상태별
    private BooleanExpression eqPdStatus(QProduct product, String pdStatus) {
        if (pdStatus == null || pdStatus.equals("")) {
            return product.pdStatus.notIn(ProductStatus.GROUP); // 그룹 제외한 모든 상품 조회
        }
        try {
            ProductStatus status = ProductStatus.valueOf(pdStatus.toUpperCase());
            return product.pdStatus.eq(status);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // 상품 정렬 조건
    private OrderSpecifier<?> getSortingOrder(QProduct product, String sorting) {
        if (sorting == null) {
            return product.pullUpDate.desc(); // 기본 정렬 조건
        }

        switch (sorting) {
            case "price_asc":
                return product.pdPrice.asc();
            case "price_dsc":
                return product.pdPrice.desc();
            case "views":
                return product.views.desc();
            default:
                return product.pullUpDate.desc();
        }
    }

    // 상품 검색어
    private BooleanExpression containsKeyword(QProduct product, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null;
        }
        return product.pdName.containsIgnoreCase(keyword)
                .or(product.pdContent.containsIgnoreCase(keyword));
    }
}
