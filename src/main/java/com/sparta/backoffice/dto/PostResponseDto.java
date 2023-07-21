package com.sparta.backoffice.dto;

import com.sparta.backoffice.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostResponseDto {

    private Long id;
    private String username;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private long views;
    private long likeCount;
    private long commentCount;
    private Boolean anonymous;
    private List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.username = post.getUsername();
        this.nickname = post.getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createAt = post.getCreatedAt();
        if (post.getNickname().contains("*")) {
            this.anonymous = true;
        } else {
            this.anonymous = false;
        }
        this.modifiedAt = post.getModifiedAt();
        this.views = post.getViews();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();

        for (int i = 0; i < post.getCommentList().size(); i++) {
            commentResponseDtoList.add(new CommentResponseDto(post.getCommentList().get(i)));
        }
    }
}