package com.sparta.backoffice.service;

import com.sparta.backoffice.dto.PostRequestDto;
import com.sparta.backoffice.dto.PostResponseDto;
import com.sparta.backoffice.dto.PostsResponseDto;
import com.sparta.backoffice.entity.Post;
import com.sparta.backoffice.entity.User;
import com.sparta.backoffice.entity.UserRoleEnum;
import com.sparta.backoffice.repository.CommentLikeRepository;
import com.sparta.backoffice.repository.PostLikeRepository;
import com.sparta.backoffice.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    // repository 주입받음
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    public PostService(PostRepository postRepository, PostLikeRepository postLikeRepository, CommentLikeRepository commentLikeRepository) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
        this.commentLikeRepository = commentLikeRepository;
    }

    // 게시글 생성
    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post = new Post(requestDto, user);
        post.setNickname(setAnonymous(requestDto, user));
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    //게시글 전체조회: 제목, 닉네임, 댓글수, 좋아요수, 조회수만 List형태로 만들어 return 한다.
    public List<PostResponseDto> getAllPost(String method) {
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        List<Post> postList;

        // PathVariable로 기준을 정해서 그 기준으로 가져오게끔 했음
        if (method.equals("createAt")) {
            postResponseDtoList = postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostResponseDto::new).toList();
//            postList = postRepository.findAllByOrderByCreatedAtDesc();
        } else if (method.equals("views")) {
            postResponseDtoList = postRepository.findAllByOrderByViewsDesc().stream().map(PostResponseDto::new).toList();
//            postList = postRepository.findAllByOrderByViewsDesc();
        } else if (method.equals("likes")) {
            postResponseDtoList = postRepository.findAllByOrderByLikeCountDesc().stream().map(PostResponseDto::new).toList();
//            postList = postRepository.findAllByOrderByLikeCountDesc();
        } else if (method.equals("comment")) {
            postResponseDtoList = postRepository.findAllByOrderByCommentCountDesc().stream().map(PostResponseDto::new).toList();
//            postList = postRepository.findAllByOrderByCommentCountDesc();
        } else {
            throw new IllegalArgumentException("올바른 url이 아닙니다.");
        }

//        for (Post post: postList) {
//            postResponseDtoList.add(new PostsResponseDto(post));
//        }

        return postResponseDtoList;
    }


    //게시글 하나조회: PostResponseDto를 반환함과동시에 Transactional환경을 걸어서 views(조회수)를 하나올린다.
    @Transactional
    public PostResponseDto getOnePost(Long id) {
        // post entity를 찾아옴
        Post post = findById(id);
        // views를 하나 올린뒤 -> Dto로 만들어서 return
        post.plusViews(post);
        return new PostResponseDto(post);
    }

    //게시글 업데이트
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user) {
        // post entity를 찾아옴
        Post post = findById(id);

        if (post.getUsername().equals(user.getUsername())) {
            post.update(requestDto);
            post.setNickname(setAnonymous(requestDto, user));
        } else if (user.getRole().equals(UserRoleEnum.ADMIN)){
            post.update(requestDto);
            User author = post.getUser();
            post.setNickname(setAnonymous(requestDto, author));
        } else {
            throw new IllegalArgumentException("본인이 아니거나 관리자가 아니면 수정할수 없습니다.");
        }

        return new PostResponseDto(post);
    }

    //게시글 삭제
    public void deletePost(Long id, User user) {
        Post post = findById(id);
        if (post.getUsername().equals(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN)) {
            // 해당 게시글과 연동된 댓글들의 좋아요 기록 전부 삭제하기
            commentLikeRepository.deleteAll(commentLikeRepository.findAllByPost(post));
            // 해당 게시글과 연관된 게시글 좋아요 기록 전부 삭제하기
            postLikeRepository.deleteAll(postLikeRepository.findAllByPost(post));
            postRepository.delete(post);
        } else {
            throw new IllegalArgumentException("본인이 아니거나 관리자가 아니면 삭제할수 없습니다.");
        }
    }

    // 검색 기능
    public List<PostsResponseDto> searchPost(String text) {
        List<Post> postList = postRepository.findAll();
        List<PostsResponseDto> postsResponseDtoList = new ArrayList<>();
        for (int i = 0; i < postList.size(); i++) {
            if (postList.get(i).getTitle().contains(text) || postList.get(i).getContent().contains(text)) {
                postsResponseDtoList.add(new PostsResponseDto(postList.get(i)));
            }
        }
        return postsResponseDtoList;
    }


    //---------------------private 메서드------------------------------
    // id로 Post entity를 찾아주는 메서드
    private Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );
    }
    // request의 anonymous가 true일 경우 익명으로, 아니면 그냥 닉네임을 반환하는 메서드
    private String setAnonymous(PostRequestDto requestDto, User user) {
        if (requestDto.isAnonymous()) {   //만약에 익명이 true로 되어있다면 앞에 한자리를 제외하고 *로 처리함
            return user.getNickname().charAt(0) + "*".repeat(user.getNickname().length() - 1);
        } else {
            return user.getNickname();
        }
    }
}
