package org.example.project302.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.example.project302.user.dto.LikeProductRequest;
import org.example.project302.user.dto.LikeProductResponse;
import org.example.project302.user.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    // 찜 등록
    @PostMapping("/likes/add")
    public ResponseEntity<LikeProductResponse> addLike(@RequestBody LikeProductRequest likeRequest) {
        LikeProductResponse response = likeService.addLike(likeRequest.getLikePd(), likeRequest.getUserId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/likes/del")
    public ResponseEntity<LikeProductResponse> delLike(@RequestBody LikeProductRequest likeRequest) {
        likeService.delLike(likeRequest.getLikePd(), likeRequest.getUserId());
        // 찜 삭제 성공 응답을 클라이언트에게 반환
        LikeProductResponse response = new LikeProductResponse(likeRequest.getLikePd(), likeRequest.getUserId());
        return ResponseEntity.ok(response);
    }
}
