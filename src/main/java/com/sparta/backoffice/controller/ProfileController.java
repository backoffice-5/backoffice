package com.sparta.backoffice.controller;

import com.sparta.backoffice.dto.ProfileRequestDto;
import com.sparta.backoffice.dto.ProfileResponseDto;
import com.sparta.backoffice.entity.UserRoleEnum;
import com.sparta.backoffice.security.UserDetailsImpl;
import com.sparta.backoffice.service.ProfileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProfileController {

    private final ProfileService profileService;


    //프로필 정보 가져오기
//    @GetMapping("/profile")
//    public ProfileResponseDto getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//
//        //회원 이름 받기
//        String username = userDetails.getUser().getUsername();
//
//
//
//        //회원 정보 가져오기
//        return profileService.getProfile(userDetails.getUser());
//
//
//    }


    //회원 프로필 개별 조회(관리자 모드)
    @GetMapping("/profile/{username}")
    public ProfileResponseDto getProfileByAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @PathVariable String username) {
        //관리자 확인
        UserRoleEnum role = userDetails.getUser().getRole();

        return profileService.getProfileByAdmin(username, role);

    }

    //전체 프로필 조회(관리자 모드)
    @GetMapping("/profiles")
    public List<ProfileResponseDto> getProfileList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        //관리자 확인
        UserRoleEnum roleEnum = userDetails.getUser().getRole();
        //프로필 전체 조회
        return profileService.getProfileList(roleEnum);

    }

    //관리자가 일반 회원에게 관리자 권한 부여
    @PutMapping("profile/role/{username}")
    public String userToAdmin(@PathVariable String username,
                              @AuthenticationPrincipal UserDetailsImpl userDetails,
                              @RequestBody ProfileRequestDto profileRequestDto) {

        //관리자 확인
        UserRoleEnum role = userDetails.getUser().getRole();

        // 받아온 값 저장
        Boolean checkRole = profileRequestDto.isAdmin();

        return profileService.userToAdmin(username, role, checkRole);


    }

    //관리자가 다른 관리자 권한 삭제
    @PutMapping("profile/role/delete/{username}")
    public String dropAdmin(@PathVariable String username,
                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                            @RequestBody ProfileRequestDto profileRequestDto) {
        //관리자 확인
        UserRoleEnum role = userDetails.getUser().getRole();

        // 받아온 값 저장
        Boolean checkRole = profileRequestDto.isAdmin();

        return profileService.dropAdmin(username, role, checkRole);

    }


    //프로필 수정
    @PutMapping("/profile/update")
    public ProfileResponseDto profileUpdate(@RequestBody ProfileRequestDto profileRequestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                            HttpServletResponse servletResponse) {

        //회원 이름
        String username = profileRequestDto.getUsername();

        //회원 비밀번호 받기
        String password = profileRequestDto.getPassword();

        //회원 정보 수정
        return profileService.updateProfile(password, username, profileRequestDto, userDetails.getUser());


    }

    //비밀번호 변경
    @PutMapping("profile/password")
    public String passwordUpdate(@RequestBody ProfileRequestDto profileRequestDto,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        //유저 받기
        String username = profileRequestDto.getUsername();

        //현재 비밀번호 확인
        String password = profileRequestDto.getPassword();

        //새 비밀번호 입력
        String newPassword = profileRequestDto.getNewPassword();

        // 새 비밀번호 확인
        String checkPassword = profileRequestDto.getCheckPassword();

        if(newPassword.equals(checkPassword)) {
            profileService.passwordUpdate(username, password, newPassword, userDetails.getUser());
        }
        else throw new IllegalArgumentException("새 비밀번호가 일치하지 않습니다.");

        return "비밀번호가 변경 되었습니다.";
    }


}



