package com.sparta.backoffice.controller;

import com.sparta.backoffice.dto.AuthRequestDto;
import com.sparta.backoffice.dto.StatusResponseDto;
import com.sparta.backoffice.jwt.JwtUtil;
import com.sparta.backoffice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final JwtUtil jwtUtil;

    @GetMapping("/api/auth/login-page")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response,String name){
        jwtUtil.deleteCookie(request,response,"Authorization");
        return "redirect:/api/auth/login-page";
    }

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @PostMapping("/api/auth/signup")
    public ResponseEntity<StatusResponseDto> signup(@Valid @RequestBody AuthRequestDto requestDto) {
        try {
            userService.signup(requestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((new StatusResponseDto("중복된 username 입니다.", HttpStatus.BAD_REQUEST.value())));
        }
        return ResponseEntity.status(201).body(new StatusResponseDto("회원가입 성공", HttpStatus.CREATED.value()));
    }
}
