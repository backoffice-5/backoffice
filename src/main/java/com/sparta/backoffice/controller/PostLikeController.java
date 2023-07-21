package com.sparta.backoffice.controller;

import com.sparta.backoffice.entity.User;
import com.sparta.backoffice.security.UserDetailsImpl;
import com.sparta.backoffice.service.PostLikeService;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PostLikeController {

    private PostLikeService postLikeService;
    public PostLikeController(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    @PostMapping("/api/post/{id}/like")
    public ResponseEntity<String> addPostLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        try {
            postLikeService.addPostLike(id, user);
            return ResponseEntity.ok().body("댓글 좋아요 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("본인의 게시글에는 좋아요를 누를 수 없습니다.");
        }
    }

    @PutMapping("/api/post/{id}/like")
    public ResponseEntity<String>  cancelPostLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        try {
            postLikeService.cancelPostLike(id, user);
            return ResponseEntity.ok().body("댓글 좋아요 취소 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("좋아요를 누른 적이 없습니다.");
        }
    }
}
