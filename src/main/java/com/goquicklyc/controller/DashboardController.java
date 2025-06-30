package com.goquicklyc.controller;

import com.goquicklyc.model.User;
import com.goquicklyc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email).orElse(null);
        if (user != null) {
            model.addAttribute("role", user.getRole().name());
        }
        return "dashboard";
    }
} 