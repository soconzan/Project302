package org.example.project302.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project302.group.dto.GetGroupOneResponse;
import org.example.project302.product.dto.GetProductResponse;
import org.example.project302.product.entity.Product;
import org.example.project302.product.repository.ProductRepository;
import org.example.project302.user.dto.LikeProductResponse;
import org.example.project302.user.entity.Like;
import org.example.project302.user.entity.User;
import org.example.project302.user.repository.LikeRepository;
import org.example.project302.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // 찜 여부 확인
    public boolean isLikedProduct(GetProductResponse product, User user) {
        Long pdId = product.getPdId();
        Long userId = user.getUserId();
        return likeRepository.existsByLikePdAndUserId(pdId, userId) > 0;
    }
    // 공구
    public boolean isLikedGroup(GetGroupOneResponse group, User user) {
        Long pdId = group.getPdId();
        Long userId = user.getUserId();
        return likeRepository.existsByLikePdAndUserId(pdId, userId) > 0;
    }

      // 찜 삭제
    public void delLike(Long pdId, Long userId) {
        likeRepository.deleteByLikePdAndUser(pdId, userId);
        log.info("User {} deleted like for product {}", userId, pdId);
    }


    // 찜하기 등록
    public LikeProductResponse addLike(Long pdId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(pdId).orElseThrow(() -> new RuntimeException("Product not found"));

        Like like = new Like();
        like.setUser(user);
        like.setLikePd(product);
        likeRepository.save(like);
        log.info("User {} liked product {}", userId, pdId);

        return new LikeProductResponse(product.getPdId(), user.getUserId());
    }

    // 찜 갯수 반환
    public int cntLike(Long pdId){
        Integer likeCount = likeRepository.countByLikePd_Id(pdId);
        return likeCount != null ? likeCount : 0; // null 처리, 하지만 SQL에서 처리하는 경우 필요 없음
    }


}
