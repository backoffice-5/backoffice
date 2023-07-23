package com.sparta.backoffice.repository;

import com.sparta.backoffice.entity.Comment;
import com.sparta.backoffice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUser(User user);
}
