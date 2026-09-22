package org.example.project302.deal.service;

import lombok.RequiredArgsConstructor;
import org.example.project302.deal.dto.*;
import org.example.project302.deal.entity.Participation;
import org.example.project302.deal.entity.Review;
import org.example.project302.deal.entity.ReviewType;
import org.example.project302.deal.repository.ParticipationRepository;
import org.example.project302.deal.repository.ReviewRepository;
import org.example.project302.file.entity.File;
import org.example.project302.file.repository.FileRepository;
import org.example.project302.file.service.FileService;
import org.example.project302.group.entity.Group;
import org.example.project302.group.repository.GroupRepository;
import org.example.project302.product.entity.Product;
import org.example.project302.product.repository.ProductRepository;
import org.example.project302.product.service.ProductService;
import org.example.project302.user.entity.User;
import org.example.project302.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final FileService fileService;
    private final GroupRepository groupRepository;
    private final ParticipationRepository partRepository;


    /*
     *  리뷰 등록 .-≡=★
     */
    @Transactional
    public GetReviewRes saveReview(User user, ReviewForm form) {
        Product product = productRepository.findById(form.getPdId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 없음"));
        User receiver = userRepository.findById(form.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 없음"));
        Review review = reviewRepository.findByProductAndSenderAndReceiver(product, user, receiver)
                .orElse(null);

        if (review != null)
            new IllegalArgumentException("작성한 후기 존재 : " + review.getReviewId());

        // 후기 생성
        review = form.toEntity();
        review.setProduct(product);
        review.setSender(user);
        review.setReceiver(receiver);
        Review saved = reviewRepository.save(review);

        // 회원 점수 업데이트
        updateScore(receiver);

        // res
        return new GetReviewRes(
                saved,
                fileService.getFileLink(saved.getProduct()),
                fileService.getFileLink(saved.getReceiver())
        );
    }


    /*
     *  상품 정보 조회 .-≡=★
     */
    public GetRvwProdRes getProduct(Long pdId) {
        Product product = productRepository.findById(pdId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 없음"));
        Group group = groupRepository.findByProduct(product)
                .orElse(null);
        return new GetRvwProdRes(product, group, fileService.getFileLink(product));
    }


    /*
     *  후기 대상 정보 조회 (중고거래, 공동구매 구매자일 경우) .-≡=★
     */
    public GetReceiverRes getReceiver(User user, Long pdId) {
        Product product = productRepository.findById(pdId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 없음 : " + pdId));

        User receiver = user.equals(product.getUser()) ?
                partRepository.findByProductAndUserIsNot(product, user)
                        .map(Participation::getUser)
                        .orElseThrow(() -> new IllegalArgumentException("대상 유저 없음"))
                : product.getUser();

        // res
        return new GetReceiverRes(
                receiver,
                fileService.getFileLink(receiver),
                product.getUser().equals(receiver) ? ReviewType.BUY : ReviewType.SELL
        );
    }


    /*
     *  후기 대상 정보 조회 (공동구매 판매자일 경우) .-≡=★
     */
    public GetReceiverRes getReceiver(User user, Long pdId, Long userId) {
        Product product = productRepository.findById(pdId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 없음 : " + pdId));
        if (user.equals(product.getUser()))
            new IllegalArgumentException("상품 판매 회원 불일치 : " + user.getUserId());

        User receiver = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("대상 유저 없음 : " + userId));

        // res
        return new GetReceiverRes(
                receiver,
                fileService.getFileLink(receiver),
                ReviewType.SELL
        );
    }


    /*
     *  리뷰 삭제
     */
    @Transactional
    public void removeReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰 없음"));
        reviewRepository.delete(review);
        updateScore(review.getReceiver());
    }


    /*
     *  프로필 - 간단 리뷰 조회
     */
    public GetSimpleRvwRes getSimpleReview(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 없음 : " + userId));

        List<Review> reviews = reviewRepository.findAllByReceiver(user);
        int[] simpleReviews = new int[7];

        for (Review review : reviews) {
            Long simpleRvw = review.getSimpleReview();
            for (int i = 0; i < simpleReviews.length; i++) {
                simpleReviews[i] += simpleRvw % 10;
                simpleRvw /= 10;
            }
        }

        return new GetSimpleRvwRes(user, simpleReviews);
    }


    /*
     *  프로필 - 상세 후기 조회
     */
    public List<GetDetailRvwRes> getDetailReview(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 없음 : " + userId));

        return reviewRepository.findAllByReceiverOrderByCreatDateDesc(user).stream()
                .map(review -> new GetDetailRvwRes(
                        review,
                        fileService.getFileLink(review.getSender()))
                ).collect(Collectors.toList());
    }

    /*
     *  프로필 - 상세 후기 2개만 조회(상품 한 건 조회시 사용)
     */
    public List<GetDetailRvwRes> getTop2DetailReviews(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 없음 : " + userId));

        return reviewRepository.findAllByReceiverOrderByCreatDateDesc(user).stream()
                .filter(review -> review.getProduct() != null)
                .limit(2) // 스트림 결과를 2개로 제한
                .map(review -> {
                    String fileLink = fileService.getFileLink(review.getSender().getUserFileId());
                    return new GetDetailRvwRes(review, fileLink);
                })
                .collect(Collectors.toList());
    }


    /* 회원 점수 업데이트 */
    @Transactional
    protected void updateScore(User user) {
        Long headcnt = reviewRepository.countByReceiver(user);
        List<Review> reviews = reviewRepository.findAllByReceiver(user);
        Long total = 0L;
        for (Review review : reviews) {
            Long simple = review.getSimpleReview();
            while (simple > 0) {
                total += simple % 10;
                simple /= 10;
            }
        }
        Float score = (float) total * 20 / headcnt;
        user.setScore(score);
        userRepository.save(user);
    }

    /*
     *  회원 리뷰 총 갯수
     */
    public Long getTotalReviewCount(User user) {
        return reviewRepository.countByReceiver(user);
    }

}
