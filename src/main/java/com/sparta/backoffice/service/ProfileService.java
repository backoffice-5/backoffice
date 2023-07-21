package com.sparta.backoffice.service;

import com.sparta.backoffice.dto.ProfileRequestDto;
import com.sparta.backoffice.dto.ProfileResponseDto;
import com.sparta.backoffice.entity.User;
import com.sparta.backoffice.entity.UserRoleEnum;
import com.sparta.backoffice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    //프로필 정보 가져오기(사용자)
    public ProfileResponseDto getProfile(User user) {

        //회원 존재확인
        userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원은 존재하지 않습니다"));

        //회원 정보 보여주기
        return new ProfileResponseDto(user);


    }

    //회원 개별 조회(관리자 모드)
    public ProfileResponseDto getProfileByAdmin(String username, UserRoleEnum role) {

        //관리자 확인 체크
        User user = null;
        if (role.equals(UserRoleEnum.ADMIN)) {
            user = findByUsername(username);
        }

        return new ProfileResponseDto(user);

    }


    //전체 회원 조회 (관리자 모드)
    public List<ProfileResponseDto> getProfileList(UserRoleEnum roleEnum) {

        //조회를 담을 리스트 만들기
        List<ProfileResponseDto> profileResponseDtoList = new ArrayList<>();

        //관리자 확인 체크
        List<User> profileList = null;
        if (roleEnum.equals(UserRoleEnum.ADMIN)) {
            profileList = userRepository.findAllByOrderByNickname();
        } else
            throw new IllegalArgumentException("접근 권한이 없습니다.");

        for (User profile : profileList) {
            profileResponseDtoList.add(new ProfileResponseDto(profile));
        }

        return profileResponseDtoList;


    }

    //관리자가 회원 권한 부여(관리자 모드)
    @Transactional
    public String userToAdmin(String username, UserRoleEnum role, Boolean checkRole) {

        //회원 정보 찾기
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        //권한 부여하기
        if (role.equals(UserRoleEnum.ADMIN) && checkRole.equals(true)) {
            user.setRole(UserRoleEnum.ADMIN);
        }


        return "수정이 완료되었습니다";

    }

    //관리자가 관리자 권한 회원으로 변경 (관리자 모드)
    @Transactional
    public String dropAdmin(String username, UserRoleEnum role, Boolean checkRole) {

        //회원 정보 찾기
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원정보를 찾을 수 없습니다."));

        //관리자이면서 받아온 값이 false이면 다른 관리자 회원으로 변경
        if (role.equals(UserRoleEnum.ADMIN) && checkRole.equals(false)) {
            user.setRole(UserRoleEnum.USER);

        }

        return "회원으로 전환되었습니다.";
    }


    // 프로필 업데이트
    @Transactional
    public ProfileResponseDto updateProfile(String password,
                                            String username,
                                            ProfileRequestDto profileRequestDto,
                                            User user) {

        //회원 존재 확인
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 존재하지 않습니다."));

        //비밀번호 확인
        if (!(passwordEncoder.matches(password, author.getPassword()) || user.getRole().equals(UserRoleEnum.ADMIN))) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않거나 접근 권한이 없습니다.");
        }

        //중복 닉네임 체크
        Optional<User> checkNickname = userRepository.findByNickname(profileRequestDto.getNickname());

        if (checkNickname.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 nickname 입니다.");
        }


        //새 닉네임 값 넣기
        author.setNickname(profileRequestDto.getNickname());


        return new ProfileResponseDto(author);
    }


    //비밀번호 변경
    @Transactional
    public void passwordUpdate(String username,
                               String password,
                               String newPassword,
                               User user) {

        //사용자 회원 존재 확인
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 존재하지 않습니다."));



        //비밀번호 확인 & 접근 권한
        if (!(passwordEncoder.matches(password, user.getPassword()) || user.getRole().equals(UserRoleEnum.ADMIN))) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않거나 접근 권한이 없습니다.");
        }

        //username과 같은 이름의 최근 비밀번호를 받아오기
        String lastPasswords = author.getLastPasswords(); // admin -> authorDB lastpasswords:2%dms /4455, author.get~~
        lastPasswords = lastPasswords == null ? author.getPassword() : lastPasswords;

        //지난 패스워드들 " " 기준으로 나누기
        String[] passwordList = lastPasswords.split(" ");

        for (int i = 0; i < passwordList.length; i++) {
            if (passwordEncoder.matches(newPassword, passwordList[i])) {
                throw new IllegalArgumentException("이전에 비밀번호를 사용한 기록이 있습니다.");
            }
        }

        author.updatePassword(passwordEncoder.encode(newPassword)); //새로 받아온 비밀번호 인코딩

        //이전 3개의 비밀번호 배열이 꽉 찼을 때 미뤄내서 값 넣기
        if (passwordList.length == 3) {
            author.updateLastPassword(passwordList[1] + " " + passwordList[2] + " " + passwordEncoder.encode(newPassword));
        } else {//받아온 비밀번호를 " " 기준으로 니눠서 붙여주기
            author.updateLastPassword((String.join(" ", passwordList) + " " + passwordEncoder.encode(newPassword)).trim());
        }
    }


    //username 찾기
    private User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("선택한 회원은 존재하지 않습니다.")
        );
    }


}
