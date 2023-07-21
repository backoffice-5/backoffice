package com.sparta.backoffice.repository;

import com.sparta.backoffice.entity.Comment;
import com.sparta.backoffice.entity.CommentLike;
import com.sparta.backoffice.entity.Post;
import com.sparta.backoffice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByCommentAndUser(Comment comment, User user);

    List<CommentLike> findAllByUserAndPost(User user, Post post);
    List<CommentLike> findAllByComment(Comment comment);

    List<CommentLike> findAllByPost(Post post);
}
