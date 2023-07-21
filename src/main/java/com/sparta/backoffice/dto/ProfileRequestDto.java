package com.sparta.backoffice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProfileRequestDto {

    private String username;
    private String nickname;

    private boolean admin = false;

    private String password;
    private String newPassword;
    private String checkPassword;

}
