package com.carla.contentinsighttool.controller;

import com.carla.contentinsighttool.dto.LoginRequest;
import com.carla.contentinsighttool.dto.LoginResponse;
import com.carla.contentinsighttool.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final String USERNAME = "carla";

    @Value("${app.auth.password}")
    private String password;

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (USERNAME.equals(request.getUsername()) && password.equals(request.getPassword())) {
            return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(USERNAME)));
        }
        return ResponseEntity.status(401).body("{\"error\":\"Invalid credentials\"}");
    }
}