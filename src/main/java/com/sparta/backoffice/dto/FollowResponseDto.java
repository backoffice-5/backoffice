package com.sparta.backoffice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowResponseDto {

    private String nickname;
    private Long follower;
    private Long following;

    public FollowResponseDto (String nickname, Long follower, Long following){
        this.nickname = nickname;
        this.follower = follower;
        this.following = following;
    }
}
