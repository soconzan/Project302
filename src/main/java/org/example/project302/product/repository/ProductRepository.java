package org.example.project302.product.repository;

import org.example.project302.product.entity.Product;
import org.example.project302.product.entity.ProductStatus;
import org.example.project302.univ.University;
import org.example.project302.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>, ProductCustomRepository {

    // 상품 한 건 조회(상품)
    @Query(value = "SELECT u.user_id, u.nickname, p.pd_id, p.pd_name, p.pd_content, p.pd_price, p.pd_status, p.creat_date, \n" +
            "      p.pull_up_date, p.views, p.latitude, p.longitude, p.detail_addr, c.ctgr_name,  COALESCE(count(l.like_pd), 0) AS like_count \n" +
            "            FROM users u, products p \n" +
            "            LEFT JOIN likes l ON p.pd_id = l.like_pd \n" +
            "            JOIN categories c ON c.ctgr_id = p.ctgr_id\n" +
            "            WHERE u.user_id = p.user_id \n" +
            "            AND p.pd_id = :id\n" +
            "            GROUP BY p.pd_id, p.pd_name, p.pd_price, p.creat_date, p.views", nativeQuery = true)
    public GetProduct findByProductId(@Param("id") Long id);

    // 조회수 업데이트
    @Modifying
    @Query(value = "UPDATE products SET views = views + 1 WHERE pd_id = :id ", nativeQuery = true)
    public int updateView(@Param("id") Long id);

    // 판매자 팔로워 수 가져오기
    @Query(value = "SELECT COALESCE(COUNT(l.like_user), 0) AS follower " +
            "FROM likes l " +
            "WHERE l.like_user = :userId", nativeQuery = true)
    public Integer countByUserId(@Param("userId") Long userId);

    // 판매자 상품 수 가져오기
    @Query(value = "SELECT COUNT(user_id)\n" +
            "FROM products\n" +
            "WHERE user_id = :userId\n" +
            "GROUP BY user_id;", nativeQuery = true)
    public Integer countProductByUserId(@Param("userId") Long userId);

    // 판매자 상품 리스트 가져오기
//    @Query(value = "SELECT u.user_id, p.pd_id, p.pd_name, p.deal_status, FORMAT(p.pd_price, 0) AS pd_price, p.pull_up_date, p.pull_up_cnt, f.file_id, \n" +
//            "\t\tCOALESCE(count(l.like_pd), 0) AS like_count  \n" +
//            "FROM users u, products p  \n" +
//            "LEFT JOIN files f ON p.pd_id = f.pd_id  \n" +
//            "LEFT JOIN likes l ON p.pd_id = l.like_pd  \n" +
//            "WHERE u.user_id = p.user_id  \n" +
//            "and u.user_id = :userId\n" +
//            "and f.thumbnail_yn = 1  \n" +
//            "and p.pd_status in ('USED', 'NEW')  \n" +
//            "GROUP BY p.pd_id, p.pd_name, p.deal_status, p.pd_price, p.pull_up_date, f.file_id \n" +
//            "ORDER BY p.pull_up_date DESC", nativeQuery = true)
//    public List<GetProductList> findProductByUserId(@Param("userId") Long userId);

    public List<Product> findAllByUserAndPdStatusNot(User user, ProductStatus pdStatus); // 등록자의 중고, 새상품인 상품만 가져옴

    //실례합니다
    public List<Product> findAllByUserAndPdStatus(User user, ProductStatus pdS);

    // 상품 필터링 결과
    /*@Query(value = "SELECT \n" +
            "    u.user_id, \n" +
            "    p.pd_id, \n" +
            "    p.pd_name, \n" +
            "    p.pd_price, \n" +
            "    p.creat_date,  \n" +
            "    CASE \n" +
            "        WHEN TIMESTAMPDIFF(SECOND, p.pull_up_date, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(SECOND, p.pull_up_date, NOW()), '초 전') \n" +
            "        WHEN TIMESTAMPDIFF(MINUTE, p.pull_up_date, NOW()) < 60 THEN CONCAT(TIMESTAMPDIFF(MINUTE, p.pull_up_date, NOW()), '분 전') \n" +
            "        WHEN TIMESTAMPDIFF(HOUR, p.pull_up_date, NOW()) < 24 THEN CONCAT(TIMESTAMPDIFF(HOUR, p.pull_up_date, NOW()), '시간 전') \n" +
            "        ELSE CONCAT(TIMESTAMPDIFF(DAY, p.pull_up_date, NOW()), '일 전') \n" +
            "    END AS pull_up_date, \n" +
            "    p.views, \n" +
            "    p.pd_status, \n" +
            "    f.file_id, \n" +
            "    COALESCE(count(l.like_pd), 0) AS like_count  \n" +
            "FROM \n" +
            "    users u \n" +
            "INNER JOIN \n" +
            "    universities univ ON u.user_id = univ.user_id\n" +
            "INNER JOIN \n" +
            "    products p ON u.user_id = p.user_id  \n" +
            "LEFT JOIN \n" +
            "    files f ON p.pd_id = f.pd_id  \n" +
            "LEFT JOIN \n" +
            "    likes l ON p.pd_id = l.like_pd  \n" +
            "WHERE \n" +
            "    univ.univ_id = :univId  \n" +
            "    AND f.thumbnail_yn = 1  \n" +
            "    AND p.pd_status IN ('USED', 'NEW') \n" +
            "    AND (:ctgrId IS NULL OR p.ctgr_id = :ctgrId) \n" +
            "    AND (:status IS NULL OR p.pd_status = :status) \n" +
            "GROUP BY \n" +
            "    p.pd_id, \n" +
            "    p.pd_name, \n" +
            "    p.pd_price, \n" +
            "    p.creat_date, \n" +
            "    p.pull_up_date, \n" +
            "    p.views, \n" +
            "    f.file_id  \n" +
            "ORDER BY \n" +
            "    :sorting", nativeQuery = true)
    List<GetProductListResponse> findProductByCtgrIdAndSortingAndPdStatus(@Param("ctgrId") int ctgrId,
                                                                          @Param("sorting") String sorting,
                                                                          @Param("pdStatus") ProductStatus pdStatus,
                                                                          @Param("univId") Long univId);*/

    // 실례합니다.
    List<Product> findAllByUser_UniversityAndAndPdStatusIsNot(University university, ProductStatus productStatus);
}
