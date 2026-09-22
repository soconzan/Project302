package org.example.project302.group.repository;

import org.example.project302.group.dto.GetGroupOttListResponse;
import org.example.project302.group.entity.Group;
import org.example.project302.product.entity.Product;
import org.example.project302.product.entity.ProductStatus;
import org.example.project302.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {

    /*AND f.thumbnail_yn = 1*/
    // 전체조회
    @Query(value = "SELECT\n" +
            "    p.pd_id,\n" +
            "    p.pd_name,\n" +
            "    p.pd_content,\n" +
            "    p.pd_price,\n" +
            "    p.creat_date,\n" +
            "    p.pull_up_date,\n" +
            "    p.latitude,\n" +
            "    p.longitude,\n" +
            "    p.views,\n" +
            "    p.deal_status,\n" +
            "    c.ctgr_name,\n" +
            "    g.min_people,\n" +
            "    g.max_people,\n" +
            "    g.round_yn,\n" +
            "    TIMESTAMPDIFF(DAY, NOW(), g.clos_date) as clos_date,\n" +
            "    f.file_id,\n" +
            "    f.thumbnail_yn,\n" +
            "    un.univ_addr,\n" +
            "    un.univ_latitude,\n" +
            "    un.univ_longitude,\n" +
            "    COALESCE(COUNT(cp.chat_id), 0) AS chat_count,\n" +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) AS distance\n" +
            "FROM\n" +
            "    users u\n" +
            "        JOIN\n" +
            "    products p ON u.user_id = p.user_id\n" +
            "        JOIN\n" +
            "    categories c ON c.ctgr_id = p.ctgr_id\n" +
            "        LEFT JOIN\n" +
            "    group_details g ON p.pd_id = g.pd_id\n" +
            "        LEFT JOIN\n" +
            "     (select * from files where thumbnail_yn = 1) f\n" +
            "        on f.pd_id = p.pd_id " +
            "        LEFT JOIN\n" +
            "    chatrooms cr ON p.pd_id = cr.pd_id\n" +
            "        LEFT JOIN\n" +
            "    chat_participations cp ON cp.chat_id = cr.chat_id\n" +
            "        JOIN\n" +
            "    universities un ON u.univ_id = un.univ_id\n" +
            "WHERE\n" +
            " un.univ_id = :univ_id\n" +
            "  AND p.pd_status = 'GROUP'\n" +
            " AND (:ctgr_id IS NULL OR c.ctgr_id = :ctgr_id)\n" +
            " AND g.clos_date > now()\n" +
            " AND (:keyword IS NULL OR p.pd_name LIKE CONCAT('%', :keyword, '%') OR p.pd_content LIKE CONCAT('%', :keyword, '%'))\n" +
            "GROUP BY\n" +
            "    p.pd_id, p.pd_name, p.pd_content, p.pd_price, p.creat_date, p.pull_up_date,\n" +
            "    p.latitude, p.longitude, c.ctgr_name, g.min_people, g.max_people,\n" +
            "    g.round_yn, g.clos_date, f.file_id, un.univ_addr\n" +
            "HAVING\n" +
            "    distance <= :distance",
            countQuery = "SELECT COUNT(*) FROM (SELECT p.pd_id FROM products p JOIN users u ON u.user_id = p.user_id JOIN categories c ON c.ctgr_id = p.ctgr_id LEFT JOIN group_details g ON p.pd_id = g.pd_id LEFT JOIN files f ON f.pd_id = p.pd_id LEFT JOIN chatrooms cr ON p.pd_id = cr.pd_id LEFT JOIN chat_participations cp ON cp.chat_id = cr.chat_id JOIN universities un ON u.univ_id = un.univ_id WHERE un.univ_id = :univ_id AND p.pd_status = 'GROUP' GROUP BY p.pd_id) as temp",
            nativeQuery = true)
    public Page<GetGroupList> findByGroup(@Param("univ_id") Long univ_id, Pageable pageable, @Param("latitude") Float latitude, @Param("longitude") Float longitude, @Param("distance") int distance, @Param("ctgr_id") Long ctgr_id, @Param("keyword") String keyword);

    // OTT 조회
    @Query(value = "SELECT\n" +
            "    p.pd_id,\n" +
            "    p.pd_name,\n" +
            "    p.pd_content,\n" +
            "    p.pd_price,\n" +
            "    p.creat_date,\n" +
            "    p.pull_up_date,\n" +
            "    p.views,\n" +
            "    c.ctgr_name,\n" +
            "    g.min_people,\n" +
            "    g.max_people,\n" +
            "    g.round_yn,\n" +
            "    g.clos_date,\n" +
            "    un.univ_addr,\n" +
            "    TIMESTAMPDIFF(DAY, NOW(), g.clos_date) as d_day,\n" +
            "    COALESCE(COUNT(cp.chat_id), 0) AS chat_count\n" +
            "FROM\n" +
            "    users u\n" +
            "        JOIN\n" +
            "    products p ON u.user_id = p.user_id\n" +
            "        JOIN\n" +
            "    categories c ON c.ctgr_id = p.ctgr_id\n" +
            "        LEFT JOIN\n" +
            "    group_details g ON p.pd_id = g.pd_id\n" +
            "        left JOIN\n" +
            "    chatrooms cr ON p.pd_id = cr.pd_id\n" +
            "        LEFT JOIN\n" +
            "    chat_participations cp ON cp.chat_id = cr.chat_id\n" +
            "        JOIN\n" +
            "    universities un ON u.univ_id = un.univ_id\n" +
            "where un.univ_id = :univ_id\n" +
            "  AND p.pd_status = 'GROUP'\n" +
            "  and g.clos_date > now()\n" +
            "  and c.ctgr_id = 12 \n" +
            " AND (:keyword IS NULL OR p.pd_name LIKE CONCAT('%', :keyword, '%') OR p.pd_content LIKE CONCAT('%', :keyword, '%'))\n" +
            "GROUP BY\n" +
            "    p.pd_id, p.pd_name, p.pd_content, p.pd_price, p.creat_date, p.pull_up_date,\n" +
            "    p.latitude, p.longitude, c.ctgr_name, g.min_people, g.max_people,\n" +
            "    g.round_yn, g.clos_date, un.univ_addr\n" +
            "ORDER BY\n" +
            "    p.creat_date",
            countQuery = "SELECT COUNT(p.pd_id) FROM users u JOIN products p ON u.user_id = p.user_id JOIN categories c ON c.ctgr_id = p.ctgr_id LEFT JOIN group_details g ON p.pd_id = g.pd_id left JOIN chatrooms cr ON p.pd_id = cr.pd_id LEFT JOIN chat_participations cp ON cp.chat_id = cr.chat_id JOIN universities un ON u.univ_id = un.univ_id where un.univ_id = :univ_id AND p.pd_status = 'GROUP' and g.clos_date > now() and c.ctgr_id = 12",
            nativeQuery = true)
    public Page<GetGroupOttList> findByGroupOtt(@Param("univ_id") Long univ_id, @Param("keyword") String keyword,  Pageable pageable);


    // 한 건 조회
    @Query(value = "SELECT u.user_id,\n" +
            "    p.pd_id, \n" +
            "    p.pd_name, \n" +
            "    p.pd_content, \n" +
            "    p.pd_price, \n" +
            "    p.creat_date, \n" +
            "    p.pull_up_date,\n" +
            "    p.latitude, \n" +
            "    p.longitude, \n" +
            "    p.views, \n" +
            "    p.detail_addr, \n" +
            "    p.deal_status, \n" +
            "    c.ctgr_name,\n" +
            "    g.min_people, \n" +
            "    g.max_people, \n" +
            "    g.round_yn, \n" +
            "    g.clos_date, \n" +
            "    TIMESTAMPDIFF(DAY, NOW(), g.clos_date) as d_day,\n" +
            "    un.univ_addr, \n" +
            "    u.nickname, \n" +
            "    COALESCE(COUNT(DISTINCT l.like_pd), 0) AS like_count,\n" +
            "    COALESCE(COUNT(cp.chat_id), 0) AS chat_count\n" +
            "FROM \n" +
            "    users u\n" +
            "JOIN \n" +
            "    products p ON u.user_id = p.user_id\n" +
            "JOIN \n" +
            "    categories c ON c.ctgr_id = p.ctgr_id\n" +
            "LEFT JOIN \n" +
            "    group_details g ON p.pd_id = g.pd_id\n" +
            "LEFT JOIN \n" +
            "    likes l ON p.pd_id = l.like_pd\n" +
            "LEFT JOIN \n" +
            "    chatrooms cr ON p.pd_id = cr.pd_id\n" +
            "LEFT JOIN \n" +
            "    chat_participations cp ON cp.chat_id = cr.chat_id\n" +
            "JOIN \n" +
            "    universities un ON u.univ_id = un.univ_id\n" +
            "WHERE \n" +
            "    un.univ_id = :univ_id\n" +
            "    AND p.pd_id = :id\n" +
            "GROUP BY \n" +
            "    p.pd_id, p.pd_name, p.pd_content, p.pd_price, p.creat_date, p.pull_up_date,\n" +
            "    p.latitude, p.longitude, p.views, p.detail_addr, p.deal_status, c.ctgr_name,\n" +
            "    g.min_people, g.max_people, g.round_yn, g.clos_date, un.univ_addr, u.nickname;", nativeQuery = true)
    public GetOneGroup findByPdId(@Param("univ_id") Long univ_id, @Param("id") Long id);


    // 실례합니다
    public Optional<Group> findByProduct(Product product);

    Optional<Group> findByProduct_PdId(Long pdId);

}
