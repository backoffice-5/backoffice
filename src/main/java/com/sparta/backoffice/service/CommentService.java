package com.sparta.backoffice.service;

import com.sparta.backoffice.dto.CommentRequestDto;
import com.sparta.backoffice.dto.CommentResponseDto;
import com.sparta.backoffice.entity.Comment;
import com.sparta.backoffice.entity.CommentLike;
import com.sparta.backoffice.entity.Post;
import com.sparta.backoffice.entity.User;
import com.sparta.backoffice.repository.CommentLikeRepository;
import com.sparta.backoffice.repository.CommentRepository;
import com.sparta.backoffice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

import java.util.ArrayList;
import java.util.List;


import static com.sparta.backoffice.entity.UserRoleEnum.ADMIN;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepository commentLikeRepository;

    // 댓글 반환 리스트 수정 .. (해당 댓글 반환 타입마다 boolean 타입의 속성을 지정해줌)
    public List<CommentResponseDto> commentlikefind(Long postid, User user, List<CommentResponseDto> list) {
        Post post = findPost(postid);

        // comment like 에 있는 post에 대한 commentlike 리스트를 만든다 (해당 사용자와, post에 대한 리스트만 갖고옴)
        // 해당 게시글에 있는 댓글들 중, 사용자가 좋아요를 누른 댓글들의 좋아요 리스트만 갖고오게 됨
        List<CommentLike> commentlikeList = commentLikeRepository.findAllByUserAndPost(user, post).stream().toList();
        // 그 둘을 비교해서 responstdto에 boolean을 setting 해주면 되는데

        for (CommentResponseDto c: list) {
            // 여기서 이제 하나씩 비교.. 를 해야되는데 .. 이중 for문 돌겠네
            for (CommentLike cc: commentlikeList) {
                // commentlike에 있는 댓글의 ld와, commentresponsedto에 있는 댓글의 id가 일치하면
                if (cc.getComment().getId() == c.getId()) {
                    if (cc.getLike()) {
                        log.info("좋아요 되어있는 commentid: "+ c.getId());
                        c.setLike(true);
                        c.setStringLike("true");
                    } else {
                        log.info("좋아요 안되어있는 commentid: "+ c.getId());
                        c.setLike(false);
                    }
                } else {
                    c.setLike(false);
                }
            }
        }

        // 뭘 return 해줘야하나 ...
        return list;
    }

    @Transactional
    public CommentResponseDto createComment(Long postid, CommentRequestDto commentRequestDto, User user) {
        log.info("postid: " + postid + ", username: " + user.getUsername());

        // 댓글 생성
        Comment comment = new Comment(commentRequestDto);
        // 해당되는 post 찾기
        Post post = postRepository.findById(postid).orElseThrow();

        // 해당 comment에 post와 user 설정
        comment.setPost(post);
        comment.setUser(user);

        // repository에 save
        commentRepository.save(comment);

        // post의 댓글 리스트에 댓글 추가
        post.addcomment(comment);
        postRepository.save(post);

        // commentResponseDto에 담아서 return
        return new CommentResponseDto(comment);
    }

    @Transactional
    public ResponseEntity<String> updateComment(Long commentid, CommentRequestDto commentRequestDto, User user) {
        log.info("commentid: " + commentid + ", username: " + user.getUsername());

        // 해당되는 댓글 찾기
        Comment comment = findComment(commentid);

        if (comment.getUser().getUsername().equals(user.getUsername())|| user.getRole().equals(ADMIN) ) {
            // 댓글 내용 update
            comment.update(commentRequestDto);
            return ResponseEntity.ok().body("댓글 수정 완료");
        } else {
            return ResponseEntity.badRequest().body("댓글 수정 실패");
        }
    }

    public ResponseEntity<String> deleteComment(Long postid, Long commentid, User user) {
        log.info("commentid: " + commentid + ", username: " + user.getUsername());

        // 해당되는 댓글 찾기
        Comment comment = findComment(commentid);

        // 해당되는 post 찾기
        Post post = postRepository.findById(postid).orElseThrow();

        if (comment.getUser().getUsername().equals(user.getUsername()) || user.getRole().equals(ADMIN) ) {
            // 댓글에 해당되는 애들 전부 삭제
            commentLikeRepository.deleteAll(commentLikeRepository.findAllByComment(comment));

            // 댓글 delete
            commentRepository.delete(comment);

            // 게시글의 댓글 리스트에서도 삭제하기
            post.deletecomment(comment);
            postRepository.save(post);

            return ResponseEntity.ok().body("댓글 삭제 완료");
        } else {
            return ResponseEntity.badRequest().body("댓글 삭제 실패");
        }

    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    private Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow();
    }

    private Optional<CommentLike> findCommentLike(Comment comment, User user) {
        return commentLikeRepository.findByCommentAndUser(comment, user);
    }

    public List<CommentResponseDto> getAllComment() {
        List<Comment> commentList = commentRepository.findAll();
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (int i = 0; i < commentList.size(); i++) {
            commentResponseDtoList.add(new CommentResponseDto(commentList.get(i)));
        }
        return commentResponseDtoList;
    }
}
