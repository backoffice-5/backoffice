package com.sparta.backoffice.repository;

import com.sparta.backoffice.entity.Follow;
import com.sparta.backoffice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findAllByFollower(User user);
    List<Follow> findAllByFollowing(User user);
    // 팔로우 받는 사람, 팔로우 하는 사람
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
}
