package com.sparta.backoffice.controller;

import com.sparta.backoffice.dto.AuthRequestDto;
import com.sparta.backoffice.dto.StatusResponseDto;
import com.sparta.backoffice.filter.JwtAuthorizationFilter;
import com.sparta.backoffice.jwt.JwtUtil;
import com.sparta.backoffice.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final JwtUtil jwtUtil;

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @GetMapping("/api/auth/login-page")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @RequestMapping("/logout")
    public ResponseEntity<StatusResponseDto> logout(HttpServletResponse response, Authentication authResult) throws ServletException, IOException, IOException, IOException {
        log.info("로그아웃 할거야 말리지마셈");
        jwtAuthorizationFilter.deleteAuthentication(response, authResult);
        return ResponseEntity.status(201).body(new StatusResponseDto("로그아웃 성공", HttpStatus.CREATED.value()));
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
