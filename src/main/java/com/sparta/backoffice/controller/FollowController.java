package com.sparta.backoffice.controller;

import com.sparta.backoffice.entity.User;
import com.sparta.backoffice.security.UserDetailsImpl;
import com.sparta.backoffice.service.FollowService;
import com.sparta.backoffice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final UserService userService;

    @PostMapping("/api/follow/{username}")
    public void followUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable String username) {
        log.info(username);
        // 팔로우할 대상의 user 찾기
        User user = userService.findUser(username);
        // 팔로우의 대상, 팔로우 하는 사람
        followService.followUser(userDetails.getUser(), user);
    }


    @DeleteMapping("/api/follow/{username}")
    public void unfollowUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable String username) {
        // 언팔로우할 대상의 user 찾기
        User user = userService.findUser(username);
        // 언팔로우의 대상, 언팔로우 하는 사람
        followService.unfollowUser(userDetails.getUser(), user);
    }


}
