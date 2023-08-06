package com.sparta.backoffice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.backoffice.dto.AuthRequestDto;
import com.sparta.backoffice.dto.PostResponseDto;
import com.sparta.backoffice.dto.StatusResponseDto;
import com.sparta.backoffice.filter.JwtAuthorizationFilter;
import com.sparta.backoffice.jwt.JwtUtil;
import com.sparta.backoffice.service.KakaoService;
import com.sparta.backoffice.service.NaverService;
import com.sparta.backoffice.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.yaml.snakeyaml.util.UriEncoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;
    private final NaverService naverService;
    private final JwtUtil jwtUtil;

    @GetMapping("/api/auth/login-page")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response,String name){
        jwtUtil.deleteCookie(request,response,"Authorization");
        return "redirect:/";
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

    @GetMapping("/api/auth/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        String token = kakaoService.kakaoLogin(code);

        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, UriEncoder.encode(token));
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }

    @GetMapping("/api/auth/naver/callback")
    public String naverLogin(@RequestParam String code, HttpServletResponse response) throws UnsupportedEncodingException, JsonProcessingException {
        String token = naverService.naverLogin(code);

        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, UriEncoder.encode(token));
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }
}
