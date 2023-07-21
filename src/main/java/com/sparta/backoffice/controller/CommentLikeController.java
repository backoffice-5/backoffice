package com.sparta.backoffice.controller;

import com.sparta.backoffice.security.UserDetailsImpl;
import com.sparta.backoffice.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping("/api/post/{postid}/comment/{commentid}/like")
    public ResponseEntity<String> addcommentlike(@PathVariable Long postid, @PathVariable Long commentid, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentLikeService.addcommentlike(postid, commentid, userDetails.getUser());
    }

    @PutMapping("/api/post/{postid}/comment/{commentid}/like")
    public ResponseEntity<String> cancelcommentlike(@PathVariable Long postid, @PathVariable Long commentid, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentLikeService.cancelcommentlike(postid, commentid, userDetails.getUser());
    }
}
