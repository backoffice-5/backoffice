package com.sparta.backoffice.dto;

import com.sparta.backoffice.entity.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostsResponseDto {

    private Long id;
    private String title;
    private String nickname;
    private long commentCount;
    private long views;
    private long likeCount;

    public PostsResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.nickname = post.getNickname();
        this.commentCount = post.getCommentCount();
        this.views = post.getViews();
        this.likeCount = post.getLikeCount();
    }
}
