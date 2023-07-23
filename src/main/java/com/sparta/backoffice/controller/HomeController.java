package com.sparta.backoffice.controller;

import com.sparta.backoffice.security.UserDetailsImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Controller
public class HomeController {

    @GetMapping("/api/auth/backoffice")
    public String backOfficePage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String roles = userDetails.getUser().getRole().getAuthority();

        Boolean role = false;
        if (roles.equals("ROLE_ADMIN")) {
            role = true;
        }

        model.addAttribute("role", role);

        return "backoffice";
    }
}
