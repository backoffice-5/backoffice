package com.sparta.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/api/auth/backoffice")
    public String backOfficePage() {
        return "backoffice";
    }
}
