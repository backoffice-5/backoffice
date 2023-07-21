package com.sparta.backoffice.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class AuthRequestDto {

    @Pattern(regexp = "^^[a-z0-9]{4,10}$", message = "최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 구성해야합니다.")
    private String username;

    private String nickname;

    private boolean admin = false;

    private String adminToken = "";

    @Pattern(regexp = "^[a-zA-Z0-9@$!%*?&]{8,15}$", message = "최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 구성해야합니다.")
    private String password;

}

