package com.sparta.backoffice.controller;

import com.sparta.backoffice.dto.CommentResponseDto;
import com.sparta.backoffice.dto.PostResponseDto;
import com.sparta.backoffice.dto.PostsResponseDto;
import com.sparta.backoffice.entity.Post;
import com.sparta.backoffice.repository.PostRepository;
import com.sparta.backoffice.security.UserDetailsImpl;
import com.sparta.backoffice.service.CommentLikeService;
import com.sparta.backoffice.service.PostLikeService;
import com.sparta.backoffice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ViewController {

    private final PostService postService;
    private final CommentLikeService commentLikeService;
    private final PostLikeService postlikeService;
    private final PostRepository postRepository;

    @GetMapping("/")
    public String mainPage(Model model) {
        log.info("게시글 불러오기");
        List<PostResponseDto> postResponseDtoList = postRepository.findAll().stream().map(PostResponseDto::new).toList();
        model.addAttribute("posts", postResponseDtoList);
        return "index";
    }

    @GetMapping("/posts/{method}")
    public String getAllPost(@PathVariable String method, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<PostResponseDto> postResponseDto = postService.getAllPost(method);

        if (method.equals("createAt")) {
            model.addAttribute("posts", postResponseDto);
        } else if (method.equals("views")) {
            model.addAttribute("posts", postResponseDto);
        } else if (method.equals("likes")) {
            model.addAttribute("posts", postResponseDto);
        } else if (method.equals("comment")) {
            model.addAttribute("posts", postResponseDto);
        } else {
            throw new IllegalArgumentException("올바른 url이 아닙니다.");
        }

        return "redirect:/";
    }

    @GetMapping("/api/post")
    public String newPost(@RequestParam(required=false) Long id, Model model) {
        if (id == null) { // id가 없으면 설정
            log.info("새 게시글");
            // 기본 생성자로 빈 객체를 만듦
            model.addAttribute("post", new PostResponseDto());

        } else { // id가 있으면 수정
            log.info("수정");
            Post post = postRepository.findById(id).orElseThrow();
            // 기존 값을 가져오는 findById 메서드 호출
            model.addAttribute("post", new PostResponseDto(post));
        }
        return "newpost";
    }

    @GetMapping("/api/post/{id}")
    public String getOnePost(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 게시글 데이터
        PostResponseDto postResponseDto = postService.getOnePost(id);
        model.addAttribute("post", postResponseDto);

        // 게시글 좋아요 데이터
        Boolean like = postlikeService.likefind(id, userDetails.getUser());
        log.info(String.valueOf(like));
        model.addAttribute("like", like);

        List<CommentResponseDto> commentResponseDtoList = commentLikeService.commentlikefind(id, userDetails.getUser(), postResponseDto.getCommentResponseDtoList());
        // 게시글에 해당하는 댓글 데이터
        model.addAttribute("comments", commentResponseDtoList);

        // post.html 뷰 조회
        return "post";
    }




}