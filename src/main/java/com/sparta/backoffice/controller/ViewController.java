package com.sparta.backoffice.controller;

import com.sparta.backoffice.dto.CommentResponseDto;
import com.sparta.backoffice.dto.PostResponseDto;
import com.sparta.backoffice.dto.UserResponseDto;
import com.sparta.backoffice.entity.Post;
import com.sparta.backoffice.entity.User;
import com.sparta.backoffice.entity.UserRoleEnum;
import com.sparta.backoffice.repository.CommentRepository;
import com.sparta.backoffice.repository.PostRepository;
import com.sparta.backoffice.repository.UserRepository;
import com.sparta.backoffice.security.UserDetailsImpl;
import com.sparta.backoffice.service.CommentLikeService;
import com.sparta.backoffice.service.PostLikeService;
import com.sparta.backoffice.service.PostService;
import com.sparta.backoffice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ViewController {

    private final PostService postService;
    private final CommentRepository commentRepository;
    private final CommentLikeService commentLikeService;
    private final PostLikeService postlikeService;
    private final UserService userService;
    private final PostRepository postRepository;

    @GetMapping("/")
    public String mainPage(Model model) {
        log.info("게시글 불러오기");
        List<PostResponseDto> postResponseDtoList = postRepository.findAll().stream().map(PostResponseDto::new).toList();
        model.addAttribute("posts", postResponseDtoList);
        return "index";
    }

    @GetMapping("/api/graph")
    public String getGraph() {
        return "graph";
    }

    @GetMapping("/api/getChartData")
    @ResponseBody
    public List<PostResponseDto> graph(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<PostResponseDto> postResponseDtoList = null;
        if (userDetails.getUser().getRole().equals(UserRoleEnum.ADMIN)) {
            postResponseDtoList = postRepository.findAll().stream().map(PostResponseDto::new).toList();
        } else {
            postResponseDtoList = postRepository.findAllByUser(userDetails.getUser()).stream().map(PostResponseDto::new).toList();
        }
        return postResponseDtoList;
    }

    @GetMapping("/api/getChartDataComment")
    @ResponseBody
    public List<CommentResponseDto> graphComment(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<CommentResponseDto> commentResponseDtoList = null;
        if (userDetails.getUser().getRole().equals(UserRoleEnum.ADMIN)) {
            commentResponseDtoList = commentRepository.findAll().stream().map(CommentResponseDto::new).toList();
        } else {
            commentResponseDtoList = commentRepository.findAllByUser(userDetails.getUser()).stream().map(CommentResponseDto::new).toList();
        }
        return commentResponseDtoList;
    }

    @GetMapping("/api/get-data")
    @ResponseBody
    public List<PostResponseDto> getData(@RequestParam("option") String option) {
        // option에 따라서 서버에서 데이터를 가져와서 model에 담아준다.
        // 여기서는 예시로 하드코딩한 데이터를 사용하였습니다.
        List<PostResponseDto> postResponseDtoList = postRepository.findAll().stream().map(PostResponseDto::new).toList();

        if (!option.equals("all")) {
            postResponseDtoList = postService.getPosts(option);
        }

        return postResponseDtoList;
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

    @GetMapping("/api/auth/page/{username}")
    public String getUserPage(@PathVariable String username, @AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        // userRequestDto를 넘겨서 해당 user에 대한 정보를 가져옴 (그 유저가 작성한 post들! 과 해당 유저의 팔로워 팔로잉 숫자까지)
        // model에 담기 - posts와 profile로
        log.info(username);
        // 해당 사용자 검색
        User user = userService.findUser(username);

        Boolean follow = false;
        if (userDetails != null) {
            follow = userService.findFollowing(userDetails.getUser(), user);
        }
        model.addAttribute("follow", follow);

        // 해당 프로필 사용자의 정보
        UserResponseDto userResponseDto = userService.findUserProfile(user);
        model.addAttribute("profile", userResponseDto);
        
        // 해당 프로필 사용자의 게시글 목록
        List<PostResponseDto> postResponseDtoList = postService.findPosts(user);
        model.addAttribute("posts", postResponseDtoList);
        // 그리고 로그인 한 유저가 해당 팔로워를 팔로우 했는지 안했는지 에 대한 boolean 타입의 필드도 가져옴
        
        return "userPage";
    }

}