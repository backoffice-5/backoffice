package com.sparta.backoffice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private String nickname;
    private String username;
    private Integer follower;   // 해당 사람이 팔로우를 받은 내역
    private Integer following;  // 해당 사람이 팔로우 한 내역

    public UserResponseDto(String username, String nickname, Integer follower, Integer following) {
        this.username = username;
        this.nickname = nickname;
        this.follower = follower;
        this.following = following;
    }
}
