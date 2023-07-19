package com.sparta.backoffice.controller;

import com.sparta.backoffice.dto.CommentResponseDto;
import com.sparta.backoffice.dto.PostResponseDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ViewController {

    private final PostService postService;
    private final CommentLikeService commentLikeService;
    private final PostLikeService postlikeService;
    private  final PostRepository postRepository;

    @GetMapping("/post/{id}")
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

    @GetMapping("/post")
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


}