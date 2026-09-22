package org.example.project302.user.repository;

import org.example.project302.user.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    // 찜 여부
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM likes WHERE like_pd = :pdId AND user_id = :userId", nativeQuery = true)
    int existsByLikePdAndUserId(@Param("pdId") Long pdId, @Param("userId") Long userId);

    // 찜 삭제(상품)
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM likes WHERE like_pd = :pdId AND user_id = :userId", nativeQuery = true)
    int deleteByLikePdAndUser(Long pdId, Long userId);

    // 찜 갯수 가져오기
    @Query(value = "SELECT COALESCE(COUNT(l.like_pd), 0) AS like_count \n" +
            "FROM products p LEFT JOIN likes l ON p.pd_id = l.like_pd \n" +
            "WHERE p.pd_id = :pdId",
            nativeQuery = true)
    public int countByLikePd_Id(@Param("pdId") Long pdId);


}
