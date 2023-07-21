package com.sparta.backoffice.dto;

import com.sparta.backoffice.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.pl.REGON;

@Getter
@RequiredArgsConstructor
public class ProfileResponseDto {

    private String username;
    private String nickname;




    //회원 정보
    public ProfileResponseDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
    }


}
