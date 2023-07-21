package com.sparta.backoffice.service;

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

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final PostRepository postRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;

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
    public ResponseEntity<String> addcommentlike(Long postid, Long commentid, User user) {
        Post post = findPost(postid);
        // 댓글 찾기
        Comment comment = findComment(commentid);

        if (comment.getUser().getUsername().equals(user.getUsername())) {   // 댓글 작성자가 본인이라면
            return ResponseEntity.badRequest().body("본인의 댓글에는 좋아요를 누를 수 없습니다");
        } else {
            // 댓글 좋아요 기록 찾기
            Optional<CommentLike> commentLike = findCommentLike(comment, user);
            if (!commentLike.isPresent()) { // 기록이 없으면
                CommentLike newCommentLike = new CommentLike(post, user, comment);
                newCommentLike.setlike(true);
                commentLikeRepository.save(newCommentLike);

                // 댓글의 좋아요 수 + 1
                comment.setCommentlikeCount(comment.getCommentlikeCount() + 1);

                return ResponseEntity.ok().body("댓글 좋아요 성공");
            } else {                  // 이미 기록이 있으면
                if (commentLike.get().getLike()) { // 그리고 이미 true 라면
                    return ResponseEntity.badRequest().body("이미 댓글에 좋아요가 눌려 있습니다.");
                } else {                     // 기록이 false라면 true로 변경해줌
                    // 댓글 좋아요 기록 true로 변경
                    commentLike.get().setlike(true);

                    // 댓글의 좋아요 수 + 1
                    comment.setCommentlikeCount(comment.getCommentlikeCount() + 1);
                    return ResponseEntity.ok().body("댓글 좋아요 성공");
                }
            }
        }
    }

    @Transactional
    public ResponseEntity<String> cancelcommentlike(Long postid, Long commentid, User user) {
        Comment comment = findComment(commentid);
        // 댓글 좋아요 기록 찾기
        Optional<CommentLike> commentLike = findCommentLike(comment, user);

        if (!commentLike.isPresent()) {       // 기록이 없다면
            return ResponseEntity.badRequest().body("댓글에 좋아요를 누른 적이 없습니다.");
        } else {                         // 기록이 있다면
            if (commentLike.get().getLike()) { // 그 기록이 true 라면
                // 댓글 좋아요 기록 false로 변경
                commentLike.get().setlike(false);

                // 댓글의 좋아요 수 + 1
                comment.setCommentlikeCount(comment.getCommentlikeCount() - 1);
                return ResponseEntity.ok().body("댓글 좋아요 취소 성공");
            } else {                     // 그 기록이 false 라면
                return ResponseEntity.badRequest().body("댓글에 이미 좋아요가 취소되어 있습니다.");
            }
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
}
