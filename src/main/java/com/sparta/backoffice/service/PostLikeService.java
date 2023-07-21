package com.sparta.backoffice.service;

import com.sparta.backoffice.entity.Post;
import com.sparta.backoffice.entity.PostLike;
import com.sparta.backoffice.entity.User;
import com.sparta.backoffice.repository.PostLikeRepository;
import com.sparta.backoffice.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PostLikeService {

    private PostRepository postRepository;
    private PostLikeRepository postLikeRepository;
    public PostLikeService(PostRepository postRepository, PostLikeRepository postLikeRepository) {
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
    }

    @Transactional
    public void addPostLike(Long id, User user) {
        Post post = findPostById(id);
        if (post.getUsername().equals(user.getUsername())) {
            // 본인 거르기
            throw new IllegalArgumentException("본인글에는 좋아요를 누를수 없습니다.");
        } else {
            Optional<PostLike> postLike = findPostLikeByPostAndUser(post, user);
            if (postLike.isEmpty()) {   //기록이 없으면
                PostLike newPostLike = new PostLike(post, user);
                postLikeRepository.save(newPostLike);
                post.setLikeCount(post.getLikeCount() + 1);
            } else {                     //기록이 있으면
                if (postLike.get().getLike()) {
                    throw new IllegalArgumentException("이미 좋아요가 눌려있습니다.");
                } else {
                    postLike.get().setLike(true);
                    post.setLikeCount(post.getLikeCount() + 1);
                }
            }
        }
    }

    @Transactional
    public void cancelPostLike(Long id, User user) {
        Post post = findPostById(id);

        Optional<PostLike> postLike = findPostLikeByPostAndUser(post, user);
        if (postLike.isEmpty()) { //기록이 없다면
            throw new IllegalArgumentException("좋아요를 누른적이 없습니다.");
        } else {                  //기록이 있으면
            if (postLike.get().getLike()) {
                postLike.get().setLike(false);
                post.setLikeCount(post.getLikeCount() - 1);
            } else {
                throw new IllegalArgumentException("이미 좋아요를 취소한 상태입니다.");
            }
        }
    }

    public Boolean likefind(Long id, User user) {
        Post post = findPostById(id);
        Optional<PostLike> postLike = findPostLikeByPostAndUser(post, user);
        if (postLike.isPresent()) {
            if (postLike.get().getLike()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private Post findPostById(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("글을 찾을수 없습니다.(PostLikeService)")
        );
    }
    private Optional<PostLike> findPostLikeByPostAndUser(Post post, User user) {
        return postLikeRepository.findByPostAndUser(post, user);
    }
}
