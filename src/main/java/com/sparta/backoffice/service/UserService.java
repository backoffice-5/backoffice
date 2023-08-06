package com.sparta.backoffice.service;

import com.sparta.backoffice.dto.AuthRequestDto;
import com.sparta.backoffice.dto.UserResponseDto;
import com.sparta.backoffice.entity.Follow;
import com.sparta.backoffice.entity.User;
import com.sparta.backoffice.entity.UserRoleEnum;
import com.sparta.backoffice.repository.FollowRepository;
import com.sparta.backoffice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final FollowRepository followRepository;

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";


    public void signup(AuthRequestDto requestDto) {
        String username = requestDto.getUsername();

        String nickname = requestDto.getNickname();

        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> checkUsername = userRepository.findByUsername(username);

        if ((checkUsername.isPresent())) {
            throw new IllegalArgumentException("이미 존재하는 username 입니다.");
        }
        User user = new User(username, nickname, role, password);
        userRepository.save(user);
    }

    public UserResponseDto findUserProfile(User user) {
        // 레포지토리에서 가져오기
        List<Follow> followers = followRepository.findAllByFollower(user);   // 팔로우 받는 사람 (해당 사람이 팔로우 받은 내역)
        List<Follow> followings = followRepository.findAllByFollowing(user); // 팔로우 하는 사람 (해당 사람이 팔로우를 한 내역)

        return new UserResponseDto(user.getUsername(), user.getNickname(),
                followings.size(), followers.size());
    }

    public Boolean findFollowing(User user, User followUser) {
        // 앞에꺼가 해당 사용자, user1이 지금 로그인한 사용자
        // 그러니까 user1이 user를 팔로우했는지 안했는지를 보면 됨 !

        Optional<Follow> follow = followRepository.findByFollowerAndFollowing(user, followUser);
        if (follow.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    public User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

}
