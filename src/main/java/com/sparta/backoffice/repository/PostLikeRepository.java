package com.sparta.backoffice.repository;

import com.sparta.backoffice.entity.Post;
import com.sparta.backoffice.entity.PostLike;
import com.sparta.backoffice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostAndUser(Post post, User user);

    List<PostLike> findAllByPost(Post post);
}
