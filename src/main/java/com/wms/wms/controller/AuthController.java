package com.wms.wms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wms.wms.dto.AuthResponse;
import com.wms.wms.dto.LoginRequest;
import com.wms.wms.dto.RegisterResponse;
import com.wms.wms.entity.User;
import com.wms.wms.enums.Role;
import com.wms.wms.service.UserService;
import com.wms.wms.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;
    
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole(Role.OPERATOR);
        }

        User savedUser = userService.save(user);

        RegisterResponse response = new RegisterResponse(
                "User registered successfully",
                savedUser.getUsername(),
                savedUser.getRole().name()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

        User user = userService.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user);

        AuthResponse response = new AuthResponse(
                "Login successful",
                token,
                user.getUsername(),
                user.getRole().name()
        );

        return ResponseEntity.ok(response);
    }
}