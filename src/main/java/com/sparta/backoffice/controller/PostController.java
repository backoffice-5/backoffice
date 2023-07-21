package com.sparta.backoffice.controller;

import com.sparta.backoffice.dto.PostRequestDto;
import com.sparta.backoffice.dto.PostResponseDto;
import com.sparta.backoffice.dto.PostsResponseDto;
import com.sparta.backoffice.entity.User;
import com.sparta.backoffice.security.UserDetailsImpl;
import com.sparta.backoffice.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class PostController {

    PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 작성
    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return postService.createPost(requestDto, user);
    }

    // 게시글 업데이트
    @PutMapping("/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return postService.updatePost(id, requestDto, user);
    }

    //게시글 삭제
    @DeleteMapping("/post/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("게시글 삭제 시도");
        User user = userDetails.getUser();
        try {
            postService.deletePost(id, user);
            return ResponseEntity.ok().body("게시글 삭제 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("본인의 게시글만 삭제할 수 있습니다.");
        }

    //게시글 검색
    @GetMapping("/post")
    public List<PostsResponseDto> searchPost(@RequestParam String text) {
        return postService.searchPost(text);
    }
}
