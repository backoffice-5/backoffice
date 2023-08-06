package com.sparta.backoffice.service;

import com.sparta.backoffice.entity.Follow;
import com.sparta.backoffice.entity.User;
import com.sparta.backoffice.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    // 팔로우
    public void followUser(User user, User following) {
        Follow follow = new Follow(user, following);
        followRepository.save(follow);
    }

    // 언팔로우
    public void unfollowUser(User user, User following) {
        Follow follow = followRepository.findByFollowerAndFollowing(user, following)
                .orElseThrow();

        if (follow != null) {
            followRepository.delete(follow);
        }
    }



}
