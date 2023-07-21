package com.sparta.backoffice.dto;

import com.sparta.backoffice.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;                   // 댓글 ID
    private String username;           // 댓글 작성자
    private String nickname;
    private String commentcontents;    // 댓글 내용
    private LocalDateTime createdAt;   // 댓글 작성일
    private LocalDateTime modifiedAt;  // 댓글 수정일
    private Integer commentlikeCount;  // 댓글 좋아요 수
    private Boolean liked;
    private String like;
    private String nickname;
    private String postTitle;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.nickname = comment.getUser().getNickname();
        this.username = comment.getUser().getUsername();
        this.commentcontents = comment.getCommentcontents();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.commentlikeCount = comment.getCommentlikeCount();
        this.nickname = comment.getUser().getNickname();
        this.postTitle = comment.getPost().getTitle();
    }

    public void setLike(Boolean b) {
        this.liked = b;
    }
    public void setStringLike(String s) {this.like = s;}
}
