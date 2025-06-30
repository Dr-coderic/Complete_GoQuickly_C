/*
package com.goquicklyc.controller;

import com.goquicklyc.model.User;
import com.goquicklyc.model.UserRole;
import com.goquicklyc.security.JwtUtil;
import com.goquicklyc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.get("email"),
                        loginRequest.get("password")
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userService.getUserByEmail(loginRequest.get("email")).orElseThrow();
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(Map.of("token", token, "role", user.getRole().name()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> registerRequest) {
        if (userService.getUserByEmail(registerRequest.get("email")).isPresent()) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        User user = new User();
        user.setFirstName(registerRequest.get("firstName"));
        user.setLastName(registerRequest.get("lastName"));
        user.setEmail(registerRequest.get("email"));
        user.setPassword(passwordEncoder.encode(registerRequest.get("password")));
        user.setPhoneNumber(registerRequest.get("phoneNumber"));
        user.setHasCar(Boolean.parseBoolean(registerRequest.getOrDefault("hasCar", "false")));
        user.setRole(user.isHasCar() ? UserRole.CAR_OWNER : UserRole.PASSENGER);
        user.setEnabled(true);
        user.setApproved(false);
        userService.saveUser(user);
        return ResponseEntity.ok("User registered successfully");
    }
}
*/ 