package com.sparta.backoffice.controller;

import com.sparta.backoffice.dto.ProfileResponseDto;
import com.sparta.backoffice.entity.User;
import com.sparta.backoffice.security.UserDetailsImpl;
import com.sparta.backoffice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProfileViewController {


    private final ProfileService profileService;

    // 프로필 정보 가져오기
    @GetMapping("/profile")
    public String viewProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                              Model model) {

        // 로그인한 회원 정보 profileResponseDto 담기
        ProfileResponseDto profileResponseDto = profileService.getProfile(userDetails.getUser());

        //정보 모델에 담기
        model.addAttribute("profile", profileResponseDto);


        return "profile";

    }

    //프로필 변경 페이지
    @GetMapping("/edit_profile")
    public String getProfileEditPage(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                     Model model) {

        // 로그인한 회원 정보 profileResponseDto 담기
        ProfileResponseDto profileResponseDto = profileService.getProfile(userDetails.getUser());

        //정보 모델에 담기
        model.addAttribute("profile", profileResponseDto);


        return "editProfile";
    }


    //비밀번호 수정 페이지
    @GetMapping("/edit_password")
    public String getPasswordEditPage() {
        return "editPassword";
    }
}


