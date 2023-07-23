package com.sparta.backoffice.repository;

import com.sparta.backoffice.entity.Post;
import com.sparta.backoffice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findAllByOrderByViewsDesc();
    List<Post> findAllByOrderByLikeCountDesc();
    List<Post> findAllByOrderByCommentCountDesc();
    List<Post> findAllByUser(User user);

    List<Post> findAllByUserOrderByCreatedAtDesc(User user);
}
