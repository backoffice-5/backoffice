package com.sparta.backoffice.controller;

import com.sparta.backoffice.dto.CommentRequestDto;
import com.sparta.backoffice.dto.CommentResponseDto;
import com.sparta.backoffice.security.UserDetailsImpl;
import com.sparta.backoffice.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments")
    public List<CommentResponseDto> getAllComment() {
        return commentService.getAllComment();
    }

    @PostMapping("/{postid}/comment")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long postid, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Controller | postid: " + postid + ", username: " + userDetails.getUsername());
        CommentResponseDto commentResponseDto = commentService.createComment(postid, commentRequestDto, userDetails.getUser());
        return ResponseEntity.ok().body(commentResponseDto);
    }

    @PutMapping("/{postid}/comment/{commentid}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentid,
                                            @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Controller | commentid: " + commentid + ", username: " + userDetails.getUsername());
        return commentService.updateComment(commentid, commentRequestDto, userDetails.getUser());
    }

    @DeleteMapping("/{postid}/comment/{commentid}")
    public ResponseEntity<String> deleteComment(@PathVariable Long postid, @PathVariable Long commentid,
                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Controller | commentid: " + commentid + ", username: " + userDetails.getUsername());
        return commentService.deleteComment(postid, commentid, userDetails.getUser());
    }

}
