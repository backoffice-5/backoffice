package com.sparta.backoffice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProfileRequestDto {

    private String username;
    private String nickname;

    private String password;
    private String newPassword;

}
